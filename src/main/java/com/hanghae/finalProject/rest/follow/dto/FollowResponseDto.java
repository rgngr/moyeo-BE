package com.hanghae.finalProject.rest.follow.dto;

import com.hanghae.finalProject.rest.user.model.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FollowResponseDto {
    private Long id;

    private String username;

    private String profileUrl;

    public FollowResponseDto(User user){
        this.id = user.getId();
        this.username = user.getUsername();
        this.profileUrl = user.getProfileUrl();


    }
}
