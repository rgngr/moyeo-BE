package com.hanghae.finalProject.rest.calendar.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.hanghae.finalProject.rest.meeting.model.CategoryCode;
import com.hanghae.finalProject.rest.meeting.model.PlatformCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class MyMeetingResponseDto implements Serializable {
     
     List<MyMeetingResponseDto.ResponseDto> meetingList = new ArrayList<>();
     public MyMeetingResponseDto addMeetingList(List<MyMeetingResponseDto.ResponseDto> meetingList){
          this.meetingList = meetingList;
          return this;
     }
     
     @Setter
     @Getter
     @RequiredArgsConstructor
     @AllArgsConstructor
     public static class ResponseDto implements Serializable {
          private Long id;
          private String title;
          private CategoryCode category;
     
          @JsonDeserialize (using = LocalDateDeserializer.class)
          @JsonSerialize (using = LocalDateSerializer.class)
          private LocalDate startDate;
     
          @JsonDeserialize (using = LocalTimeDeserializer.class)
          @JsonSerialize (using = LocalTimeSerializer.class)
          private LocalTime startTime;
          
          private int duration;
          private PlatformCode platform;
          private String content;
          private boolean secret;
          private String password;
          private boolean attend; // 실제 입장 여부
          private boolean review; // 리뷰 달았는지 여부
          private String image;
     }
}
