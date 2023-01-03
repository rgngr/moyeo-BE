package com.hanghae.finalProject.rest.comment.service;
import com.hanghae.finalProject.config.errorcode.Code;
import com.hanghae.finalProject.config.exception.RestApiException;
import com.hanghae.finalProject.config.util.SecurityUtil;
import com.hanghae.finalProject.rest.comment.dto.CommentRequestDto;
import com.hanghae.finalProject.rest.comment.dto.CommentResponseDto;
import com.hanghae.finalProject.rest.comment.model.Comment;
import com.hanghae.finalProject.rest.comment.repository.CommentRepository;
import com.hanghae.finalProject.rest.meeting.model.Meeting;
import com.hanghae.finalProject.rest.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    // 포스트 레포지토리 추가 필요

    // 댓글 조회
    @Transactional
    public List<Comment> getCommentList(Long postId) {
        List<Comment> commentList = commentRepository.findByMeetingIdOrderByCreatedAtDesc(postId);
        return commentList;
    }

    // 댓글 작성
    @Transactional
    public CommentResponseDto createComment(Long postId, CommentRequestDto commentRequestDto) {

        User user = SecurityUtil.getCurrentUser();
        if (user == null) throw new RestApiException(Code.NOT_FOUND_AUTHORIZATION_IN_SECURITY_CONTEXT);

        Meeting meeting = postRepository.findByIdAndDeletedIsFalse(postId).orElseThrow(
                () -> new RestApiException(Code.NO_ARTICLE)
        );
        Comment comment = commentRepository.save(new Comment(commentRequestDto, meeting, user));

        return new CommentResponseDto(comment);
    }

    // 댓글 삭제
    @Transactional
    public Code deleteComment(Long commentId) {
        User user = SecurityUtil.getCurrentUser();
        if (user == null) throw new RestApiException(Code.NOT_FOUND_AUTHORIZATION_IN_SECURITY_CONTEXT);

        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new RestApiException(Code.NO_COMMENT)
        );

        if (!user.getId().equals(comment.getUser().getId())) {
            throw new RestApiException(Code.INVALID_USER_DELETE);
        }

        commentRepository.delete(comment);
        return Code.DELETE_COMMENT;
    }
}
