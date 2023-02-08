package com.hanghae.finalProject.rest.alarm.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AlarmCountResponseDto {
    private int alarmCount;

    public AlarmCountResponseDto(int alarmCount) {
        this.alarmCount = alarmCount;
    }
}
