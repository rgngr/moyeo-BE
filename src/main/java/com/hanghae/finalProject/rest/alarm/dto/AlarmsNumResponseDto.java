package com.hanghae.finalProject.rest.alarm.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AlarmsNumResponseDto {
    private int alarmsNum;

    public AlarmsNumResponseDto(int alarmsNum) {
        this.alarmsNum = alarmsNum;
    }
}
