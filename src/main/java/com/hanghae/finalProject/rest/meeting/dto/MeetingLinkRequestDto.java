package com.hanghae.finalProject.rest.meeting.dto;

import com.hanghae.finalProject.rest.meeting.model.PlatformCode;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class MeetingLinkRequestDto {

    @NotNull(message = "모임 플랫폼을 선택해주세요.")
    private PlatformCode platform;

    @NotNull(message = "모임 링크를 입력해주세요.")
    private String link;

}
