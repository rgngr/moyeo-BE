package com.hanghae.finalProject.rest.meeting.dto;

import com.hanghae.finalProject.rest.meeting.model.CategoryCode;
import com.hanghae.finalProject.rest.meeting.model.Meeting;
import com.hanghae.finalProject.rest.meeting.model.PlatformCode;
import lombok.Getter;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
public class MeetingUpdatePageResponseDto {

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

    public MeetingUpdatePageResponseDto(Meeting meeting) {
        this.title = meeting.getTitle();
        this.category = meeting.getCategory();
        this.startDate = meeting.getStartDate();
        this.startTime = meeting.getStartTime();
        this.duration = meeting.getDuration();
        this.content = meeting.getContent();
        this.maxNum = meeting.getMaxNum();
        this.platform = meeting.getPlatform();
        this.link = meeting.getLink();
        this.secret = meeting.isSecret();
        this.password = meeting.getPassword();
    }

}
