package com.hanghae.finalProject.rest.comment.repository;

import com.hanghae.finalProject.rest.comment.dto.CommentResponseDto;
import com.hanghae.finalProject.rest.comment.model.Comment;
import com.hanghae.finalProject.rest.meeting.model.Meeting;

import java.util.List;

public interface CommentCustomRepository {
     List<CommentResponseDto> findByMeetingOrderByCreatedAtDesc(Long meetingId, Long commentId);
}
