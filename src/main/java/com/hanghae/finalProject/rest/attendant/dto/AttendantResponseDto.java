package com.hanghae.finalProject.rest.attendant.dto;

import com.hanghae.finalProject.rest.attendant.model.Attendant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AttendantResponseDto {
     private Long attendantId;

     private Long meetingId;

     private Long userId;

     private boolean attend;

     private boolean review;

     public AttendantResponseDto(Attendant attendant) {
         this.attendantId = attendant.getId();
         this.meetingId = attendant.getMeeting().getId();
         this.userId = attendant.getUser().getId();
         this.attend = attendant.isAttend();
     }
}
