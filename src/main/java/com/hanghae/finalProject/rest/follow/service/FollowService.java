package com.hanghae.finalProject.rest.follow.service;

import com.hanghae.finalProject.config.errorcode.Code;
import com.hanghae.finalProject.config.exception.RestApiException;
import com.hanghae.finalProject.config.util.SecurityUtil;
import com.hanghae.finalProject.rest.alarm.service.AlarmService;
import com.hanghae.finalProject.rest.follow.dto.FollowListResponesDto;
import com.hanghae.finalProject.rest.follow.dto.FollowResponseDto;
import com.hanghae.finalProject.rest.follow.model.Follow;
import com.hanghae.finalProject.rest.follow.repository.FollowRepository;
import com.hanghae.finalProject.rest.user.model.User;
import com.hanghae.finalProject.rest.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FollowService {

     private final UserRepository userRepository;
     private final FollowRepository followRepository;
     private final AlarmService alarmService;
     
     @Transactional
     public Code follow(Long followId) {
          User user = SecurityUtil.getCurrentUser();
          if (user == null) throw new RestApiException(Code.NOT_FOUND_AUTHORIZATION_IN_SECURITY_CONTEXT);
          //본인은 팔로우 안됨
          if(user.getId()==followId) throw new RestApiException(Code.USER_FOLLOW_FAIL);
          // 팔로우상대가 존재하는가
          User followUser = userRepository.findById(followId).orElseThrow(
               () -> new RestApiException(Code.NO_USER)
          );
          
          // 기존에 팔로우한 사람인가
          Follow isFollow = followRepository.findByUserAndFollowing(user, followUser).orElseGet(new Follow());
          if (isFollow == null) {
               // 기존 팔로우 x
               followRepository.save(new Follow(user, followUser));
               // 알림
              alarmService.alarmFollow(user, followUser);
               return Code.USER_FOLLOW_SUCCESS;
          } else {
               // 기존 팔로우 o >> 팔로우 취소
               followRepository.delete(isFollow);
               return Code.USER_UNFOLLOW_SUCCESS;
          }
     }
    //팔로잉 리스트 (내가 팔로우)
    @Transactional
    public FollowListResponesDto followingList() {
         //userId를 가져와서 follow테이블의 userId에 해당하는 모든 팔로우 아이디를 가져옴
        //그 팔로우 아이디에 해당하는 user테이블의 정보 userId(기준), username,profileUrl,
        //팔로워 리스트를 담을객체생성
        FollowListResponesDto followListResponseDto = new FollowListResponesDto();
        //토큰값에의한 유저데이터 가져옴
        User user = SecurityUtil.getCurrentUser();
        if (user == null) throw new RestApiException(Code.NOT_FOUND_AUTHORIZATION_IN_SECURITY_CONTEXT);
        //followRepository에서 기준은 user를 기준으로 User 같은값을 통째로 다 가져옴
        List<Follow> followList = followRepository.findByUser(user);
        //followList(내가 팔로우한사람들의 user데이터)에 들어있는 같은값을
        // 하나씩뺴서 addFollowList실행해 리스트로 많들어줌
        for (Follow value : followList) {
            followListResponseDto.addFollowList(new FollowResponseDto(value.getFollowing()));
        }

         return followListResponseDto;
    }
    //팔로워 리스트 (쟤가 팔로우)
    @Transactional
    public FollowListResponesDto followerList() {
        //userId를 가져와서 follow테이블의 userId에 해당하는 모든 팔로우 아이디를 가져옴
        //그 팔로우 아이디에 해당하는 user테이블의 정보 userId(기준), username,profileUrl,
        //팔로워 리스트를 담을객체생성
        FollowListResponesDto followListResponseDto = new FollowListResponesDto();
        //토큰값에의한 유저데이터 가져옴
        User user = SecurityUtil.getCurrentUser();
        if (user == null) throw new RestApiException(Code.NOT_FOUND_AUTHORIZATION_IN_SECURITY_CONTEXT);
        //followRepository에서 기준은 user의 id값을 기준으로 followId와 같은값을 다 가져옴
        List<Follow> followList = followRepository.findByFollow(user);
        //followList(팔로우한사람들의 user데이터)에 들어있는 같은값을
        // 하나씩뺴서 addFollowList실행해 리스트로 많들어줌
        for (Follow value : followList) {
            followListResponseDto.addFollowList(new FollowResponseDto(value.getUser()));
        }

        return followListResponseDto;
    }
}
