package com.hanghae.finalProject.rest.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
public class SignupRequestDto {
     
     //    @Pattern (regexp="^(?=.*?[0-9])(?=.*?[a-z]).{5,10}$")
     @Schema (description = "유저명")
     @Size (min = 5, max = 10)
     private String username;
     
     @Schema (description = "패스워드")
     @Size (min = 8, max = 15)
     @Pattern (regexp = "^.(?=.*\\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&+=]).*$")
     private String password;
     
     @Schema (description = "이메일")
     @NotBlank
     private String email;
     
     
}
