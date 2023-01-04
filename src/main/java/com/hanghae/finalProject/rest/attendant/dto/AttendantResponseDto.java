package com.hanghae.finalProject.rest.attendant.dto;

import lombok.Getter;

public class AttendantResponseDto {
//      “userId”:1,
//      “username”: “abc”,
//      “userProfileImg”: “abc.png”,
//      “followed”:false
     
     // meeting list 에 들어가는 용
     @Getter
     public static class forMeetings{
          private Long userId;
          private String userProfileImg;
     }
}
