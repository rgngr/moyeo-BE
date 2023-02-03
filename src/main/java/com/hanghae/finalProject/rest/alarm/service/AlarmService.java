package com.hanghae.finalProject.rest.alarm.service;

import com.hanghae.finalProject.config.dto.ResponseDto;
import com.hanghae.finalProject.config.errorcode.Code;
import com.hanghae.finalProject.config.exception.RestApiException;
import com.hanghae.finalProject.config.util.SecurityUtil;
import com.hanghae.finalProject.rest.alarm.dto.AlarmListResponseDto;
import com.hanghae.finalProject.rest.alarm.dto.AlarmCountResponseDto;
import com.hanghae.finalProject.rest.alarm.model.Alarm;
import com.hanghae.finalProject.rest.alarm.model.AlarmList;
import com.hanghae.finalProject.rest.alarm.repository.AlarmListRepository;
import com.hanghae.finalProject.rest.alarm.repository.AlarmRepository;
import com.hanghae.finalProject.rest.alarm.repository.EmitterRepository;
import com.hanghae.finalProject.rest.attendant.model.Attendant;
import com.hanghae.finalProject.rest.attendant.repository.AttendantRepository;
import com.hanghae.finalProject.rest.follow.model.Follow;
import com.hanghae.finalProject.rest.follow.repository.FollowRepository;
import com.hanghae.finalProject.rest.meeting.model.Meeting;
import com.hanghae.finalProject.rest.meeting.repository.MeetingRepository;
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

    private static final Long DEFAULT_TIMEOUT = 60L * 60 * 1000;
    private final EmitterRepository emitterRepository;
    private final MeetingRepository meetingRepository;
    private final AttendantRepository attendantRepository;
    private final AlarmRepository alarmRepository;
    private final AlarmListRepository alarmListRepository;
    private final FollowRepository followRepository;


    // 알림 구독
    public SseEmitter subscribe(Long id, String lastEventId) {
//        // 유저 정보 들고오기
//        User user = SecurityUtil.getCurrentUser();
//        if (user == null) throw new RestApiException(Code.NOT_FOUND_AUTHORIZATION_IN_SECURITY_CONTEXT);
//        Long userId = user.getId();

        //userId + 현재시간 >> 마지막 받은 알림 이후의 새로운 알림을 전달하기 위해 필요
        String eventId = id + "_" + System.currentTimeMillis();

        // 유효시간 포함한 SseEmitter 객체 생성
        // eventId를 key로, SseEmitter를 value로 저장
        SseEmitter emitter = emitterRepository.save(eventId, new SseEmitter(DEFAULT_TIMEOUT));

        // sse 연결 뒤 데이터가 하나도 전송되지 않고 유효시간이 끝나면 503에러 발생
        // 503 에러를 방지하기 위한 더미 이벤트 전송
        sendToClient(emitter, eventId, "Alarm Connected [receiverId=" + id + "]");

        // 클라이언트가 미수신한 Event 목록이 존재할 경우 전송
        if (!lastEventId.isEmpty()) {
            Map<String, Object> events = emitterRepository.findAllEventCacheStartWithId(String.valueOf(id));
            events.entrySet().stream()
                    .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
                    .forEach(entry -> sendToClient(emitter, entry.getKey(), entry.getValue()));
        }

        //비동기 요청이 정상 동작 되지 않는다면 저장한 SseEmitter를 삭제
        emitter.onCompletion(() -> emitterRepository.deleteById(eventId));
        emitter.onTimeout(() -> emitterRepository.deleteById(eventId));

        return emitter;
    }

    // 클라리언트에게 이벤트 내용 전송
    private void sendToClient(SseEmitter emitter, String eventId, Object data) {
        try {
            emitter.send(SseEmitter.event()
                    .id(eventId) // event id
                    .name("sse") // event 이름
                    .data(data)); // event 객체, content 포함
        } catch (IOException exception) {
            emitterRepository.deleteById(eventId); // error 발생시 event 삭제
            throw new RuntimeException("sse error!!");
        }
    }

    // 공통된 이벤트 전송 과정
    private void alarmProcess(String receiverId, AlarmList alarmList) {
        // 로그인 한 유저의 SseEmitter 모두 가져오기
        Map<String, SseEmitter> sseEmitters = emitterRepository.findAllStartWithById(receiverId);
        sseEmitters.forEach(
                (key, emitter) -> {
                    // event 저장
                    emitterRepository.saveEventCache(key, alarmList);
                    // data 전송
                    AlarmListResponseDto.Alarm1 alarmData = new AlarmListResponseDto.Alarm1(alarmList);
                    sendToClient(emitter, key, alarmData);
                }
        );
    }

    // 댓글 달렸을 때 글 작성자에게 알림
    @Transactional
    public void alarmComment(Meeting meeting) {

        User receiver = meeting.getUser();
        String receiverId = String.valueOf(receiver.getId());
        String content = "["+meeting.getTitle()+"] 모임에 댓글이 달렸습니다.";
        String url = "https://moyeo.vercel.app/detail/"+meeting.getId();

        //알림 내용 생성
        AlarmList alarmList = new AlarmList(meeting, receiver, content, url);
        alarmListRepository.save(alarmList);

        alarmProcess(receiverId, alarmList);
    }

    // 참석 버튼을 눌렀을 때 / 정원 다 찼을 때 글 작성자에게 알림
    @Transactional
    public void alarmAttend(Meeting meeting, User user) {

        User receiver = meeting.getUser();
        String receiverId = String.valueOf(receiver.getId());
        String attendant = user.getUsername();
        String content1 = attendant+" 님이 ["+meeting.getTitle()+"] 모임에 참석 예정입니다.";
        String content2 = "["+meeting.getTitle()+"] 모임의 정원이 다 찼습니다.";
        String url = "https://moyeo.vercel.app/detail/"+meeting.getId();

        //알림 내용 생성
        AlarmList alarmList1 = new AlarmList(meeting, receiver, content1, url);
        alarmListRepository.saveAndFlush(alarmList1);

        alarmProcess(receiverId, alarmList1);

        // 정원 초과 시
        List<Attendant> attendants = attendantRepository.findAllByMeeting(meeting);
        if (meeting.getMaxNum() <= attendants.size()) {
            //알림 내용 생성
            AlarmList alarmList2 = new AlarmList(meeting, receiver, content2, url);
            alarmListRepository.save(alarmList2);

            alarmProcess(receiverId, alarmList2);
        }
    }

    // 취소 버튼을 눌렀을 때 글 작성자에게 알림
    @Transactional
    public void alarmCancelAttend(Meeting meeting, User user) {

        User receiver = meeting.getUser();
        String receiverId = String.valueOf(receiver.getId());
        String attendant = user.getUsername();
        String content = attendant+" 님이 ["+meeting.getTitle()+"] 모임 참석을 취소했습니다.";

        //알림 내용 생성
        AlarmList alarmList = new AlarmList(meeting, receiver, content, null);
        alarmListRepository.save(alarmList);

        alarmProcess(receiverId, alarmList);
    }

    // 팔로잉하는 사람이 모임 글 작성했을 때 알림
    @Transactional
    public void alarmFollowers(Meeting meeting, User user) {
        // 글 작성자의 팔로워 전체 가져오기
        List<Follow> followers = followRepository.findByFollow(user);
        if (followers.isEmpty()) {
            return;
        }

        String master = user.getUsername();
        String content = master+" 님이 ["+meeting.getTitle()+"] 모임을 생성했습니다.";
        String url = "https://moyeo.vercel.app/detail/"+meeting.getId();

        // 팔로워에게 알림 보내기
        for (Follow follower : followers) {
            User receiver = follower.getUser();
            String receiverId = String.valueOf(receiver.getId());

            //알림 내용 생성
            AlarmList alarmList = new AlarmList(meeting, receiver, content, url);
            alarmListRepository.saveAndFlush(alarmList);

            alarmProcess(receiverId, alarmList);

        }
    }

    // 모임 글이 수정되었을 때 참서자에게 알림
    @Transactional
    public void alarmUpdateMeeting(Meeting meeting) {

        String content = "["+meeting.getTitle()+"] 모임의 내용이 수정되었습니다.";
        String url = "https://moyeo.vercel.app/detail/"+meeting.getId();

        // 알림 수신 여부 확인
        List<Alarm> alarmReceivers = alarmRepository.findAllByMeeting(meeting);
        if (alarmReceivers.isEmpty()) {
            return;
        }

        // 알림 보내기
        for (Alarm alarmReceiver : alarmReceivers) {
            User receiver = alarmReceiver.getUser();
            String receiverId = String.valueOf(receiver.getId());

            //알림 내용 생성
            AlarmList alarmList = new AlarmList(meeting, receiver, content, url);
            alarmListRepository.saveAndFlush(alarmList);

            alarmProcess(receiverId, alarmList);

        }
    }

    // 링크가 생성/수정되었을 때 참석자에게 알림
    @Transactional
    public void alarmUpdateLink(Meeting meeting) {

        String content = "["+meeting.getTitle()+"] 모임의 링크가 생성/수정 되었습니다.";
        String url = "https://moyeo.vercel.app/detail/"+meeting.getId();

        // 알림 수신 여부 확인
        List<Alarm> alarmReceivers = alarmRepository.findAllByMeeting(meeting);
        if (alarmReceivers.isEmpty()) {
            return;
        }

        // 알림 보내기
        for (Alarm alarmReceiver : alarmReceivers) {
            User receiver = alarmReceiver.getUser();
            String receiverId = String.valueOf(receiver.getId());

            //알림 내용 생성
            AlarmList alarmList = new AlarmList(meeting, receiver, content, url);
            alarmListRepository.saveAndFlush(alarmList);

            alarmProcess(receiverId, alarmList);

        }
    }

    // 모임 글이 삭제되었을 때 참석자에게 알림
    @Transactional
    public void alarmDeleteMeeting(Meeting meeting) {

        String content = "["+meeting.getTitle()+"] 모임이 삭제되었습니다.";

        // 알림 수신 여부 확인
        List<Alarm> alarmReceivers = alarmRepository.findAllByMeeting(meeting);
        if (alarmReceivers.isEmpty()) {
            return;
        }

        // 알림 보내기
        for (Alarm alarmReceiver : alarmReceivers) {
            User receiver = alarmReceiver.getUser();
            String receiverId = String.valueOf(receiver.getId());

            //알림 내용 생성
            AlarmList alarmList = new AlarmList(meeting, receiver, content, null);
            alarmListRepository.saveAndFlush(alarmList);

            alarmProcess(receiverId, alarmList);

        }

    }

//    @Scheduled(cron = "0 1/10 * * * *")
    public void searchMeetings() {

        LocalDate today = LocalDate.now(); // 오늘 날짜
        LocalTime now = LocalTime.now(); // 지금 시간

        int min = (now.getMinute()/10)*10; // 10분 단위 맞추기 위해
        // 지금 시간에서 30분 뒤
        LocalTime nowAfter30 =  LocalTime.of(now.getHour(), min,0).plusMinutes(30);

        // 30분 후 시작하는 모임 리스트
        List<Meeting> meetings = meetingRepository.findAllByStartDateAndStartTime(today, nowAfter30);
        if (meetings.isEmpty()) {
            return;
        }

        // 각 모임 알림 보내기
        for (Meeting meeting : meetings) {
            alarmBefore30(meeting);
        }

    }

    @Transactional
    public void alarmBefore30(Meeting meeting) {

        String content1 = "["+meeting.getTitle()+"] 모임 시작 30분 전입니다. 모임 링크를 올려주세요.";
        String content2 = "["+meeting.getTitle()+"] 모임 시작 30분 전입니다. 모임 링크를 확인해주세요.";
        String url = "https://moyeo.vercel.app/detail/"+meeting.getId();

        // 모임 글 작성자
        User receiver1 = meeting.getUser();
        String receiver1Id = String.valueOf(receiver1.getId());

        //알림 내용 생성
        AlarmList alarmList1 = new AlarmList(meeting, receiver1, content1, url);
        alarmListRepository.saveAndFlush(alarmList1);

        alarmProcess(receiver1Id, alarmList1);

        //알림 수신 여부 확인
        List<Alarm> alarmReceivers = alarmRepository.findAllByMeeting(meeting);
        if (alarmReceivers.isEmpty()) {
            return;
        }

        //알림 보내기
        for (Alarm alarmReceiver : alarmReceivers) {
            User receiver2 = alarmReceiver.getUser();
            String receiver2Id = String.valueOf(receiver2.getId());

            //알림 내용 생성
            AlarmList alarmList2 = new AlarmList(meeting, receiver2, content2, url);
            alarmListRepository.saveAndFlush(alarmList2);

            alarmProcess(receiver2Id, alarmList2);

        }

    }

    @Transactional(readOnly = true)
    public AlarmCountResponseDto alarmCount() {
        // 유저 정보
        User user = SecurityUtil.getCurrentUser();
        if (user == null) throw new RestApiException(Code.NOT_FOUND_AUTHORIZATION_IN_SECURITY_CONTEXT);

        // 알림 전부 가져오기
        List<AlarmList> alarmCount = alarmListRepository.findAllByUser(user);

        // 알림 개수
        if (alarmCount.isEmpty()) {
            return new AlarmCountResponseDto(0);
        } else {
            return new AlarmCountResponseDto(alarmCount.size());
        }
    }

    @Transactional(readOnly = true)
    public ResponseDto isExistAlarms() {
        // 유저 정보
        User user = SecurityUtil.getCurrentUser();
        if (user == null) throw new RestApiException(Code.NOT_FOUND_AUTHORIZATION_IN_SECURITY_CONTEXT);

        boolean isExistAlarms = alarmListRepository.existsByUser(user);

        return ResponseDto.of(isExistAlarms, Code.IS_EXIST_ALARMS);
    }

    // GET 알림 리스트
    @Transactional (readOnly = true)
    public AlarmListResponseDto getAlarms() {
        // 유저 정보
        User user = SecurityUtil.getCurrentUser();
        if (user == null) throw new RestApiException(Code.NOT_FOUND_AUTHORIZATION_IN_SECURITY_CONTEXT);

        // 알림 전부 가져오기
        List<AlarmList> alarms = alarmListRepository.findAllByUserOrderByCreatedAtDesc(user);
        if (alarms.isEmpty()) {
            return null;
        }

        AlarmListResponseDto alarmListResponseDto = new AlarmListResponseDto();
        for(AlarmList alarm : alarms) {
            alarmListResponseDto.addAlarm(new AlarmListResponseDto.Alarm1(alarm));
        }

        return alarmListResponseDto;
    }

    // 알림 삭제(읽음) 처리
    @Transactional
    public void deleteAlarm(Long id) {
        // 유저 정보
        User user = SecurityUtil.getCurrentUser();
        if (user == null) throw new RestApiException(Code.NOT_FOUND_AUTHORIZATION_IN_SECURITY_CONTEXT);

        // 알림 존재 여부 확인
        AlarmList alarmList = alarmListRepository.findById(id).orElseThrow(() -> new RestApiException(Code.NO_ALARM));
        // 알림 삭제 (읽음)
        alarmListRepository.delete(alarmList);
    }

    // 알림 전체 삭제
    @Transactional
    public void deleteAlarms() {
        // 유저 정보
        User user = SecurityUtil.getCurrentUser();
        if (user == null) throw new RestApiException(Code.NOT_FOUND_AUTHORIZATION_IN_SECURITY_CONTEXT);

        alarmListRepository.deleteAllByUser(user);
    }
}
