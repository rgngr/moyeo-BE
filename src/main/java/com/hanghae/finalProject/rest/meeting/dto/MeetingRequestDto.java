package com.hanghae.finalProject.rest.meeting.dto;

import com.hanghae.finalProject.rest.meeting.model.CategoryCode;
import lombok.Getter;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
public class MeetingRequestDto {

    @NotNull(message = "제목을 입력해주세요.")
    private String title;

    @NotNull(message = "카테고리를 선택해주세요.")
    private CategoryCode category;

    @NotNull(message = "시작 날짜를 선택해주세요.")
    private LocalDateTime startDate;

    @NotNull(message = "시작 시간을 선택해주세요.")
    private LocalDateTime startTime;

    @NotNull(message = "예상 소요시간 입력해주세요.")
    private int duration;

    @NotNull(message = "성새 설명을 입력해주세요.")
    private String content;

    @NotNull(message = "최대 정원을 입력해주세요.")
    private int maxNum;

    private String platform;

    private String link;

    private boolean secret;

    private String password;

}
