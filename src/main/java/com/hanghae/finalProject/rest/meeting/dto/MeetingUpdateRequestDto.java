package com.hanghae.finalProject.rest.meeting.dto;

import lombok.Getter;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
public class MeetingUpdateRequestDto {

    @NotNull(message = "제목을 입력해주세요.")
    private String title;

    @NotNull(message = "시작 날짜를 선택해주세요.")
    private LocalDateTime startDate;

    @NotNull(message = "시작 시간을 선택해주세요.")
    private LocalDateTime startTime;

    @NotNull(message = "예상 소요시간 입력해주세요.")
    private int duration;

    @NotNull(message = "성새 설명을 입력해주세요.")
    private String content;

    private String platform;

    private String link;

    private boolean secret;

    private String password;

}
