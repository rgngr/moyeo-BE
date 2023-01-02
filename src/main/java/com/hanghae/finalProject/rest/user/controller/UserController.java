package com.hanghae.finalProject.rest.user.controller;

import com.hanghae.finalProject.config.dto.PrivateResponseBody;
import com.hanghae.finalProject.config.errorcode.UserStatusCode;
import com.hanghae.finalProject.rest.user.dto.SignupRequestDto;
import com.hanghae.finalProject.rest.user.repository.UserRepository;
import com.hanghae.finalProject.rest.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping ("/api/user")
@RequiredArgsConstructor
public class UserController {
     
//     private final PasswordEncoder passwordEncoder;
     private final UserRepository userRepository;
     private final UserService userService;
     
     
//     @PostMapping ("/signup")
//     public PrivateResponseBody signup(@RequestBody @Valid SignupRequestDto requestDto) {
//          userService.signUp(requestDto);
////          return ResponseEntity.ok(new PrivateResponseBody(UserStatusCode.USER_SIGNUP_SUCCESS));
//          return new PrivateResponseBody(UserStatusCode.USER_SIGNUP_SUCCESS);
//     }
//
//     @PostMapping ("/login")
//     public PrivateResponseBody login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
//          return new PrivateResponseBody(UserStatusCode.USER_LOGIN_SUCCESS, userService.login(loginRequestDto, response));
//     }
     
}
