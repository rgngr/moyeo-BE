package com.hanghae.finalProject.rest.alarm.service;

import com.hanghae.finalProject.config.errorcode.Code;
import com.hanghae.finalProject.config.exception.RestApiException;
import com.hanghae.finalProject.config.util.SecurityUtil;
import com.hanghae.finalProject.rest.alarm.dto.AlarmListResponseDto;
import com.hanghae.finalProject.rest.alarm.dto.AlarmListsResponseDto;
import com.hanghae.finalProject.rest.alarm.model.Alarm;
import com.hanghae.finalProject.rest.alarm.model.AlarmList;
import com.hanghae.finalProject.rest.alarm.repository.AlarmListRepository;
import com.hanghae.finalProject.rest.alarm.repository.AlarmRepository;
import com.hanghae.finalProject.rest.alarm.repository.EmitterRepository;
import com.hanghae.finalProject.rest.attendant.model.Attendant;
import com.hanghae.finalProject.rest.attendant.repository.AttendantRepository;
import com.hanghae.finalProject.rest.meeting.model.Meeting;
import com.hanghae.finalProject.rest.user.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlarmService {

    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;
    private final EmitterRepository emitterRepository;
    private final AttendantRepository attendantRepository;
    private final AlarmRepository alarmRepository;
    private final AlarmListRepository alarmListRepository;


    public SseEmitter subscribe(String lastEventId) {
        User user = SecurityUtil.getCurrentUser();
        if (user == null) throw new RestApiException(Code.NOT_FOUND_AUTHORIZATION_IN_SECURITY_CONTEXT);

        Long userId = user.getId();
        String receiverId = userId + "_" + System.currentTimeMillis();

        // 2
        SseEmitter emitter = emitterRepository.save(receiverId, new SseEmitter(DEFAULT_TIMEOUT));

        emitter.onCompletion(() -> emitterRepository.deleteById(receiverId));
        emitter.onTimeout(() -> emitterRepository.deleteById(receiverId));

        // 3
        // 503 에러를 방지하기 위한 더미 이벤트 전송
        sendToClient(emitter, receiverId, "EventStream Created. [userId=" + userId + "]");

        // 4
        // 클라이언트가 미수신한 Event 목록이 존재할 경우 전송하여 Event 유실을 예방
        if (!lastEventId.isEmpty()) {
            Map<String, Object> events = emitterRepository.findAllEventCacheStartWithId(String.valueOf(userId));
            events.entrySet().stream()
                    .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
                    .forEach(entry -> sendToClient(emitter, entry.getKey(), entry.getValue()));
        }

        return emitter;
    }

    private void sendToClient(SseEmitter emitter, String receiverId, Object data) {
        try {
            emitter.send(SseEmitter.event()
                    .id(receiverId)
                    .name("sse")
                    .data(data));
        } catch (IOException exception) {
            emitterRepository.deleteById(receiverId);
            throw new RuntimeException("연결 오류!");
        }
    }

    private void alarmProcess(String receiverId, AlarmList alarmList) {
        // 로그인 한 유저의 SseEmitter 모두 가져오기
        Map<String, SseEmitter> sseEmitters = emitterRepository.findAllStartWithById(receiverId);
        sseEmitters.forEach(
                (key, emitter) -> {
                    // 데이터 캐시 저장(유실된 데이터 처리하기 위함)
                    emitterRepository.saveEventCache(key, alarmList);
                    // 데이터 전송
//                  sendToClient(emitter, key, AlarmListResponseDto.from(alarmList));
                    AlarmListResponseDto alarmListResponseDto = new AlarmListResponseDto(alarmList);
                    sendToClient(emitter, key, alarmListResponseDto);
                }
        );
    }


    // 모음 글 작성자 : 글에 댓글 달렸을 때 알람
    @Transactional
    public void alarmComment(Meeting meeting) {

        User receiver = meeting.getUser();
        String receiverId = String.valueOf(receiver.getId());
        String content = meeting.getTitle()+"에 댓글이 달렸습니다!";

        AlarmList alarmList = new AlarmList(meeting, receiver, content);
        alarmListRepository.saveAndFlush(alarmList);

        alarmProcess(receiverId, alarmList);
    }

    // 모임 글 작성자 : 글에 누군가 참석 버튼을 눌렀을 때 알람
    @Transactional
    public void alarmAttend(Meeting meeting, User user) {

        User receiver = meeting.getUser();
        String receiverId = String.valueOf(receiver.getId());
        String attendant = user.getUsername();
        String content1 = attendant+"이/가 "+meeting.getTitle()+"에 참석 예정입니다!";
        String content2 = meeting.getTitle()+"의 정원이 다 찼습니다!";

        AlarmList alarmList1 = new AlarmList(meeting, receiver, content1);
        alarmListRepository.saveAndFlush(alarmList1);

        alarmProcess(receiverId, alarmList1);

        List<Attendant> attendants = attendantRepository.findAllByMeeting(meeting);
        if (meeting.getMaxNum() <= attendants.size()) {
            AlarmList alarmList2 = new AlarmList(meeting, receiver, content2);
            alarmListRepository.saveAndFlush(alarmList2);

            alarmProcess(receiverId, alarmList2);
        }
    }

    // 모임 글 작성자 : 글에 누군가 (참석)취소 버튼을 눌렀을 때 알람
    @Transactional
    public void alarmCancelAttend(Meeting meeting, User user) {

        User receiver = meeting.getUser();
        String receiverId = String.valueOf(receiver.getId());
        String attendant = user.getUsername();
        String content = attendant+"이/가 "+meeting.getTitle()+"참석을 취소했습니다!";

        AlarmList alarmList = new AlarmList(meeting, receiver, content);
        alarmListRepository.saveAndFlush(alarmList);

        alarmProcess(receiverId, alarmList);
    }

    // 모임 참석자 : 참석하는 모임 글이 수정되었을 때 알람
    @Transactional
    public void alarmUpdateMeeting(Meeting meeting) {

        String content = meeting.getTitle()+"의 내용이 수정되었습니다. 확인해주세요!";

        List<Alarm> alarmReceivers = alarmRepository.findAllByMeeting(meeting);

        for (Alarm alarmReceiver : alarmReceivers) {
            User receiver = alarmReceiver.getUser();
            String receiverId = String.valueOf(receiver.getId());

            AlarmList alarmList = new AlarmList(meeting, receiver, content);
            alarmListRepository.saveAndFlush(alarmList);

            alarmProcess(receiverId, alarmList);

        }
    }

    // 모임 참석자 : 참석하는 모임 글에 링크가 생성/수정되었을 때 알람
    @Transactional
    public void alarmUpdateLink(Meeting meeting) {

        String content = meeting.getTitle()+"의 모임 링크가 생성/수정되었습니다. 확인해주세요!";

        List<Alarm> alarmReceivers = alarmRepository.findAllByMeeting(meeting);

        for (Alarm alarmReceiver : alarmReceivers) {
            User receiver = alarmReceiver.getUser();
            String receiverId = String.valueOf(receiver.getId());

            AlarmList alarmList = new AlarmList(meeting, receiver, content);
            alarmListRepository.saveAndFlush(alarmList);

            alarmProcess(receiverId, alarmList);

        }
    }

    // 모임 참석자 : 참석하는 모임 글이 삭제되었을 때 알람
    @Transactional
    public void alarmDeleteMeeting(Meeting meeting) {

        String content = meeting.getTitle()+"이/가 삭제되었습니다. 다른 모임에 참가해보세요!";

        List<Alarm> alarmReceivers = alarmRepository.findAllByMeeting(meeting);

        for (Alarm alarmReceiver : alarmReceivers) {
            User receiver = alarmReceiver.getUser();
            String receiverId = String.valueOf(receiver.getId());

            AlarmList alarmList = new AlarmList(meeting, receiver, content);
            alarmListRepository.saveAndFlush(alarmList);

            alarmProcess(receiverId, alarmList);

        }

    }

    @Transactional (readOnly = true)
    public AlarmListsResponseDto getAlarms() {
        User user = SecurityUtil.getCurrentUser();
        if (user == null) throw new RestApiException(Code.NOT_FOUND_AUTHORIZATION_IN_SECURITY_CONTEXT);

        AlarmListsResponseDto alarmListsResponseDto = new AlarmListsResponseDto();
        List<AlarmList> alarmLists = alarmListRepository.findAllByUserOrderByCreatedAtDesc(user);
        for(AlarmList alarmList : alarmLists) {
            alarmListsResponseDto.addAlarmList(new AlarmListResponseDto(alarmList));
        }

        return alarmListsResponseDto;
    }

    @Transactional
    public void alarmIsRead(Long id) {
        User user = SecurityUtil.getCurrentUser();
        if (user == null) throw new RestApiException(Code.NOT_FOUND_AUTHORIZATION_IN_SECURITY_CONTEXT);

        AlarmList alarmList = alarmListRepository.findById(id).orElseThrow(() -> new RestApiException(Code.NO_ALARM));
        if (alarmList.isRead()) {
            throw new RestApiException(Code.IS_READ_TRUE);
        } else {
            alarmList.readAlarm();
        }
    }

}
