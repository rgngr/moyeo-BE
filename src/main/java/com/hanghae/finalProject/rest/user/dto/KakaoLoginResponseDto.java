package com.hanghae.finalProject.rest.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class KakaoLoginResponseDto {
     
     private Long id;
     @Schema (description = "유저명")
     private String username;
     
     @Schema(description = "프로필URL")
     private String profileUrl;
     
     @JsonInclude (JsonInclude.Include.NON_EMPTY)
     private String jwtToken;
     
     public KakaoLoginResponseDto(String username, String profileUrl) {
          this.username = username;
          this.profileUrl = profileUrl;
     }
}
