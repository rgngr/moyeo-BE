package com.hanghae.finalProject.rest.follow.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter

public class FollowListResponesDto {
    List<FollowResponseDto> followList = new ArrayList<>();
    public void addFollowList(FollowResponseDto followResponseDto) {
        followList.add(followResponseDto);
    }
}
