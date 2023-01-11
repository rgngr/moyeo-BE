package com.hanghae.finalProject.rest.alarm.controller;

import com.hanghae.finalProject.config.errorcode.Code;
import com.hanghae.finalProject.config.exception.RestApiException;
import com.hanghae.finalProject.config.jwt.JwtUtil;
import com.hanghae.finalProject.config.util.SecurityUtil;
import com.hanghae.finalProject.rest.user.model.User;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AlarmController {

    public static Map<Long, SseEmitter> sseEmitters = new ConcurrentHashMap<>();
    private final JwtUtil jwtUtil;

    @ApiOperation(value = "알람")
    @GetMapping(value = "/sub", consumes = MediaType.ALL_VALUE)
    public SseEmitter subscribe(@RequestParam String token) {

        User user = SecurityUtil.getCurrentUser();
        if (user == null) throw new RestApiException(Code.NOT_FOUND_AUTHORIZATION_IN_SECURITY_CONTEXT);

        Long userId = user.getId();

        SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);
        try {
            sseEmitter.send(SseEmitter.event().name("connect"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        sseEmitters.put(userId, sseEmitter);

        sseEmitter.onCompletion(() -> sseEmitters.remove(userId));
        sseEmitter.onTimeout(() -> sseEmitters.remove(userId));
        sseEmitter.onError((e) -> sseEmitters.remove(userId));

        return sseEmitter;
    }


}
