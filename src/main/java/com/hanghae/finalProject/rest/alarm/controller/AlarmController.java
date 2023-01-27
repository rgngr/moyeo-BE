package com.hanghae.finalProject.rest.alarm.controller;

import com.hanghae.finalProject.config.dto.DataResponseDto;
import com.hanghae.finalProject.config.dto.ResponseDto;
import com.hanghae.finalProject.config.errorcode.Code;
import com.hanghae.finalProject.rest.alarm.service.AlarmService;
import com.hanghae.finalProject.rest.attendant.service.AttendantService;
import io.swagger.annotations.ApiOperation;
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

     @ApiOperation(value = "알람 구독")
     @GetMapping(value = "/alarm/subscribe/{id}", produces = "text/event-stream")
     public SseEmitter subscribe(@PathVariable Long id,
                                 @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId) {
          return alarmService.subscribe(id, lastEventId);
     }

//     @ApiOperation(value = "알람 구독")
//     @GetMapping(value = "/alarm/subscribe", produces = "text/event-stream")
//     public SseEmitter subscribe(@RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId) {
//          return alarmService.subscribe(lastEventId);
//     }

     @ApiOperation(value = "모든 알람 목록")
     @GetMapping(value = "/alarms")
     public ResponseDto getAlarms() {
          return DataResponseDto.of(alarmService.getAlarms(), Code.GET_ALARMS.getStatusMsg());
     }

     @ApiOperation(value = "알람 읽음 처리")
     @PatchMapping(value = "/alarms/{id}")
     public ResponseDto alarmIsRead(@PathVariable Long id) {
          alarmService.alarmIsRead(id);
          return ResponseDto.of(true, Code.ALARM_IS_READ);
     }

}
