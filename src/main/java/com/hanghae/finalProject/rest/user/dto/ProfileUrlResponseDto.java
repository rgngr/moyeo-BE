package com.hanghae.finalProject.rest.user.dto;

import com.hanghae.finalProject.rest.user.model.User;
import lombok.Getter;

@Getter
public class ProfileUrlResponseDto {
    private String profileUrl;

    public ProfileUrlResponseDto(User user) {
        this.profileUrl = user.getProfileUrl();
    }
}
