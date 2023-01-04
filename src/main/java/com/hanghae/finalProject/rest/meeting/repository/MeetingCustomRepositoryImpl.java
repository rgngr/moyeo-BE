package com.hanghae.finalProject.rest.meeting.repository;

import com.hanghae.finalProject.rest.meeting.model.Meeting;
import com.hanghae.finalProject.rest.meeting.model.QMeeting;
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
               .orderBy(meeting.id.desc())
               .fetch();
     }
}
