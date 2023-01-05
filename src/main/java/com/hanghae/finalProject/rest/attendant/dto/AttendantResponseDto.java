package com.hanghae.finalProject.rest.attendant.dto;

import com.hanghae.finalProject.rest.attendant.model.Attendant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AttendantResponseDto {
     private Long attendantId;

     private Long meetingId;

     private Long userId;

     private boolean attend; // entrance 로 수정필요

     private boolean review;
     private String userProfileImg; // 지우기

     public AttendantResponseDto(Attendant attendant) {
         this.attendantId = attendant.getId();
         this.meetingId = attendant.getMeeting().getId();
         this.userId = attendant.getUser().getId();
         this.attend = attendant.isAttend();
     }
     
     @Getter
     @Setter
     @AllArgsConstructor
     @NoArgsConstructor
     // 메인리스트의 attendantsList 용
     public static class simpleResponseDto{
//          private Long attendantId;
//          private Long meetingId;
          private Long userId;
//          private boolean entrance; // entrance 로 수정필요
//          private boolean review;
          private String userProfileImg;
     }
}
