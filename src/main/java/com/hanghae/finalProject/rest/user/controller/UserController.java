package com.hanghae.finalProject.rest.user.controller;

import com.hanghae.finalProject.config.dto.PrivateResponseBody;
import com.hanghae.finalProject.config.errorcode.UserStatusCode;
import com.hanghae.finalProject.rest.user.dto.SignupRequestDto;
import com.hanghae.finalProject.rest.user.repository.UserRepository;
import com.hanghae.finalProject.rest.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping ("/api/user")
@RequiredArgsConstructor
public class UserController {

     private final UserRepository userRepository;
     private final UserService userService;
     
     
     @PostMapping ("/signup")
     public PrivateResponseBody signup(@RequestBody @Valid SignupRequestDto requestDto) {
          userService.signUp(requestDto);
          return new PrivateResponseBody(UserStatusCode.USER_SIGNUP_SUCCESS);
     }


     
}
