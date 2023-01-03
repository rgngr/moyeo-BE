package com.hanghae.finalProject.rest.comment.service;
import com.hanghae.finalProject.config.controller.errorcode.Code;
import com.hanghae.finalProject.config.exception.RestApiException;
import com.hanghae.finalProject.config.util.SecurityUtil;
import com.hanghae.finalProject.rest.comment.dto.CommentRequestDto;
import com.hanghae.finalProject.rest.comment.dto.CommentResponseDto;
import com.hanghae.finalProject.rest.comment.model.Comment;
import com.hanghae.finalProject.rest.comment.repository.CommentRepository;
import com.hanghae.finalProject.rest.meeting.model.Meeting;
import com.hanghae.finalProject.rest.meeting.repository.MeetingRepository;
import com.hanghae.finalProject.rest.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final MeetingRepository meetingRepository;

    // 댓글 조회
    @Transactional(readOnly = true)
    public List<CommentResponseDto> getCommentList(Long meetingId) {
        List<CommentResponseDto> commentList = new ArrayList<>();
        List<Comment> comments = commentRepository.findByMeetingIdOrderByCreatedAtDesc(meetingId);
        for (Comment comment : comments) {
            commentList.add(new CommentResponseDto(comment));
        }
        return commentList;
    }

    // 댓글 작성
    @Transactional
    public CommentResponseDto createComment(Long meetingId, CommentRequestDto commentRequestDto) {

        User user = SecurityUtil.getCurrentUser();
        if (user == null) throw new RestApiException(Code.NOT_FOUND_AUTHORIZATION_IN_SECURITY_CONTEXT);

        Meeting meeting = meetingRepository.findByIdAndDeletedIsFalse(meetingId).orElseThrow(
                () -> new RestApiException(Code.NO_MEETING)
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
