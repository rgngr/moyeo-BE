package com.hanghae.finalProject.rest.attendant.dto;

import com.hanghae.finalProject.rest.attendant.model.Attendant;
import com.hanghae.finalProject.rest.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AttendantListResponseDto {
    private Long userId;
    private String username;
    private String profileUrl;
    private boolean followed;

    public AttendantListResponseDto(Attendant attendant) {
        this.userId = attendant.getUser().getId();
        this.username = attendant.getUser().getUsername();
        this.profileUrl = attendant.getUser().getProfileUrl();
    }
}
