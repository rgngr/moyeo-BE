package com.hanghae.finalProject.rest.comment.controller;

import com.hanghae.finalProject.config.controller.errorcode.Code;
import com.hanghae.finalProject.config.dto.DataResponseDto;
import com.hanghae.finalProject.config.dto.ResponseDto;
import com.hanghae.finalProject.rest.comment.dto.CommentRequestDto;
import com.hanghae.finalProject.rest.comment.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "comment", description = "댓글 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/meetings/{meetingId}")
public class CommentController {
    private final CommentService commentService;

    @Operation(summary = "댓글 조회")
    @GetMapping("/comments")
    public ResponseDto getCommentList(@PathVariable Long meetingId) {
        return DataResponseDto.of(commentService.getCommentList(meetingId));
    }

    @Operation(summary = "댓글 작성")
    @PostMapping("/comments")
    public ResponseDto createComment(@PathVariable Long meetingId,
                                     @RequestBody CommentRequestDto commentRequestDto) {
        return DataResponseDto.of(commentService.createComment(meetingId, commentRequestDto), Code.CREATE_COMMENT.getStatusMsg());
    }

    @Operation(summary = "댓글 삭제")
    @DeleteMapping("/comments/{commentId}")
    public ResponseDto deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseDto.of(true, Code.DELETED_COMMENT);
    }
}
