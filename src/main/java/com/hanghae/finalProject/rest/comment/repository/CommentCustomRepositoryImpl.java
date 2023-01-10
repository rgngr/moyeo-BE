package com.hanghae.finalProject.rest.comment.repository;

import com.hanghae.finalProject.rest.comment.dto.CommentResponseDto;
import com.hanghae.finalProject.rest.comment.model.Comment;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.services.databrew.model.Project;

import java.util.List;

import static com.hanghae.finalProject.rest.comment.model.QComment.comment1;
import static com.hanghae.finalProject.rest.user.model.QUser.user;

@RequiredArgsConstructor
@Repository
public class CommentCustomRepositoryImpl implements CommentCustomRepository {
     
     private final JPAQueryFactory jpaQueryFactory;
     
     @Override
     public List<CommentResponseDto> findByMeetingIdOrderByCreatedAtAsc(Long meetingId) {
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
               .on(comment1.meeting.id.eq(meetingId), comment1.user.id.eq(user.id))
               .orderBy(comment1.createdAt.asc())
               .fetch();
     }
}
