package com.hanghae.finalProject.rest.user.controller;

import com.hanghae.finalProject.config.dto.DataResponseDto;
import com.hanghae.finalProject.config.dto.ResponseDto;
import com.hanghae.finalProject.config.errorcode.Code;
import com.hanghae.finalProject.rest.user.service.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class EmailController {

    private final EmailService emailService;
    @Operation(summary = "이메일 인증번호 발송", description = "사용자가 입력한 이메일에 인증코드 발송및 redis에 이메일 저장")
    @GetMapping("/user/mailConfirm")
    public ResponseDto mailConfirm(@RequestParam String email) throws Exception {
        String code = emailService.sendSimpleMessage(email);
        log.info("인증코드 : " + code);
        return DataResponseDto.of( code, Code.Email_Code.getStatusMsg());
    }

//    @PostMapping("/user/mailConfirms")
//    public ResponseDto SmsVerification(@RequestBody EmailCertificationRequest requestDto) {
//        EmailService.verifyEmail(requestDto);
//        return ResponseDto.of(true, Code.USER_SIGNUP_SUCCESS);
//    }
}