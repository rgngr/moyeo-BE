package com.hanghae.finalProject.rest.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class ProfileRequestDto {

    @NotNull(message = "username을 선택해주세요.")
    private String username;

    private String profileMsg;
}
