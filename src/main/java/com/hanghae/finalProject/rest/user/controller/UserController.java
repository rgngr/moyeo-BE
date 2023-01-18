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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/api/users")
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

          // 1. data o , msg o
//          return DataResponseDto.of("data test", "test 성공");
          // 2. data o msg 정상
//          return DataResponseDto.of("data test");
            // 3. DATA X, MSG 따로
//          return ResponseDto.of(true, Code.USER_SIGNUP_SUCCESS);
    
    //https://kauth.kakao.com/oauth/authorize?client_id=ced49bfdb65f5f152e2e43f12e88bd86&redirect_uri=http://localhost:3000/api/user/kakao/callback&response_type=code
    @Operation(summary = "카카오 로그인 콜백", description = "email, password 로 로그인")
    @GetMapping ("/kakao/callback")
    public ResponseDto kakaoLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
        return DataResponseDto.of(kakaoService.kakaoLogin(code, response), Code.USER_LOGIN_SUCCESS.getStatusMsg());
    }

    @ApiOperation(value = "프로필 수정 페이지 불러오기")
    @GetMapping(value = "/profile/update-page")
    public ResponseDto getProfileUpdatePage() {
        return DataResponseDto.of(userService.getProfileUpdatePage(), Code.GET_PROFILE_UPDATE_PAGE.getStatusMsg());
    }

    @ApiOperation(value = "프로필 이미지 변경")
    @PatchMapping(value = "/profileUrl",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseDto updateProfileUrl(@RequestParam(value="file") MultipartFile file) throws IOException {
        return DataResponseDto.of(userService.updateProfileUrl(file), Code.UPDATE_PROFILE_URL.getStatusMsg());
    }

    @ApiOperation(value = "프로필 이미지 삭제")
    @DeleteMapping(value = "/profileUrl")
    public ResponseDto deleteProfileUrl() {
        userService.deleteProfileUrl();
        return ResponseDto.of(true, Code.DELETE_PROFILE_URL);
    }

    @ApiOperation(value = "프로필 username/자기소개 수정")
    @PatchMapping(value = "/profile")
    public ResponseDto updateProfile(@Valid @RequestBody ProfileRequestDto requestDto, HttpServletResponse response){
        return DataResponseDto.of(userService.updateProfile(requestDto, response), Code.UPDATE_PROFILE.getStatusMsg());
    }
    
    @Operation(summary = "마이페이지 불러오기")
    @GetMapping("/mypage")
    public ResponseDto getMypage(){
        return DataResponseDto.of(userService.getMypage());
    }

}
