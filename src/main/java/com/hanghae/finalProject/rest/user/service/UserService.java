package com.hanghae.finalProject.rest.user.service;

import com.hanghae.finalProject.config.errorcode.UserStatusCode;
import com.hanghae.finalProject.config.exception.RestApiException;
import com.hanghae.finalProject.config.jwt.JwtUtil;
import com.hanghae.finalProject.rest.user.dto.SignupRequestDto;
import com.hanghae.finalProject.rest.user.model.User;
import com.hanghae.finalProject.rest.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
//          User user = SecurityUtil.getCurrentUser(); // 비회원일경우(토큰못받았을경우) null
          Optional<User> userfound = userRepository.findByUsername(username);
          if(userfound.isPresent()){
               throw new RestApiException(UserStatusCode.OVERLAPPED_USERNAME);
          }
          Optional<User> emailfound = userRepository.findByKakaoIdIsNullAndEmail(email);
          if(emailfound.isPresent()){
               throw new RestApiException(UserStatusCode.OVERLAPPED_USERNAME);
          }
          User user = new User(username,password,email);
          userRepository.save(user);

     }

//     User user = SecurityUtil.getCurrentUser();// 비회원일경우 null

//     Post post = postRepository.findByIdAndDeletedIsFalse(id).orElseThrow(
//          // 삭제 or 존재하지않는 글일경우
//          () -> new RestApiException(CommonStatusCode.NO_ARTICLE)
//     );
//     throw new RestApiException(CommonStatusCode.INVALID_USER);
}
