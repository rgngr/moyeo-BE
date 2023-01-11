package com.hanghae.finalProject.rest.follow.service;

import com.hanghae.finalProject.config.errorcode.Code;
import com.hanghae.finalProject.config.exception.RestApiException;
import com.hanghae.finalProject.config.util.SecurityUtil;
import com.hanghae.finalProject.rest.follow.model.Follow;
import com.hanghae.finalProject.rest.follow.repository.FollowRepository;
import com.hanghae.finalProject.rest.user.model.User;
import com.hanghae.finalProject.rest.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FollowService {
     
     private final UserRepository userRepository;
     private final FollowRepository followRepository;
     
     @Transactional
     public Code follow(Long followId) {
          User user = SecurityUtil.getCurrentUser();
          if (user == null) throw new RestApiException(Code.NOT_FOUND_AUTHORIZATION_IN_SECURITY_CONTEXT);
          
          // 팔로우상대가 존재하는가
          User followUser = userRepository.findById(followId).orElseThrow(
               () -> new RestApiException(Code.NO_USER)
          );
          
          // 기존에 팔로우한 사람인가
          Follow isFollow = followRepository.findByUserIdAndFollowId(user.getId(), followId).orElseGet(new Follow());
          if (isFollow == null) {
               // 기존 팔로우 x
               followRepository.save(new Follow(user, followId));
               return Code.USER_FOLLOW_SUCCESS;
          } else {
               // 기존 팔로우 o >> 팔로우 취소
               followRepository.delete(isFollow);
               return Code.USER_UNFOLLOW_SUCCESS;
          }
          
     }
}
