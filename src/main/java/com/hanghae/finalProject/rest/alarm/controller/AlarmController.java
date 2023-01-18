package com.hanghae.finalProject.rest.alarm.controller;

import com.hanghae.finalProject.config.dto.DataResponseDto;
import com.hanghae.finalProject.config.dto.ResponseDto;
import com.hanghae.finalProject.rest.alarm.service.AlarmService;
import com.hanghae.finalProject.rest.attendant.service.AttendantService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@RequestMapping ("/api")
public class AlarmController {
     private final AttendantService attendantService;
     private final AlarmService alarmService;
     
     @Operation (summary = "알림 받기/음소거")
     @PatchMapping ("/meetings/{meetingId}/alarm")
     public ResponseDto getAlarm(@PathVariable Long meetingId) {
          return DataResponseDto.of(attendantService.getAlarm(meetingId));
     }

     @GetMapping(value = "/alarm/subscribe", produces = "text/event-stream")
     public SseEmitter subscribe(@RequestParam String token,
                                 @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId) {
          return alarmService.subscribe(token, lastEventId);
     }

}
