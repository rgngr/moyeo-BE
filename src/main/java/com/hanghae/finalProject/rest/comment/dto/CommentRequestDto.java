package com.hanghae.finalProject.rest.comment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class CommentRequestDto {
    @Schema(description = "댓글 내용 입력")
    private String comment;
}
