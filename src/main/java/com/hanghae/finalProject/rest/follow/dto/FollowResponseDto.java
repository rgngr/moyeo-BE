package com.hanghae.finalProject.rest.follow.dto;

import com.hanghae.finalProject.rest.follow.model.Follow;
import com.hanghae.finalProject.rest.user.model.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FollowResponseDto {
    private Long id;

    private String username;

    private String profile;

    public FollowResponseDto(User user){
        this.id = user.getId();
        this.username = user.getUsername();
        this.profile = user.getProfileUrl();


    }
}
