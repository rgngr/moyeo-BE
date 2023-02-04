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

//     // userId로 구독 + Last-Event_Id
//     @ApiOperation(value = "알림 구독")
//     @GetMapping(value = "/alarm/subscribe/{id}", produces = "text/event-stream")
//     public SseEmitter subscribe(@PathVariable Long id,
//                                 @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId) {
//          return alarmService.subscribe(id, lastEventId);
//     }
     
//     @ApiOperation(value = "test용")
//     @GetMapping(value = "/alarm/subscribe/test")
//     public void repoTest() {
//          alarmService.testRepo();
//     }

     // 토큰으로 구독 + Last-Event_Id
     @ApiOperation(value = "알림 구독")
     @GetMapping(value = "/alarm/subscribe", produces = "text/event-stream")
     public SseEmitter subscribe(@RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId) {
          return alarmService.subscribe(lastEventId);
     }

     @ApiOperation(value = "알림 개수")
     @GetMapping(value = "/alarms/count")
     public ResponseDto alarmCount() {
          return DataResponseDto.of(alarmService.alarmCount(), Code.GET_ALARM_COUNT.getStatusMsg());
     }

     @ApiOperation(value = "알림 존재 여부 표시(빨간불)")
     @GetMapping(value = "/alarms/existence")
     public ResponseDto isExistAlarms() {
          return alarmService.isExistAlarms();
     }

     @ApiOperation(value = "모든 알림 목록")
     @GetMapping(value = "/alarms")
     public ResponseDto getAlarms() {
          return DataResponseDto.of(alarmService.getAlarms(), Code.GET_ALARMS.getStatusMsg());
     }

     @ApiOperation(value = "알림 삭제(읽음) 처리")
     @DeleteMapping(value = "/alarms/{id}")
     public ResponseDto deleteAlarm(@PathVariable Long id) {
          alarmService.deleteAlarm(id);
          return ResponseDto.of(true, Code.DELETE_ALARM);
     }

     @ApiOperation(value = "알림 전체 삭제")
     @DeleteMapping(value = "/alarms")
     public ResponseDto deleteAlarms() {
          alarmService.deleteAlarms();
          return ResponseDto.of(true, Code.DELETE_ALARMS);
     }

}
