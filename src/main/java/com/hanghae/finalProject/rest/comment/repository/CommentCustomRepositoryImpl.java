package com.hanghae.finalProject.rest.comment.repository;

import com.hanghae.finalProject.rest.comment.dto.CommentResponseDto;
import com.hanghae.finalProject.rest.comment.model.Comment;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.services.databrew.model.Project;

import java.util.List;

import static com.hanghae.finalProject.rest.comment.model.QComment.comment1;
import static com.hanghae.finalProject.rest.meeting.model.QMeeting.meeting;
import static com.hanghae.finalProject.rest.user.model.QUser.user;

@RequiredArgsConstructor
@Repository
public class CommentCustomRepositoryImpl implements CommentCustomRepository {
     
     private final JPAQueryFactory jpaQueryFactory;
     
     @Override
     public List<CommentResponseDto> findByMeetingIdOrderByCreatedAtDesc(Long meetingId, Long commentId) {
          List<Long> ids = jpaQueryFactory
               .select(comment1.id)
               .from(comment1)
               .where(
                    comment1.meeting.id.eq(meetingId),
                    comment1.deleted.isFalse(),
                    ltCommentId(commentId)
               )
               .orderBy(comment1.id.desc())
               .limit(10)
               .fetch();
          
          return jpaQueryFactory
               .select(Projections.fields(
                         CommentResponseDto.class,
                         comment1.id.as("commentId"),
                         user.username,
                         user.profileUrl,
                         comment1.comment,
                         comment1.createdAt,
                         comment1.deleted
                    )
               ).from(comment1)
               .join(user)
               .on(comment1.user.id.eq(user.id))
               .where(comment1.id.in(ids))
               .orderBy(comment1.createdAt.desc())
               .fetch();
     }
     
     private BooleanExpression ltCommentId(Long commentId) {
          if (commentId == null) {
               return null; // BooleanExpression 자리에 null이 반환되면 조건문에서 자동으로 제거된다
          }
          return comment1.id.lt(commentId);
     }
}
