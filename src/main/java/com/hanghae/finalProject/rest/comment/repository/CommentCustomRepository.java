package com.hanghae.finalProject.rest.comment.repository;

import com.hanghae.finalProject.rest.comment.dto.CommentResponseDto;

import java.util.List;

public interface CommentCustomRepository {
     List<CommentResponseDto> findByMeetingIdOrderByCreatedAtDesc(Long meetingId, Long commentId);
}
