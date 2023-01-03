package com.hanghae.finalProject.rest.meeting.dto;

import com.hanghae.finalProject.rest.meeting.model.Meeting;
import com.hanghae.finalProject.rest.review.model.Review;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MeetingCreateResponseDto {

    private Long id;
    private Long masterId;
    private boolean isMaster;
    private String title;
    private String category;
    private LocalDateTime startDate;
    private LocalDateTime startTime;
    private int duration;
    private String content;
    private int maxNum;
    private String platform;
    private String link;
    private boolean secret;
    private String password;
    private int likeNum;
    private int hateNum;

    public MeetingCreateResponseDto(Meeting meeting, boolean isMaster, int likeNum, int hateNum) {
        this.id = meeting.getId();
        this.masterId = meeting.getUser().getId();
        this.isMaster = isMaster;
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
        this.likeNum = likeNum;
        this.hateNum =hateNum;
    }

}
