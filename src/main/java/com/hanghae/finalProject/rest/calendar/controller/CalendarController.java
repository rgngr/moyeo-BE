package com.hanghae.finalProject.rest.calendar.controller;

import com.hanghae.finalProject.config.dto.DataResponseDto;
import com.hanghae.finalProject.config.dto.ResponseDto;
import com.hanghae.finalProject.rest.calendar.service.CalendarService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
          @RequestParam(value="month", required = false) Integer month
     ){
          return DataResponseDto.of(calendarService.getMyMeetings(year, month));
     }
}
