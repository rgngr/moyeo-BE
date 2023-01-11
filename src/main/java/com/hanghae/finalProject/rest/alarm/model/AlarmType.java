package com.hanghae.finalProject.rest.alarm.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum AlarmType {

    NEW_ATTENDANT("new attendant!"),
    NEW_CANCELER("new canceler!"),
    FULL_CAPACITY("full capacity!"),
    NEW_COMMENT("new comment!"),
    PUT_LINK("링크를 입력해주세요!"),
    UPDATE_MEETING("참석 예정 모임 글이 수정되었습니다. 확인해보세요!"),
    NEW_LINK("참석 예정 모임 장소 LINK가 생성되었습니다. 확인해보세요!"),
    ;

    private final String alarmText;

}
