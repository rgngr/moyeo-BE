package com.hanghae.finalProject.rest.alarm.controller;

import com.hanghae.finalProject.rest.alarm.service.AlarmService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AlarmController {

    private final AlarmService alarmService;

    @ApiOperation(value = "알람")
    @GetMapping(value = "/alarm", produces = "text/event-stream")
    public SseEmitter alarm(@RequestHeader(value = "Last-Alarm-ID", required = false, defaultValue = "") String lastAlarmId) {
        return alarmService.alarm(lastAlarmId);
    }

}
