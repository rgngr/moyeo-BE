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
@RequestMapping("/api/users/mail-code")
public class EmailController {

    private final EmailService emailService;
    @Operation(summary = "이메일 인증코드 발송", description = "사용자가 입력한 이메일에 인증코드 발송및 redis에 이메일 저장")
    @GetMapping("/create")
    public ResponseDto emailCreate(@RequestParam String email) throws Exception {
        emailService.emailCreate(email);
        return ResponseDto.of(true, Code.EMAIL_CODE);
    }
    @Operation(summary = "인증 코드 확인", description = "사용자가 입력한 이메일에 인증코드 발송및 redis에 이메일 저장")
    @GetMapping("/confirm")
    public ResponseDto emailConfirm(@RequestParam String email,
                                    @RequestParam String ePw){
        emailService.emailConfirm(email,ePw);
        return ResponseDto.of(true, Code.EMAIL_CONFIRM_SUCCESS);
    }
}