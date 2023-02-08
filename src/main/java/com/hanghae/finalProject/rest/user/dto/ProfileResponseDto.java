package com.hanghae.finalProject.rest.user.dto;

import com.hanghae.finalProject.rest.user.model.User;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class ProfileResponseDto {

    private String username;
    private String profileUrl;
    private String profileMsg;
    public ProfileResponseDto(User user) {
        this.username = user.getUsername();
        this.profileUrl = user.getProfileUrl();
        this.profileMsg = user.getProfileMsg();
    }

    @Getter
    public static class Url {

        private String profileUrl;

        public Url(User user) {
            this.profileUrl = user.getProfileUrl();
        }
    }

}
