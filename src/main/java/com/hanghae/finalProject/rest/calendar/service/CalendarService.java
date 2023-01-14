package com.hanghae.finalProject.rest.calendar.service;

import com.hanghae.finalProject.rest.calendar.dto.MyMeetingResponseDto;
import com.hanghae.finalProject.rest.calendar.repository.CalendarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CalendarService {
     private final CalendarRepository calendarRepository;
     
//     @Cacheable (value = "Calendar", key = "#userId+','+#year+','+#month")
     @Transactional (readOnly = true)
     public MyMeetingResponseDto getMyMeetings( Long userId, Integer year, Integer month) {
          MyMeetingResponseDto response = new MyMeetingResponseDto();
          List<MyMeetingResponseDto.ResponseDto> responseDtoList = calendarRepository.findAllByUserIdAndMonth(userId, year, month);
          return response.addMeetingList(responseDtoList);
     }
}
