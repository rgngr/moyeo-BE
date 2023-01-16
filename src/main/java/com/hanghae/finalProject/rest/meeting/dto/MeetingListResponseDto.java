package com.hanghae.finalProject.rest.meeting.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hanghae.finalProject.rest.attendant.dto.AttendantResponseDto;
import com.hanghae.finalProject.rest.meeting.model.CategoryCode;
import com.hanghae.finalProject.rest.meeting.model.Meeting;
import com.hanghae.finalProject.rest.meeting.model.PlatformCode;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class MeetingListResponseDto {
     List<ResponseDto> meetingList = new ArrayList<>();
     public MeetingListResponseDto addMeetingList(List<ResponseDto> meetingList){
          this.meetingList = meetingList;
          return this;
     }
     
     @Setter
     @Getter
     @RequiredArgsConstructor
     @AllArgsConstructor
     public static class ResponseDto {
         private Long id;
         private Long masterId;
         private boolean master;
         private String title;
         private CategoryCode category;
         private LocalDate startDate;
         private LocalTime startTime;
         private int duration;
         private PlatformCode platform;
         private String content;
         private int maxNum;
         private boolean secret;
         private String password;
         private boolean attend;
          private int attendantsNum;
          private List<AttendantResponseDto.simpleResponseDto> attendantsList;
     }
     

}
