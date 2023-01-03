package com.hanghae.finalProject.rest.user.dto;

import com.hanghae.finalProject.rest.user.model.User;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@RequiredArgsConstructor
public class LoginResponseDto {

    private Long id;

    private String username;

    private String profileUrl;

    public LoginResponseDto(User user){
        this.id = user.getId();
        this.username = user.getUsername();
        this.profileUrl = user.getProfileUrl();
    }

}
