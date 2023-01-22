package com.hanghae.finalProject.rest.comment.dto;

import com.hanghae.finalProject.rest.comment.model.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponseDto {
    private Long commentId;

    private String username;
    private String profileUrl;

    private String comment;
    private LocalDateTime createdAt;

    private LocalDate createdDate;
    private LocalTime createdTime;

    private boolean deleted;

    public CommentResponseDto (Comment comment) {
        this.commentId = comment.getId();
        this.username = comment.getUser().getUsername();
        this.comment = comment.getComment();
        this.createdDate = comment.getCreatedAt().toLocalDate();
        this.createdTime = comment.getCreatedAt().toLocalTime();
        this.deleted = comment.isDeleted();
    }
}
