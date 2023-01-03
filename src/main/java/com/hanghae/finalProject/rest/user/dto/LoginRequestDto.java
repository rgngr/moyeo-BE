package com.hanghae.finalProject.rest.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Setter
@Getter
public class LoginRequestDto {

    @Schema(description = "이메일")
    @Email
    @NotBlank
    private String email;

    @Schema (description = "패스워드")
    @Size(min = 8, max = 15)
    private String password;

}
