package com.hanghae.finalProject.rest.calendar.dto;

import com.hanghae.finalProject.rest.meeting.model.CategoryCode;
import com.hanghae.finalProject.rest.meeting.model.PlatformCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class MyMeetingResponseDto {
     
     List<MyMeetingResponseDto.ResponseDto> meetingList = new ArrayList<>();
     public MyMeetingResponseDto addMeetingList(List<MyMeetingResponseDto.ResponseDto> meetingList){
          this.meetingList = meetingList;
          return this;
     }
     
     @Setter
     @Getter
     @RequiredArgsConstructor
     @AllArgsConstructor
     public static class ResponseDto {
          private Long id;
          private String title;
          private CategoryCode category;
          private LocalDateTime startTime;
          private int duration;
          private PlatformCode platform;
          private String content;
          private boolean secret;
          private String password;
          private boolean attend; // 실제 입장 여부
          private boolean review; // 리뷰 달았는지 여부
     }
}
