package com.hanghae.finalProject.rest.calendar.repository;

import com.hanghae.finalProject.rest.meeting.dto.MyMeetingResponseDto;

import java.util.List;

public interface CalendarCustomRepository {
     // 나의 모임 (달별)
     List<MyMeetingResponseDto.ResponseDto> findAllByUserIdAndMonth(Long id, int year, int month);
}
