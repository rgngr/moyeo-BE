package com.hanghae.finalProject.rest.alarm.service;

import com.hanghae.finalProject.rest.alarm.repository.AlarmRepository;
import com.hanghae.finalProject.rest.attendant.model.Attendant;
import com.hanghae.finalProject.rest.attendant.repository.AttendantRepository;
import com.hanghae.finalProject.rest.meeting.model.Meeting;
import com.hanghae.finalProject.rest.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import java.util.List;
import static com.hanghae.finalProject.rest.alarm.controller.SseController.sseEmitters;

@Service
@RequiredArgsConstructor
public class AlarmService {

    private final AttendantRepository attendantRepository;
    private final AlarmRepository alarmRepository;

    public void alarmComment(Meeting meeting) {

        Long masterId = meeting.getUser().getId();

        if (sseEmitters.containsKey(masterId)) {
            SseEmitter sseEmitter = sseEmitters.get(masterId);
            try {
                sseEmitter.send(SseEmitter.event().name("addComment")
                        .data(meeting.getTitle()+"에 댓글이 달렸습니다!"));
            } catch (Exception e) {
                sseEmitters.remove(masterId);
            }
        }
    }

    public void alarmAttend(Meeting meeting, User user) {

        Long masterId = meeting.getUser().getId();
        String attendant = user.getUsername();

        if (sseEmitters.containsKey(masterId)) {
            SseEmitter sseEmitter = sseEmitters.get(masterId);
            try {
                sseEmitter.send(SseEmitter.event().name("newAttend")
                        .data(attendant+"이/가 "+meeting.getTitle()+"에 참석 예정입니다!"));

                List<Attendant> attendants = attendantRepository.findAllByMeetingId(meeting.getId());
                if (meeting.getMaxNum() <= attendants.size()) {
                    sseEmitter.send(SseEmitter.event().name("fullAttend")
                            .data(meeting.getTitle()+"의 정원이 다 찼습니다!"));
                }
            } catch (Exception e) {
                sseEmitters.remove(masterId);
            }
        }
    }

    public void alarmCancelAttend(Meeting meeting, User user) {

        Long masterId = meeting.getUser().getId();
        String attendant = user.getUsername();

        if (sseEmitters.containsKey(masterId)) {
            SseEmitter sseEmitter = sseEmitters.get(masterId);
            try {
                sseEmitter.send(SseEmitter.event().name("cancelAttend")
                        .data(attendant+"이/가 "+meeting.getTitle()+"참석을 취소했습니다!"));
            } catch (Exception e) {
                sseEmitters.remove(masterId);
            }
        }
    }

    public void alarmUpdateMeeting(Meeting meeting) {

        List<Attendant> attendants = attendantRepository.findAllByMeetingId(meeting.getId());

        for (Attendant attendant : attendants) {
            Long attendantId = attendant.getUser().getId();

            boolean isAlarm = alarmRepository.existsByMeetingIdAndUserId(meeting.getId(), attendantId);
            if (isAlarm) {
                if (sseEmitters.containsKey(attendantId)) {
                    SseEmitter sseEmitter = sseEmitters.get(attendantId);
                    try {
                        sseEmitter.send(SseEmitter.event().name("updateMeeting")
                                .data(meeting.getTitle()+"의 내용이 수정되었습니다. 확인해주세요!"));
                    } catch (Exception e) {
                        sseEmitters.remove(attendantId);
                    }
                }
            }
        }
    }

    public void alarmUpdateLink(Meeting meeting) {

        List<Attendant> attendants = attendantRepository.findAllByMeetingId(meeting.getId());

        for (Attendant attendant : attendants) {
            Long attendantId = attendant.getUser().getId();

            boolean isAlarm = alarmRepository.existsByMeetingIdAndUserId(meeting.getId(), attendantId);
            if (isAlarm) {
                if (sseEmitters.containsKey(attendantId)) {
                    SseEmitter sseEmitter = sseEmitters.get(attendantId);
                    try {
                        sseEmitter.send(SseEmitter.event().name("updateLink")
                                .data(meeting.getTitle()+"의 모임 링크가 생성/수정되었습니다. 확인해주세요!"));
                    } catch (Exception e) {
                        sseEmitters.remove(attendantId);
                    }
                }
            }
        }
    }

    public void alarmDeleteMeeting(Meeting meeting) {

        List<Attendant> attendants = attendantRepository.findAllByMeetingId(meeting.getId());

        for (Attendant attendant : attendants) {
            Long attendantId = attendant.getUser().getId();

            boolean isAlarm = alarmRepository.existsByMeetingIdAndUserId(meeting.getId(), attendantId);
            if (isAlarm) {
                if (sseEmitters.containsKey(attendantId)) {
                    SseEmitter sseEmitter = sseEmitters.get(attendantId);
                    try {
                        sseEmitter.send(SseEmitter.event().name("deleteMeeting")
                                .data(meeting.getTitle()+"이/가 삭제되었습니다. 다른 모임에 참가해보세요!"));
                    } catch (Exception e) {
                        sseEmitters.remove(attendantId);
                    }
                }
            }
        }
    }

}
