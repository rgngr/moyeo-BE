package com.hanghae.finalProject.rest.alarm.service;

import com.hanghae.finalProject.config.errorcode.Code;
import com.hanghae.finalProject.config.exception.RestApiException;
import com.hanghae.finalProject.config.util.SecurityUtil;
import com.hanghae.finalProject.rest.alarm.dto.AlarmDataResponseDto;
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
        String id = userId + "_" + System.currentTimeMillis();

        // 2
        SseEmitter emitter = emitterRepository.save(id, new SseEmitter(DEFAULT_TIMEOUT));

        emitter.onCompletion(() -> emitterRepository.deleteById(id));
        emitter.onTimeout(() -> emitterRepository.deleteById(id));

        // 3
        // 503 에러를 방지하기 위한 더미 이벤트 전송
        sendToClient(emitter, id, "EventStream Created. [userId=" + userId + "]");

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

    private void sendToClient(SseEmitter emitter, String id, Object data) {
        try {
            emitter.send(SseEmitter.event()
                    .id(id)
                    .name("sse")
                    .data(data));
        } catch (IOException exception) {
            emitterRepository.deleteById(id);
            throw new RuntimeException("연결 오류!");
        }
    }

    private AlarmList createAlarmList(Meeting meeting, User receiver, String content) {
        return AlarmList.builder()
                .meeting(meeting)
                .user(receiver)
                .content(content)
                .build();
    }


    // 모음 글 작성자 : 글에 댓글 달렸을 때 알람
    @Transactional
    public void alarmComment(Meeting meeting) {

        User receiver = meeting.getUser();
        String content = meeting.getTitle()+"에 댓글이 달렸습니다!";

        AlarmList alarmList = createAlarmList(meeting, receiver, content);
        String receiverId = String.valueOf(receiver.getId());
        alarmListRepository.saveAndFlush(alarmList);

        // 로그인 한 유저의 SseEmitter 모두 가져오기
        Map<String, SseEmitter> sseEmitters = emitterRepository.findAllStartWithById(receiverId);
        sseEmitters.forEach(
                (key, emitter) -> {
                    // 데이터 캐시 저장(유실된 데이터 처리하기 위함)
                    emitterRepository.saveEventCache(key, alarmList);
                    // 데이터 전송
                    sendToClient(emitter, key, AlarmDataResponseDto.from(alarmList));
                }
        );

    }

    // 모임 글 작성자 : 글에 누군가 참석 버튼을 눌렀을 때 알람
    @Transactional
    public void alarmAttend(Meeting meeting, User user) {

        User receiver = meeting.getUser();
        String attendant = user.getUsername();
        String content1 = attendant+"이/가 "+meeting.getTitle()+"에 참석 예정입니다!";
        String content2 = meeting.getTitle()+"의 정원이 다 찼습니다!";

        AlarmList alarmList = createAlarmList(meeting, receiver, content1);
        String receiverId = String.valueOf(receiver.getId());
        alarmListRepository.saveAndFlush(alarmList);

        // 로그인 한 유저의 SseEmitter 모두 가져오기
        Map<String, SseEmitter> sseEmitters = emitterRepository.findAllStartWithById(receiverId);
        sseEmitters.forEach(
                (key, emitter) -> {
                    // 데이터 캐시 저장(유실된 데이터 처리하기 위함)
                    emitterRepository.saveEventCache(key, alarmList);
                    // 데이터 전송
                    sendToClient(emitter, key, AlarmDataResponseDto.from(alarmList));
                }
        );

        List<Attendant> attendants = attendantRepository.findAllByMeeting(meeting);
        if (meeting.getMaxNum() <= attendants.size()) {
            AlarmList alarmList2 = createAlarmList(meeting, receiver, content2);
            alarmListRepository.saveAndFlush(alarmList2);

            // 로그인 한 유저의 SseEmitter 모두 가져오기
            Map<String, SseEmitter> sseEmitters2 = emitterRepository.findAllStartWithById(receiverId);
            sseEmitters2.forEach(
                    (key, emitter) -> {
                        // 데이터 캐시 저장(유실된 데이터 처리하기 위함)
                        emitterRepository.saveEventCache(key, alarmList2);
                        // 데이터 전송
                        sendToClient(emitter, key, AlarmDataResponseDto.from(alarmList2));
                    }
            );
        }
    }

    // 모임 글 작성자 : 글에 누군가 (참석)취소 버튼을 눌렀을 때 알람
    @Transactional
    public void alarmCancelAttend(Meeting meeting, User user) {

        User receiver = meeting.getUser();
        String attendant = user.getUsername();
        String content = attendant+"이/가 "+meeting.getTitle()+"참석을 취소했습니다!";

        AlarmList alarmList = createAlarmList(meeting, receiver, content);
        String receiverId = String.valueOf(receiver.getId());
        alarmListRepository.saveAndFlush(alarmList);

        // 로그인 한 유저의 SseEmitter 모두 가져오기
        Map<String, SseEmitter> sseEmitters = emitterRepository.findAllStartWithById(receiverId);
        sseEmitters.forEach(
                (key, emitter) -> {
                    // 데이터 캐시 저장(유실된 데이터 처리하기 위함)
                    emitterRepository.saveEventCache(key, alarmList);
                    // 데이터 전송
                    sendToClient(emitter, key, AlarmDataResponseDto.from(alarmList));
                }
        );
    }

    // 모임 참석자 : 참석하는 모임 글이 수정되었을 때 알람
    @Transactional
    public void alarmUpdateMeeting(Meeting meeting) {

        String content = meeting.getTitle()+"의 내용이 수정되었습니다. 확인해주세요!";
        List<Alarm> alarmReceivers = alarmRepository.findAllByMeeting(meeting);

        for (Alarm alarmReceiver : alarmReceivers) {
            User receiver = alarmReceiver.getUser();
            AlarmList alarmList = createAlarmList(meeting, receiver, content);
            String receiverId = String.valueOf(receiver.getId());
            alarmListRepository.saveAndFlush(alarmList);

            // 로그인 한 유저의 SseEmitter 모두 가져오기
            Map<String, SseEmitter> sseEmitters = emitterRepository.findAllStartWithById(receiverId);
            sseEmitters.forEach(
                    (key, emitter) -> {
                        // 데이터 캐시 저장(유실된 데이터 처리하기 위함)
                        emitterRepository.saveEventCache(key, alarmList);
                        // 데이터 전송
                        sendToClient(emitter, key, AlarmDataResponseDto.from(alarmList));
                    }
            );

        }
    }

    // 모임 참석자 : 참석하는 모임 글에 링크가 생성/수정되었을 때 알람
    @Transactional
    public void alarmUpdateLink(Meeting meeting) {

        String content = meeting.getTitle()+"의 모임 링크가 생성/수정되었습니다. 확인해주세요!";
        List<Alarm> alarmReceivers = alarmRepository.findAllByMeeting(meeting);

        for (Alarm alarmReceiver : alarmReceivers) {
            User receiver = alarmReceiver.getUser();
            AlarmList alarmList = createAlarmList(meeting, receiver, content);
            String receiverId = String.valueOf(receiver.getId());
            alarmListRepository.saveAndFlush(alarmList);

            // 로그인 한 유저의 SseEmitter 모두 가져오기
            Map<String, SseEmitter> sseEmitters = emitterRepository.findAllStartWithById(receiverId);
            sseEmitters.forEach(
                    (key, emitter) -> {
                        // 데이터 캐시 저장(유실된 데이터 처리하기 위함)
                        emitterRepository.saveEventCache(key, alarmList);
                        // 데이터 전송
                        sendToClient(emitter, key, AlarmDataResponseDto.from(alarmList));
                    }
            );

        }
    }

    // 모임 참석자 : 참석하는 모임 글이 삭제되었을 때 알람
    @Transactional
    public void alarmDeleteMeeting(Meeting meeting) {

        String content = meeting.getTitle()+"이/가 삭제되었습니다. 다른 모임에 참가해보세요!";
        List<Alarm> alarmReceivers = alarmRepository.findAllByMeeting(meeting);

        for (Alarm alarmReceiver : alarmReceivers) {
            User receiver = alarmReceiver.getUser();
            AlarmList alarmList = createAlarmList(meeting, receiver, content);
            String receiverId = String.valueOf(receiver.getId());
            alarmListRepository.saveAndFlush(alarmList);

            // 로그인 한 유저의 SseEmitter 모두 가져오기
            Map<String, SseEmitter> sseEmitters = emitterRepository.findAllStartWithById(receiverId);
            sseEmitters.forEach(
                    (key, emitter) -> {
                        // 데이터 캐시 저장(유실된 데이터 처리하기 위함)
                        emitterRepository.saveEventCache(key, alarmList);
                        // 데이터 전송
                        sendToClient(emitter, key, AlarmDataResponseDto.from(alarmList));
                    }
            );

        }

    }

}
