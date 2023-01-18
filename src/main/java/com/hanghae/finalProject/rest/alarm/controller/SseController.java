package com.hanghae.finalProject.rest.alarm.controller;

import com.hanghae.finalProject.config.errorcode.Code;
import com.hanghae.finalProject.config.exception.RestApiException;
import com.hanghae.finalProject.config.util.SecurityUtil;
import com.hanghae.finalProject.rest.user.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
public class SseController {

//    public static Map<Long, SseEmitter> sseEmitters = new ConcurrentHashMap<>();
//
//    @GetMapping(value = "/sub", consumes = MediaType.ALL_VALUE)
//    public SseEmitter subscribe() {
//
//        //로그인 확인 및 현재 유저 정보 들고오기
//        User user = SecurityUtil.getCurrentUser();
//        if (user == null) throw new RestApiException(Code.NOT_FOUND_AUTHORIZATION_IN_SECURITY_CONTEXT);
//
//        // userId
//        Long userId = user.getId();
//
//        // 현재 유저의 SseEmitter 생성
//        SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);
//        try {
//            // 연결!!
//            sseEmitter.send(SseEmitter.event().name("connect"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        // userId싱 key값으로 해서 SseEmitter를 저장
//        sseEmitters.put(userId, sseEmitter);
//
//        sseEmitter.onCompletion(() -> sseEmitters.remove(userId));
//        sseEmitter.onTimeout(() -> sseEmitters.remove(userId));
//        sseEmitter.onError((e) -> sseEmitters.remove(userId));
//
//        return sseEmitter;
//    }

}
