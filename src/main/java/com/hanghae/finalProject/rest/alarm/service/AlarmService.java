package com.hanghae.finalProject.rest.alarm.service;

import com.hanghae.finalProject.config.errorcode.Code;
import com.hanghae.finalProject.config.exception.RestApiException;
import com.hanghae.finalProject.config.util.SecurityUtil;
import com.hanghae.finalProject.rest.alarm.model.Alarm;
import com.hanghae.finalProject.rest.alarm.model.AlarmList;
import com.hanghae.finalProject.rest.alarm.repository.AlarmListRepository;
import com.hanghae.finalProject.rest.alarm.repository.AlarmRepository;
import com.hanghae.finalProject.rest.attendant.model.Attendant;
import com.hanghae.finalProject.rest.attendant.repository.AttendantRepository;
import com.hanghae.finalProject.rest.meeting.model.Meeting;
import com.hanghae.finalProject.rest.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import java.util.List;
import java.util.Map;

import static com.hanghae.finalProject.rest.alarm.controller.SseController.sseEmitters;

@Service
@RequiredArgsConstructor
public class AlarmService {

    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;
    private static final String ALARM_NAME = "alarm";
    private final EmitterRepository emitterRepository;
    private final AttendantRepository attendantRepository;
    private final AlarmRepository alarmRepository;
    private final AlarmListRepository alarmListRepository;

    public SseEmitter connectAlarm(String token, String lastEventId) {
        SseEmitter sseEmitter = new SseEmitter(DEFAULT_TIMEOUT);

        sseEmitter.onCompletion(() ->);
        sseEmitter.onTimeout(() ->);

        try {
            sseEmitter.send(sseEmitter.event().id("").name(ALARM_NAME).data("connect completed"));
        } catch (Exception e) {
            throw new RestApiException(Code.ALARM_CONNECT_ERROR);
        }

        return sseEmitter;
    }

    // 모음 글 작성자 : 글에 댓글 달렸을 때 알람
    public void alarmComment(Meeting meeting) {

        // 모임 글 작성자 userId
        Long masterId = meeting.getUser().getId();
        String alarmContent = meeting.getTitle()+"에 댓글이 달렸습니다!";

        if (sseEmitters.containsKey(masterId)) {
            SseEmitter sseEmitter = sseEmitters.get(masterId);
            try {
                sseEmitter.send(SseEmitter.event().name("addComment")
                        .data(alarmContent));
                AlarmList alarmList = (new AlarmList(meeting, meeting.getUser(), alarmContent));
                alarmListRepository.saveAndFlush(alarmList);
            } catch (Exception e) {
                sseEmitters.remove(masterId);
            }
        }
    }

    // 모임 글 작성자 : 글에 누군가 참석 버튼을 눌렀을 때 알람
    public void alarmAttend(Meeting meeting, User user) {

        Long masterId = meeting.getUser().getId();
        String attendant = user.getUsername();
        String alarmContent1 = attendant+"이/가 "+meeting.getTitle()+"에 참석 예정입니다!";
        String alarmContent2 = meeting.getTitle()+"의 정원이 다 찼습니다!";

        if (sseEmitters.containsKey(masterId)) {
            SseEmitter sseEmitter = sseEmitters.get(masterId);
            try {
                sseEmitter.send(SseEmitter.event().name("newAttend")
                        .data(alarmContent1));
                AlarmList alarmList1 = (new AlarmList(meeting, meeting.getUser(), alarmContent1));
                alarmListRepository.saveAndFlush(alarmList1);

                List<Attendant> attendants = attendantRepository.findAllByMeetingId(meeting.getId());
                if (meeting.getMaxNum() <= attendants.size()) {
                    sseEmitter.send(SseEmitter.event().name("fullAttend")
                            .data(alarmContent2));
                    AlarmList alarmList2 = (new AlarmList(meeting, meeting.getUser(), alarmContent2));
                    alarmListRepository.saveAndFlush(alarmList2);
                }
            } catch (Exception e) {
                sseEmitters.remove(masterId);
            }
        }
    }

    // 모임 글 작성자 : 글에 누군가 (참석)취소 버튼을 눌렀을 때 알람
    public void alarmCancelAttend(Meeting meeting, User user) {

        Long masterId = meeting.getUser().getId();
        String attendant = user.getUsername();
        String alarmContent = attendant+"이/가 "+meeting.getTitle()+"참석을 취소했습니다!";

        if (sseEmitters.containsKey(masterId)) {
            SseEmitter sseEmitter = sseEmitters.get(masterId);
            try {
                sseEmitter.send(SseEmitter.event().name("cancelAttend")
                        .data(alarmContent));
                AlarmList alarmList = (new AlarmList(meeting, meeting.getUser(), alarmContent));
                alarmListRepository.saveAndFlush(alarmList);
            } catch (Exception e) {
                sseEmitters.remove(masterId);
            }
        }
    }

    // 모임 참석자 : 참석하는 모임 글이 수정되었을 때 알람
    public void alarmUpdateMeeting(Meeting meeting) {

        String alarmContent = meeting.getTitle()+"의 내용이 수정되었습니다. 확인해주세요!";
        List<Alarm> alarmRecivers = alarmRepository.findAllByMeetingId(meeting.getId());

        for (Alarm alarmReciver : alarmRecivers) {
            Long alarmReciverId = alarmReciver.getUser().getId();

            if (sseEmitters.containsKey(alarmReciverId)) {
                SseEmitter sseEmitter = sseEmitters.get(alarmReciverId);
                try {
                    sseEmitter.send(SseEmitter.event().name("updateMeeting")
                            .data(alarmContent));
                    AlarmList alarmList = (new AlarmList(meeting, alarmReciver.getUser(), alarmContent));
                    alarmListRepository.saveAndFlush(alarmList);
                } catch (Exception e) {
                    sseEmitters.remove(alarmReciverId);
                }
            }
        }
    }

    // 모임 참석자 : 참석하는 모임 글에 링크가 생성/수정되었을 때 알람
    public void alarmUpdateLink(Meeting meeting) {

        String alarmContent = meeting.getTitle()+"의 모임 링크가 생성/수정되었습니다. 확인해주세요!";
        List<Alarm> alarmRecivers = alarmRepository.findAllByMeetingId(meeting.getId());

        for (Alarm alarmReciver : alarmRecivers) {
            Long alarmReciverId = alarmReciver.getUser().getId();

            if (sseEmitters.containsKey(alarmReciverId)) {
                SseEmitter sseEmitter = sseEmitters.get(alarmReciverId);
                try {
                    sseEmitter.send(SseEmitter.event().name("updateLink")
                            .data(alarmContent));
                    AlarmList alarmList = (new AlarmList(meeting, alarmReciver.getUser(), alarmContent));
                    alarmListRepository.saveAndFlush(alarmList);
                } catch (Exception e) {
                    sseEmitters.remove(alarmReciverId);
                }
            }

        }
    }

    // 모임 참석자 : 참석하는 모임 글이 삭제되었을 때 알람
    public void alarmDeleteMeeting(Meeting meeting) {

        String alarmContent = meeting.getTitle()+"이/가 삭제되었습니다. 다른 모임에 참가해보세요!";
        List<Alarm> alarmRecivers = alarmRepository.findAllByMeetingId(meeting.getId());

        for (Alarm alarmReciver : alarmRecivers) {
            Long alarmReciverId = alarmReciver.getUser().getId();

            if (sseEmitters.containsKey(alarmReciverId)) {
                SseEmitter sseEmitter = sseEmitters.get(alarmReciverId);
                try {
                    sseEmitter.send(SseEmitter.event().name("deleteMeeting")
                            .data(alarmContent));
                    AlarmList alarmList = (new AlarmList(meeting, alarmReciver.getUser(), alarmContent));
                    alarmListRepository.saveAndFlush(alarmList);
                } catch (Exception e) {
                    sseEmitters.remove(alarmReciverId);
                }
            }
        }
    }

}
