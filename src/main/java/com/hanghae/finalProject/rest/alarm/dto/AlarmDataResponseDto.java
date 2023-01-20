package com.hanghae.finalProject.rest.alarm.dto;

import com.hanghae.finalProject.rest.alarm.model.AlarmList;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AlarmDataResponseDto {

    private Long id;
    private String content;
//    private Integer[] createdAt;

    @Builder
    public AlarmDataResponseDto(Long id, String content) {
        this.id = id;
        this.content = content;
    }

    public static AlarmDataResponseDto from(AlarmList alarmList) {
        return AlarmDataResponseDto.builder()
                .id(alarmList.getId())
                .content(alarmList.getContent())
                .build();
    }

}
