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

     @Transactional
     public ProfileResponseDto updateProfile(MultipartFile file) throws IOException {
          User user = SecurityUtil.getCurrentUser();
          if (user == null) throw new RestApiException(Code.NOT_FOUND_AUTHORIZATION_IN_SECURITY_CONTEXT);

          String currentProfileUrl = user.getProfileUrl();

          if(!file.isEmpty()) {
               String profileUrl = s3Uploader.upload(file,"file");
               user.updateProfile(profileUrl);
               userRepository.saveAndFlush(user);

               return new ProfileResponseDto(user);

          } else {
               if(currentProfileUrl != null) {
                    s3Uploader.deleteFile(currentProfileUrl.split(".com/")[1]);

                    user.deleteProfileUrl();
                    return new ProfileResponseDto(user);
               } else {
                    throw new RestApiException(Code.NO_IMAGE);
               }

          }

     }
     
     
     public Object getMypage() {
          User user = SecurityUtil.getCurrentUser();
          if (user == null) throw new RestApiException(Code.NOT_FOUND_AUTHORIZATION_IN_SECURITY_CONTEXT);
          return null;
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
