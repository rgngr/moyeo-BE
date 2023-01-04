package com.hanghae.finalProject.rest.user.dto;

import com.hanghae.finalProject.rest.user.model.User;
import lombok.Getter;

@Getter
public class ProfileResponseDto {

    private String profileUrl;
    public ProfileResponseDto(User user) {
        this.profileUrl = user.getProfileUrl();
    }

}
