package com.hanghae.finalProject.rest.comment.controller;

import com.hanghae.finalProject.config.dto.DataResponseDto;
import com.hanghae.finalProject.config.dto.ResponseDto;
import com.hanghae.finalProject.config.errorcode.Code;
import com.hanghae.finalProject.rest.comment.dto.CommentRequestDto;
import com.hanghae.finalProject.rest.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/meetings/{meetingId}")
public class CommentController {
    private final CommentService commentService;

    @GetMapping("/comments")
    public ResponseDto getCommentList(@PathVariable Long postId) {
        return DataResponseDto.of(commentService.getCommentList(postId));
    }

    @PostMapping("/comments")
    public ResponseDto createComment(@PathVariable Long postId,
                                     @RequestBody CommentRequestDto commentRequestDto) {
        return DataResponseDto.of(commentService.createComment(postId, commentRequestDto), Code.CREATE_COMMENT.getStatusMsg());
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseDto deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseDto.of(true, Code.DELETE_COMMENT);
    }
}
