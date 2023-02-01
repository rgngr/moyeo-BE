package com.hanghae.finalProject.rest.user.dto;

import com.hanghae.finalProject.rest.user.model.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MypageResponseDto {
     
     private Long userId;
     private String username;
     private String profileUrl;
     private String profileMsg;
     private int attendantsNum;
     private int followersNum;
     private int followingsNum;
     
     public MypageResponseDto addUser(User user){
          this.userId = user.getId();
          this.username = user.getUsername();
          this.profileUrl = user.getProfileUrl();
          this.profileMsg = user.getProfileMsg();
          return this;
     }
}
