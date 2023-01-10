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
            // 참석하지 않은 유저인 경우 참석으로
            Attendant attendant = attendantRepository.save(new Attendant(meeting, user));
            return new AttendantResponseDto(attendant);
        } else {
            Attendant attendant = attendantRepository.findAttendantByMeetingId(meetingId);
            attendant.cancelAttendant(meeting);
            attendantRepository.delete(attendant);
            return null;
        }
    }

    // 모임 참석자 리스트 조회
    @Transactional(readOnly = true)
    public List<AttendantListResponseDto> getAttendantList(Long meetingId) {
        User user = SecurityUtil.getCurrentUser();
        // 링크공유받은 비회원유저도 요청들어옴. exception 처리 x
        // 팔로우여부도 같이 들고오기
        List<AttendantListResponseDto> attendants = attendantRepository.findByMeetingIdAndFollow(meetingId, user);
        
        return attendants;
    }

    // 모임 입장
    public Code enter(Long meetingId) {
        User user = SecurityUtil.getCurrentUser();
        if (user == null) throw new RestApiException(Code.NOT_FOUND_AUTHORIZATION_IN_SECURITY_CONTEXT);

        Meeting meeting = meetingRepository.findByIdAndDeletedIsFalse(meetingId).orElseThrow(
                () -> new RestApiException(Code.NO_MEETING)
        );

        Attendant attendant = attendantRepository.findByMeetingIdAndUser(meetingId, user);
        attendant.enter(meeting);
        attendantRepository.save(attendant);
        return Code.CREATE_ENTER;
    }

}
