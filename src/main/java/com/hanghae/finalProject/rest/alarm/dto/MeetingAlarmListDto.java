package com.hanghae.finalProject.rest.alarm.dto;

import lombok.Data;

import java.time.LocalTime;
import java.util.List;

@Data
public class MeetingAlarmListDto {
     private Long meetingId;
     private LocalTime startTime;
     private String title;
     private Long meetingUserId;
     
     private List<Long> alarmUserIdList;
}
