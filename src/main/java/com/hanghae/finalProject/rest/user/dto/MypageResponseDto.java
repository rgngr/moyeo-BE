package com.hanghae.finalProject.rest.user.dto;

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
}
