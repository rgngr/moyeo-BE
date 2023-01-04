package com.hanghae.finalProject.rest.attendant.service;

import com.hanghae.finalProject.config.errorcode.Code;
import com.hanghae.finalProject.config.exception.RestApiException;
import com.hanghae.finalProject.config.util.SecurityUtil;
import com.hanghae.finalProject.rest.attendant.dto.AttendantResponseDto;
import com.hanghae.finalProject.rest.attendant.dto.AttendantListResponseDto;
import com.hanghae.finalProject.rest.attendant.model.Attendant;
import com.hanghae.finalProject.rest.attendant.repository.AttendantRepository;
import com.hanghae.finalProject.rest.meeting.model.Meeting;
import com.hanghae.finalProject.rest.meeting.repository.MeetingRepository;
import com.hanghae.finalProject.rest.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AttendantService {

    private final AttendantRepository attendantRepository;
    private final MeetingRepository meetingRepository;

    // 모임 참석/취소
    @Transactional
    public AttendantResponseDto addAttendant(Long meetingId){
        User user = SecurityUtil.getCurrentUser();
        if (user == null) throw new RestApiException(Code.NOT_FOUND_AUTHORIZATION_IN_SECURITY_CONTEXT);

        Meeting meeting = meetingRepository.findByIdAndDeletedIsFalse(meetingId).orElseThrow(
                () -> new RestApiException(Code.NO_MEETING)
        );

        if (attendantRepository.findByMeetingIdAndUser(meetingId, user) == null) {
            Attendant attendant = attendantRepository.save(new Attendant(meeting, user));
            return new AttendantResponseDto(attendant);
        } else {
            Attendant attendant = attendantRepository.findAttendantByMeetingId(meetingId);
            attendant.cancelAttendant(meeting);
            attendantRepository.delete(attendant);
            return new AttendantResponseDto(attendant);
        }
    }

    // 모임 참석자 리스트 조회
    @Transactional(readOnly = true)
    public List<AttendantListResponseDto> getAttendantList(Long meetingId) {
        List<AttendantListResponseDto> attendantList = new ArrayList<>();
        List<Attendant> attendants = attendantRepository.findByMeetingId(meetingId);
        for (Attendant attendant : attendants) {
            attendantList.add(new AttendantListResponseDto(attendant));
        }
        return attendantList;
    }
}
