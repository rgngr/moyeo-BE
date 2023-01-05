package com.hanghae.finalProject.rest.meeting.dto;

import com.hanghae.finalProject.rest.attendant.dto.AttendantResponseDto;
import com.hanghae.finalProject.rest.meeting.model.CategoryCode;
import com.hanghae.finalProject.rest.meeting.model.Meeting;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class MeetingListResponseDto {
     List<ResponseDto> meetingList = new ArrayList<>();
     public void addMeetingList(List<ResponseDto> meetingList){
          this.meetingList = meetingList;
     }
     
     @Getter
     @RequiredArgsConstructor
     public static class ResponseDto {
         private Long id;
         private Long masterId;
         private boolean master;
         private String title;
         private CategoryCode category;
         private LocalDateTime startDate;
         private LocalDateTime startTime;
         private int duration;
         private String platform;
//         private String link;
//         private String content;
         private int maxNum;
         private boolean secret;
         private String password;
         private boolean attend;
//         private boolean alarm;
//         private int likeNum;
//         private int hateNum;
     
          private int attendantsNum;
          private List<AttendantResponseDto.forMeetings> attendantsList;
     
          public ResponseDto(Meeting meeting, Long userId){
               this.id = meeting.getId();
               this.masterId = meeting.getUser().getId();
               this.master = (meeting.getUser().getId().equals(userId));
               this.title = meeting.getTitle();
               this.category = meeting.getCategory(); // category model type enum으로 변경하기
               this.startDate = meeting.getStartDate();
               this.startTime = meeting.getStartTime();
               this.duration = meeting.getDuration();
               this.maxNum = meeting.getMaxNum();
               this.platform = meeting.getPlatform();
               this.secret = meeting.isSecret();
               this.password = meeting.getPassword();
               this.attend = false; // TODO 참석구현 후 수정필요
          }
     
     }

//
//    public MeetingListResponseDto(Meeting meeting, boolean isMaster,
//                                  boolean isAttend, boolean isAlarm, int likeNum, int hateNum) {
//            this.id = meeting.getId();
//            this.masterId = meeting.getUser().getId();
//            this.isMaster = isMaster;
//            this.title = meeting.getTitle();
//            this.category = meeting.getCategory();
//            this.startDate = meeting.getStartDate();
//            this.startTime = meeting.getStartTime();
//            this.duration = meeting.getDuration();
//            this.content = meeting.getContent();
//            this.maxNum = meeting.getMaxNum();
//            this.platform = meeting.getPlatform();
//            this.link = meeting.getLink();
//            this.secret = meeting.isSecret();
//            this.password = meeting.getPassword();
//            this.attend =isAttend;
//            this.alarm = isAlarm;
//            this.likeNum = likeNum;
//            this.hateNum =hateNum;
//    }

}
