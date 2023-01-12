package com.hanghae.finalProject.rest.calendar.service;

import com.hanghae.finalProject.config.errorcode.Code;
import com.hanghae.finalProject.config.exception.RestApiException;
import com.hanghae.finalProject.config.util.SecurityUtil;
import com.hanghae.finalProject.rest.calendar.repository.CalendarRepository;
import com.hanghae.finalProject.rest.calendar.dto.MyMeetingResponseDto;
import com.hanghae.finalProject.rest.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CalendarService {
     
     private final CalendarRepository calendarRepository;
     
     @Transactional (readOnly = true)
     public MyMeetingResponseDto getMyMeetings(Integer year, Integer month) {
          year = (year == null || year == 0) ? LocalDateTime.now().getYear() : year;
          month = (month == null || month == 0) ? LocalDateTime.now().getMonthValue() : month;
          
          User user = SecurityUtil.getCurrentUser(); // 비회원일경우(토큰못받았을경우) null
          if (user == null) throw new RestApiException(Code.NOT_FOUND_AUTHORIZATION_IN_SECURITY_CONTEXT);
          
          MyMeetingResponseDto response = new MyMeetingResponseDto();
          List<MyMeetingResponseDto.ResponseDto> responseDtoList = calendarRepository.findAllByUserIdAndMonth(user.getId(), year, month);
          return response.addMeetingList(responseDtoList);
     }
}
