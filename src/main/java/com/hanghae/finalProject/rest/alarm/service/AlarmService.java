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
import com.hanghae.finalProject.rest.meeting.repository.MeetingRepository;
import com.hanghae.finalProject.rest.review.repository.ReviewRepository;
import com.hanghae.finalProject.rest.user.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlarmService {
    private final MeetingRepository meetingRepository;
    private final ReviewRepository reviewRepository;

    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;
    private final EmitterRepository emitterRepository;
    private final AttendantRepository attendantRepository;
    private final AlarmRepository alarmRepository;
    private final AlarmListRepository alarmListRepository;

    // 알람 연결
    public SseEmitter subscribe(Long id) {
//        // 유저 정보 들고오기
//        User user = SecurityUtil.getCurrentUser();
//        if (user == null) throw new RestApiException(Code.NOT_FOUND_AUTHORIZATION_IN_SECURITY_CONTEXT);

//        Long userId = user.getId();
        //userId + 현재시간 >> 마지막 받은 알람 이후의 새로운 알람을 전달하기 위해 필요
        String receiverId = String.valueOf(id);

        // 유효시간 포함한 SseEmitter 객체 생성
        // receiverId를 key로, SseEmitter를 value로 저장
        SseEmitter emitter = emitterRepository.save(receiverId, new SseEmitter(DEFAULT_TIMEOUT));

        //비동기 요청이 정상 동작 되지 않는다면 저장한 SseEmitter를 삭제
        emitter.onCompletion(() -> emitterRepository.deleteById(receiverId));
        emitter.onTimeout(() -> emitterRepository.deleteById(receiverId));

        // sse 연결 뒤 데이터가 하나도 전송되지 않고 유효시간이 끝나면 503에러 발생
        // 503 에러를 방지하기 위한 더미 이벤트 전송
        sendToClient(emitter, receiverId, "Alarm Connected!! [receiverId=" + id + "]");

        return emitter;
    }

    // 클라리언트에게 이벤트 내용 전송
    private void sendToClient(SseEmitter emitter, String receiverId, Object data) {
        try {
            emitter.send(SseEmitter.event()
                    .id(receiverId) //이벤트 아이디
                    .name("sse") //이벤트 이름
                    .data(data)); //이벤트 객체, content 포함
        } catch (IOException exception) {
            emitterRepository.deleteById(receiverId); //오류발생시 id값 삭제
            throw new RuntimeException("연결 오류!");
        }
    }

    // 공통된 이벤트 전송 과정 메소드로 추상
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
        String content = "'"+meeting.getTitle()+"' 모임에 댓글이 달렸습니다!";

        //알람 리스트 생성
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
        String content1 = "'"+attendant+"'이/가 '"+meeting.getTitle()+"' 모임에 참석 예정입니다!";
        String content2 = "'"+meeting.getTitle()+"' 모임의 정원이 다 찼습니다!";

        //알람 리스트 생성
        AlarmList alarmList1 = new AlarmList(meeting, receiver, content1);
        alarmListRepository.saveAndFlush(alarmList1);

        alarmProcess(receiverId, alarmList1);

        // 정원 초과 시
        List<Attendant> attendants = attendantRepository.findAllByMeeting(meeting);
        if (meeting.getMaxNum() <= attendants.size()) {
            //알람 리스트 생성
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
        String content = "'"+attendant+"'이/가 '"+meeting.getTitle()+"' 모임 참석을 취소했습니다!";

        //알람 리스트 생성
        AlarmList alarmList = new AlarmList(meeting, receiver, content);
        alarmListRepository.saveAndFlush(alarmList);

        alarmProcess(receiverId, alarmList);
    }

    // 모임 참석자 : 참석하는 모임 글이 수정되었을 때 알람
    @Transactional
    public void alarmUpdateMeeting(Meeting meeting) {

        String content = "'"+meeting.getTitle()+"' 모임의 내용이 수정되었습니다. 확인해주세요!";

        //알람 수신 여부 확인
        List<Alarm> alarmReceivers = alarmRepository.findAllByMeeting(meeting);

        //해당 모임 글 참석자 중 알람 수신한 사람 모두에게 알람
        for (Alarm alarmReceiver : alarmReceivers) {
            User receiver = alarmReceiver.getUser();
            String receiverId = String.valueOf(receiver.getId());

            //알람 리스트 생성
            AlarmList alarmList = new AlarmList(meeting, receiver, content);
            alarmListRepository.saveAndFlush(alarmList);

            alarmProcess(receiverId, alarmList);

        }
    }

    // 모임 참석자 : 참석하는 모임 글에 링크가 생성/수정되었을 때 알람
    @Transactional
    public void alarmUpdateLink(Meeting meeting) {

        String content = "'"+meeting.getTitle()+"' 모임의 링크가 생성/수정되었습니다. 확인해주세요!";

        //알림 수신 여부 확인
        List<Alarm> alarmReceivers = alarmRepository.findAllByMeeting(meeting);

        //해당 모임 글 참석자 중 알람 수신한 사람 모두에게 알람
        for (Alarm alarmReceiver : alarmReceivers) {
            User receiver = alarmReceiver.getUser();
            String receiverId = String.valueOf(receiver.getId());

            //알람 리스트 생성
            AlarmList alarmList = new AlarmList(meeting, receiver, content);
            alarmListRepository.saveAndFlush(alarmList);

            alarmProcess(receiverId, alarmList);

        }
    }

    // 모임 참석자 : 참석하는 모임 글이 삭제되었을 때 알람
    @Transactional
    public void alarmDeleteMeeting(Meeting meeting) {

        String content = "'"+meeting.getTitle()+"' 모임이 삭제되었습니다. 다른 모임에 참가해보세요!";

        //알람 수신 여부 확인
        List<Alarm> alarmReceivers = alarmRepository.findAllByMeeting(meeting);

        //해당 모임 글 참석자 중 알람 수신한 사람 모두에게 알람
        for (Alarm alarmReceiver : alarmReceivers) {
            User receiver = alarmReceiver.getUser();
            String receiverId = String.valueOf(receiver.getId());

            //알람 리스트 생성
            AlarmList alarmList = new AlarmList(meeting, receiver, content);
            alarmListRepository.saveAndFlush(alarmList);

            alarmProcess(receiverId, alarmList);

        }

    }

    @Transactional
    public void alarmBefore30(Meeting meeting) {
        String content1 = "'"+meeting.getTitle()+"' 모임 시작 30분 전입니다! 모임 링크를 올려주세요!";
        String content2 = "'"+meeting.getTitle()+"' 모임 시작 30분 전입니다! 모임 링크를 확인해주세요!";


        // 모임 글 작성자
        User receiver1 = meeting.getUser();
        String receiver1Id = String.valueOf(receiver1.getId());

        //알람 리스트 생성
        AlarmList alarmList1 = new AlarmList(meeting, receiver1, content1);
        alarmListRepository.saveAndFlush(alarmList1);

        alarmProcess(receiver1Id, alarmList1);

        //알림 수신 여부 확인
        List<Alarm> alarmReceivers = alarmRepository.findAllByMeeting(meeting);

        //해당 모임 글 참석자 중 알람 수신한 사람 모두에게 알람
        for (Alarm alarmReceiver : alarmReceivers) {
            User receiver2 = alarmReceiver.getUser();
            String receiver2Id = String.valueOf(receiver2.getId());

            //알람 리스트 생성
            AlarmList alarmList2 = new AlarmList(meeting, receiver2, content2);
            alarmListRepository.saveAndFlush(alarmList2);

            alarmProcess(receiver2Id, alarmList2);

        }

    }

    // GET 알람 리스트
    @Transactional (readOnly = true)
    public AlarmListsResponseDto getAlarms() {
        // 유저 정보
        User user = SecurityUtil.getCurrentUser();
        if (user == null) throw new RestApiException(Code.NOT_FOUND_AUTHORIZATION_IN_SECURITY_CONTEXT);

        // 알람 리스트 생성
        AlarmListsResponseDto alarmListsResponseDto = new AlarmListsResponseDto();
        List<AlarmList> alarmLists = alarmListRepository.findAllByUserOrderByCreatedAtDesc(user);
        for(AlarmList alarmList : alarmLists) {
            alarmListsResponseDto.addAlarmList(new AlarmListResponseDto(alarmList));
        }

        return alarmListsResponseDto;
    }

    // 알람 삭제(읽음) 처리
    @Transactional
    public void deleteAlarm(Long id) {
        // 유저 정보
        User user = SecurityUtil.getCurrentUser();
        if (user == null) throw new RestApiException(Code.NOT_FOUND_AUTHORIZATION_IN_SECURITY_CONTEXT);

        // 알람 존재 여부 확인
        AlarmList alarmList = alarmListRepository.findById(id).orElseThrow(() -> new RestApiException(Code.NO_ALARM));
        // 알람 읽음 여부에 따른 처리
        alarmListRepository.delete(alarmList);
    }


//    @Scheduled(fixedRate = 60 * 1000)
    public void searchTodayMeetings() {

        LocalDate today = LocalDate.now();

        List<Meeting> todayMeetings = meetingRepository.findAllByStartDate(today);

        if (todayMeetings.isEmpty()) {
            return;
        }

        for (Meeting todayMeeting : todayMeetings) {

            LocalTime meetingStartTime = todayMeeting.getStartTime();
            LocalTime now = LocalTime.now();

            LocalTime nowAfter29 = now.plusMinutes(29);
            LocalTime nowAfter31 = now.plusMinutes(31);

            boolean isAfter29 = meetingStartTime.isAfter(nowAfter29);
            boolean isBefore31 = meetingStartTime.isBefore(nowAfter31);

            if (isAfter29 && isBefore31) {
                alarmBefore30(todayMeeting);
            }

        }

    }

}
