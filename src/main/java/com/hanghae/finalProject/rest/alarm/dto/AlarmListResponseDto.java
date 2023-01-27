package com.hanghae.finalProject.rest.alarm.dto;

import com.hanghae.finalProject.rest.alarm.model.AlarmList;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class AlarmListResponseDto {

    private Long id;
    private String content;
    private boolean isRead;
    private LocalDateTime createdAt;

    public AlarmListResponseDto(AlarmList alarmList) {
        this.id = alarmList.getId();
        this.content = alarmList.getContent();
        this.isRead = alarmList.isRead();
        this.createdAt = alarmList.getCreatedAt();
    }

}
