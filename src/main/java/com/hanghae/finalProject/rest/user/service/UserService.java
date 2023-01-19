package com.hanghae.finalProject.rest.user.service;


import com.hanghae.finalProject.config.S3.S3Uploader;
import com.hanghae.finalProject.config.errorcode.Code;
import com.hanghae.finalProject.config.exception.RestApiException;
import com.hanghae.finalProject.config.jwt.JwtUtil;
import com.hanghae.finalProject.config.util.SecurityUtil;
import com.hanghae.finalProject.rest.user.dto.*;
import com.hanghae.finalProject.rest.user.model.User;
import com.hanghae.finalProject.rest.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.awt.print.Pageable;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class UserService {
     private final UserRepository userRepository;

     private final JwtUtil jwtUtil;

     private final PasswordEncoder passwordEncoder;

     private final S3Uploader s3Uploader;

     @Transactional
     public void signUp(SignupRequestDto requestDto) {
          String username = requestDto.getUsername();
          String email = requestDto.getEmail();
          String password = passwordEncoder.encode(requestDto.getPassword());

          if (userRepository.existsByUsername(username)) {
               throw new RestApiException(Code.OVERLAPPED_USERNAME);
          }
          if (userRepository.existsByEmail(email)) {
               throw new RestApiException(Code.OVERLAPPED_EMAIL);
          }
          userRepository.save(new User(username, password, email));
     }

     @Transactional(readOnly = true)
     public LoginResponseDto login(LoginRequestDto RequestDto, HttpServletResponse response){
          String email = RequestDto.getEmail();
          String password = RequestDto.getPassword();

          User user = userRepository.findByKakaoIdIsNullAndEmail(email).orElseThrow(
                  () -> new RestApiException(Code.NO_USER)
          );

          if (!passwordEncoder.matches(password,user.getPassword())){
               throw new RestApiException(Code.WRONG_PASSWORD);
          }

          response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(user.getUsername()));

          return new LoginResponseDto(user);
     }

     
     // 마이페이지 첫화면 불러오기
     public MypageResponseDto getMypage() {
          User user = SecurityUtil.getCurrentUser();
          if (user == null) throw new RestApiException(Code.NOT_FOUND_AUTHORIZATION_IN_SECURITY_CONTEXT);
     
          //참여모임수, 팔로워(나를 팔로우 추가한사람) 수 , 팔로잉(내가 팔로우) 수
          return userRepository.findByUserAndAttendantAndFollow(user);

     }

     // 프로필 수정
     @Transactional
     public ProfileResponseDto updateProfile(ProfileRequestDto requestDto, MultipartFile file,
                                             HttpServletResponse response) throws IOException {
          // 로그인 확인 및 현재 유저 정보 들고 오기
          User user = SecurityUtil.getCurrentUser();
          if (user == null) throw new RestApiException(Code.NOT_FOUND_AUTHORIZATION_IN_SECURITY_CONTEXT);

          // 현재 username
          String currentUsername = user.getUsername();
          //username update >> username은 NOT_NULL
          user.updateUsername(requestDto.getUsername());
          //토큰 재발급
          if (!requestDto.getUsername().equals(currentUsername)) {
               response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(user.getUsername()));
          }

          //profileMsg update
          if (requestDto.getProfileMsg() == null) {
               user.updateProfileMsg("");
          } else {
               user.updateProfileMsg(requestDto.getProfileMsg());
          }

          // 현재 profileUrl
          String currentProfileUrl = user.getProfileUrl();
          //profileUrl update
          if(file.isEmpty()) {
               user.updateProfileUrl(currentProfileUrl);
          } else {
               String profileUrl = s3Uploader.upload(file,"file");
               user.updateProfileUrl(profileUrl);
          }

          userRepository.saveAndFlush(user);

          return new ProfileResponseDto(user);

     }

     // 프로필 수정 페이지 불러오기
     @Transactional
     public ProfileResponseDto updateProfilePage() {
          // 로그인 확인 및 현재 유저 정보 들고 오기
          User user = SecurityUtil.getCurrentUser();
          if (user == null) throw new RestApiException(Code.NOT_FOUND_AUTHORIZATION_IN_SECURITY_CONTEXT);

          // username, profileUrl, profileMsg
          return new ProfileResponseDto(user);
     }


     // 프로필 이미지 삭제
     @Transactional
     public void deleteProfileUrl() {
          // 로그인 확인 및 현재 유저 정보 들고 오기
          User user = SecurityUtil.getCurrentUser();
          if (user == null) throw new RestApiException(Code.NOT_FOUND_AUTHORIZATION_IN_SECURITY_CONTEXT);

          //prufileUrl = null
          user.deleteProfileUrl();
     }

    


}
     
     // jwt token 에서 user정보뽑기 set
     //User user = SecurityUtil.getCurrentUser(); // 비회원일경우(토큰못받았을경우) null
     //if (user == null) throw new RestApiException(Code.NOT_FOUND_AUTHORIZATION_IN_SECURITY_CONTEXT);
     
//     // throw 기존버전
//     User user = userRepository.findByUsername("test");
//          if(user==null){
//          throw new RestApiException(Code.NOT_FOUND_AUTHORIZATION_IN_SECURITY_CONTEXT);
//     }
//     //>>>  orElseThrow 사용
//     User user = userRepository.findByUsername("test").orElseThrow(
//          () -> new RestApiException(Code.NOT_FOUND_AUTHORIZATION_IN_SECURITY_CONTEXT)

//     );
