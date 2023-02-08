package com.hanghae.finalProject.rest.meeting.dto;

import com.hanghae.finalProject.rest.meeting.model.CategoryCode;
import com.hanghae.finalProject.rest.meeting.model.Meeting;
import com.hanghae.finalProject.rest.meeting.model.PlatformCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class MeetingDetailResponseDto {

    private Long id;
    private Long masterId;
    private boolean master = false;
    private String title;
    private CategoryCode category;
    private LocalDate startDate;
    private LocalTime startTime;
    private int duration;
    private String content;
    private int maxNum;
    private PlatformCode platform;
    private String link;
    private boolean secret;
    private String password;
    private boolean attend;
    private boolean entrance;
    private boolean review;
    private boolean alarm;
    private Long likeNum;
    private Long hateNum;
    private String image;

    public void isMaster(boolean master){
         this.master = master;
    }
}
