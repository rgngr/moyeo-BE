package com.hanghae.finalProject.rest.alarm.dto;

import com.hanghae.finalProject.rest.alarm.model.AlarmList;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class AlarmListResponseDto {

    List<Alarm1> alarmList = new ArrayList<>();

    public void addAlarm(Alarm1 alarm) {
        alarmList.add(alarm);
    }

    @Getter
    @Setter
    public static class Alarm1 {

        private Long id;
        private String content;
        private String url;
        private LocalDateTime createdAt;

        public Alarm1(AlarmList alarm) {
            this.id = alarm.getId();
            this.content = alarm.getContent();
            this.url = alarm.getUrl();
            this.createdAt = alarm.getCreatedAt();
        }
    }

}
