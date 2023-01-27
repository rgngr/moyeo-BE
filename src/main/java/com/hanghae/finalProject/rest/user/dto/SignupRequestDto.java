package com.hanghae.finalProject.rest.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
public class SignupRequestDto {
    @Schema(description = "유저명")
    @Size(min = 5, max = 10)
    private String username;

    @Schema (description = "패스워드")
    @Size(min = 8, max = 15)
    @Pattern (regexp="^.(?=.*\\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&+()_*=]).*$")
    private String password;

    @Schema (description = "이메일")
    @Email
    @NotBlank
    private String email;



}
