package com.hanghae.finalProject.rest.follow.service;

import com.hanghae.finalProject.config.errorcode.Code;
import com.hanghae.finalProject.config.exception.RestApiException;
import com.hanghae.finalProject.config.util.SecurityUtil;
import com.hanghae.finalProject.rest.follow.dto.FollowListResponesDto;
import com.hanghae.finalProject.rest.follow.dto.FollowResponseDto;
import com.hanghae.finalProject.rest.follow.model.Follow;
import com.hanghae.finalProject.rest.follow.repository.FollowRepository;
import com.hanghae.finalProject.rest.user.model.User;
import com.hanghae.finalProject.rest.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

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
          Follow isFollow = followRepository.findByUserAndFollowId(user, followId).orElseGet(new Follow());
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
    //팔로잉 리스트 (내가 팔로우)
    public FollowListResponesDto followingList() {
         //userId를 가져와서 follow테이블의 userId에 해당하는 모든 팔로우 아이디를 가져옴
        //그 팔로우 아이디에 해당하는 user테이블의 정보 userId(기준), username,profileUrl,
        FollowListResponesDto followListResponseDto = new FollowListResponesDto();
        User user = SecurityUtil.getCurrentUser();
        List<Follow> users = followRepository.findByUser(user);

        for (Follow value : users) {
            Long follow = value.getFollowId();
            List<User> followList = userRepository.findAllById(Collections.singleton(follow));//findAllById라는
            for (User followId : followList){
                followListResponseDto.addFollowList(new FollowResponseDto(followId));
            }

        }

       ;//follow테이블에있는 모두가져온다 기준은 UserId를기준으로 (매개값을 기준으로)

//        for (int i = 0; i < users.size(); i++) {
//            Follow followId =users.get(i);
//            System.out.println(followId);
//        }
//        for(Follow follow : users){
//            FollowListResponesDto.addFollowList(new FollowResponseDto(follow));
//        }














         return followListResponseDto;
    }
}
