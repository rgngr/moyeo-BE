package com.hanghae.finalProject.rest.comment.dto;

import com.hanghae.finalProject.rest.comment.model.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponseDto {
    private Long commentId;

    private String username;
    private String profileUrl;

    private String comment;

    private LocalDateTime createdAt;

    private boolean deleted;

    public CommentResponseDto (Comment comment) {
        this.commentId = comment.getId();
        this.username = comment.getUser().getUsername();
        this.comment = comment.getComment();
        this.createdAt = comment.getCreatedAt();
        this.deleted = comment.isDeleted();
    }
}
