package com.hanghae.finalProject.rest.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hanghae.finalProject.config.dto.DataResponseDto;
import com.hanghae.finalProject.config.dto.ResponseDto;
import com.hanghae.finalProject.config.errorcode.Code;
import com.hanghae.finalProject.rest.user.dto.LoginRequestDto;
import com.hanghae.finalProject.rest.user.dto.ProfileRequestDto;
import com.hanghae.finalProject.rest.user.dto.SignupRequestDto;
import com.hanghae.finalProject.rest.user.repository.UserRepository;
import com.hanghae.finalProject.rest.user.service.KakaoService;
import com.hanghae.finalProject.rest.user.service.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

@Tag (name="user", description = "사용자 API")
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    
    private final UserRepository userRepository;
    private final UserService userService;
    private final KakaoService kakaoService;
    
    @Operation (summary = "회원가입", description = "email, username, password 로 회원가입 ")
    @PostMapping("/signup")
    public ResponseDto signup(@RequestBody @Valid SignupRequestDto requestDto) {
        userService.signUp(requestDto);
//          return ResponseEntity.ok(new PrivateResponseBody(UserStatusCode.USER_SIGNUP_SUCCESS));
//          return DataResponseDto.of("data test", "test 성공"); //data있고 별도 msg보낼 경우
        return ResponseDto.of(true, Code.USER_SIGNUP_SUCCESS);
    }

    @Operation(summary = "로그인", description = "email, password 로 로그인 ")
    @PostMapping("/login")
    public ResponseDto login(@RequestBody @Valid LoginRequestDto RequestDto, HttpServletResponse response) {

        return DataResponseDto.of( userService.login(RequestDto, response), Code.USER_LOGIN_SUCCESS.getStatusMsg());
    }

//          // 1. data o , msg o
////          return DataResponseDto.of("data test", "test 성공"); 
//          // 2. data o msg 정상
////          return DataResponseDto.of("data test");
//          // 3. DATA X, MSG 따로
//          return ResponseDto.of(true, Code.USER_SIGNUP_SUCCESS);
    
    //https://kauth.kakao.com/oauth/authorize?client_id=ced49bfdb65f5f152e2e43f12e88bd86&redirect_uri=https://sparta-hippo.shop/api/user/kakao/callback&response_type=code
    //https://kauth.kakao.com/oauth/authorize?client_id=ced49bfdb65f5f152e2e43f12e88bd86&redirect_uri=http://localhost:8080/api/user/kakao/callback&response_type=code
    @Operation(summary = "카카오 로그인 콜백", description = "email, password 로 로그인")
    @GetMapping ("/kakao/callback")
    public ResponseDto kakaoLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
        return DataResponseDto.of(kakaoService.kakaoLogin(code, response), Code.USER_LOGIN_SUCCESS.getStatusMsg());
    }

    @ApiOperation(value = "프로필 이미지 변경")
    @PatchMapping(value = "/profile-image",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseDto updateProfile(@RequestParam(value="file") MultipartFile file) throws IOException {
            return DataResponseDto.of(userService.updateProfile(file), Code.UPDATE_PROFILE.getStatusMsg());
    }

}
