package com.hanghae.finalProject.rest.user.service;


import com.hanghae.finalProject.config.errorcode.Code;
import com.hanghae.finalProject.config.exception.RestApiException;
import com.hanghae.finalProject.config.jwt.JwtUtil;
import com.hanghae.finalProject.rest.user.dto.LoginRequestDto;
import com.hanghae.finalProject.rest.user.dto.SignupRequestDto;
import com.hanghae.finalProject.rest.user.model.User;
import com.hanghae.finalProject.rest.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
     private final UserRepository userRepository;

     private final JwtUtil jwtUtil;

     private final PasswordEncoder passwordEncoder;

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
