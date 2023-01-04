package com.hanghae.finalProject.rest.meeting.repository;

import com.hanghae.finalProject.rest.meeting.model.Meeting;
import com.hanghae.finalProject.rest.meeting.model.QMeeting;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.hanghae.finalProject.rest.meeting.model.QMeeting.meeting;

@Repository
public class MeetingCustomRepositoryImpl implements MeetingCustomRepository{
     
     private final JPAQueryFactory jpaQueryFactory;
     
     public MeetingCustomRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
          this.jpaQueryFactory = jpaQueryFactory;
     }
     
     @Override
     public List<Meeting> findAllByOrderByIdDesc(Long meetingIdx) {
          return jpaQueryFactory
               .selectFrom(meeting)
               .where(
                    ltBookId(meetingIdx)
               )
               .orderBy(meeting.id.desc())
               .limit(5)
               .fetch();
     }
     
     private BooleanExpression ltBookId(Long meetingIdx) {
          if (meetingIdx == null) {
               return null; // BooleanExpression 자리에 null이 반환되면 조건문에서 자동으로 제거된다
          }
          return meeting.id.lt(meetingIdx);
     }
}
