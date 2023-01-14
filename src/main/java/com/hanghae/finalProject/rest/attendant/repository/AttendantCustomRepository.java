package com.hanghae.finalProject.rest.attendant.repository;

import com.hanghae.finalProject.rest.attendant.dto.AttendantListResponseDto;
import com.hanghae.finalProject.rest.user.model.User;

import java.util.List;

public interface AttendantCustomRepository {
     List<AttendantListResponseDto> findByMeetingIdAndFollow(Long meetingId, User user);
     
}
