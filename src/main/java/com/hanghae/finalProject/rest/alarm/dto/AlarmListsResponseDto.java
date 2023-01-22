package com.hanghae.finalProject.rest.alarm.dto;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class AlarmListsResponseDto {

    List<AlarmListResponseDto> alarmLists = new ArrayList<>();

    public void addAlarmList(AlarmListResponseDto responseDto) {
        alarmLists.add(responseDto);
    }

}
