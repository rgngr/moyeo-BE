package com.hanghae.finalProject.rest.user.controller;

import com.hanghae.finalProject.config.dto.ResponseDto;
import com.hanghae.finalProject.config.errorcode.Code;
import com.hanghae.finalProject.rest.user.dto.SignupRequestDto;
import com.hanghae.finalProject.rest.user.repository.UserRepository;
import com.hanghae.finalProject.rest.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Tag (name="user", description = "사용자 API")
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;
    
    @Operation (summary = "회원가입", description = "email, username, password 로 회원가입 ")
    @PostMapping("/signup")
    public ResponseDto signup(@RequestBody @Valid SignupRequestDto requestDto) {
        userService.signUp(requestDto);
//          return ResponseEntity.ok(new PrivateResponseBody(UserStatusCode.USER_SIGNUP_SUCCESS));
//          return DataResponseDto.of("data test", "test 성공"); //data있고 별도 msg보낼 경우
        return ResponseDto.of(true, Code.USER_SIGNUP_SUCCESS);
    }

//     @PostMapping ("/login")
//     public ResponseEntity<PrivateResponseBody> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
//          return new ResponseEntity<>(new PrivateResponseBody(UserStatusCode.USER_LOGIN_SUCCESS, userService.login(loginRequestDto, response)), HttpStatus.OK);
//     }

//     public ResponseDto signup(@RequestBody @Valid SignupRequestDto requestDto) {
//          userService.signUp(requestDto);
//          // 1. data o , msg o
////          return DataResponseDto.of("data test", "test 성공"); //data있고 별도 msg보낼 경우
//          // 2. data o msg 정상
////          return DataResponseDto.of("data test");
//          // 3. DATA X, MSG 따로
//          return ResponseDto.of(true, Code.USER_SIGNUP_SUCCESS);
//     }


}
