package com.hanghae.finalProject.rest.user.service;

import com.hanghae.finalProject.config.errorcode.Code;
import com.hanghae.finalProject.config.exception.RestApiException;
import com.hanghae.finalProject.config.jwt.JwtUtil;
import com.hanghae.finalProject.config.util.SecurityUtil;
import com.hanghae.finalProject.rest.user.dto.SignupRequestDto;
import com.hanghae.finalProject.rest.user.model.User;
import com.hanghae.finalProject.rest.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
     private final UserRepository userRepository;
     
     private final JwtUtil jwtUtil;
     
     private final PasswordEncoder passwordEncoder;
     
     public void signUp(SignupRequestDto requestDto) {
     
          
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
}
