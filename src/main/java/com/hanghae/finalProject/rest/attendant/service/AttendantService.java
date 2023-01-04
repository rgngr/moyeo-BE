package com.hanghae.finalProject.rest.attendant.service;

import com.hanghae.finalProject.config.errorcode.Code;
import com.hanghae.finalProject.config.exception.RestApiException;
import com.hanghae.finalProject.config.util.SecurityUtil;
import com.hanghae.finalProject.rest.attendant.dto.AttendantResponseDto;
import com.hanghae.finalProject.rest.attendant.model.Attendant;
import com.hanghae.finalProject.rest.attendant.repository.AttendantRepository;
import com.hanghae.finalProject.rest.meeting.model.Meeting;
import com.hanghae.finalProject.rest.meeting.repository.MeetingRepository;
import com.hanghae.finalProject.rest.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AttendantService {

    private final AttendantRepository attendantRepository;
    private final MeetingRepository meetingRepository;

    @Transactional
    public AttendantResponseDto addAttendant(Long meetingId){
        User user = SecurityUtil.getCurrentUser();
        if (user == null) throw new RestApiException(Code.NOT_FOUND_AUTHORIZATION_IN_SECURITY_CONTEXT);

        Meeting meeting = meetingRepository.findByIdAndDeletedIsFalse(meetingId).orElseThrow(
                () -> new RestApiException(Code.NO_MEETING)
        );

        if (attendantRepository.findByMeetingIdAndUser(meetingId, user) == null) {
//            Attendant attendant = new Attendant(meeting, user);
//            attendantRepository.save(attendant);
//            return new AttendantResponseDto();
            Attendant attendant = attendantRepository.save(new Attendant(meeting, user));
            return new AttendantResponseDto(attendant);
        } else {
            Attendant attendant = attendantRepository.findAttendantByMeetingId(meetingId);
            attendant.cancelAttendant(meeting);
            attendantRepository.delete(attendant);
            return new AttendantResponseDto(attendant);
        }
    }
}
