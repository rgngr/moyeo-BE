package com.hanghae.finalProject.rest.user.service;

import com.hanghae.finalProject.config.jwt.JwtUtil;
import com.hanghae.finalProject.rest.user.dto.SignupRequestDto;
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
//          User user = SecurityUtil.getCurrentUser(); // 비회원일경우(토큰못받았을경우) null
          
     }
//     User user = SecurityUtil.getCurrentUser();// 비회원일경우 null

//     Post post = postRepository.findByIdAndDeletedIsFalse(id).orElseThrow(
//          // 삭제 or 존재하지않는 글일경우
//          () -> new RestApiException(CommonStatusCode.NO_ARTICLE)
//     );
//     throw new RestApiException(CommonStatusCode.INVALID_USER);
}
