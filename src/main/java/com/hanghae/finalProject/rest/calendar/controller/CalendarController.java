package com.hanghae.finalProject.rest.calendar.controller;

import com.hanghae.finalProject.config.dto.DataResponseDto;
import com.hanghae.finalProject.config.dto.ResponseDto;
import com.hanghae.finalProject.config.errorcode.Code;
import com.hanghae.finalProject.config.exception.RestApiException;
import com.hanghae.finalProject.config.security.UserDetailsImpl;
import com.hanghae.finalProject.rest.calendar.service.CalendarService;
import com.hanghae.finalProject.rest.user.model.User;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping ("/api/meetings")
public class CalendarController {
     private final CalendarService calendarService;
     
     
     @ApiOperation (value = "나의 월별 캘린더 불러오기")
     @GetMapping ("/mine")
     public ResponseDto getMyMeetings(
          @RequestParam (value="year", required = false) Integer year,
          @RequestParam(value="month", required = false) Integer month,
          @AuthenticationPrincipal UserDetailsImpl userDetails
     ){
          User user = userDetails.getUser();
          if (user == null) throw new RestApiException(Code.NOT_FOUND_AUTHORIZATION_IN_SECURITY_CONTEXT);
     
          year = (year == null || year == 0) ? LocalDateTime.now().getYear() : year;
          month = (month == null || month == 0) ? LocalDateTime.now().getMonthValue() : month;
          
          return DataResponseDto.of(calendarService.getMyMeetings(user.getId(), year, month));
     }
}
