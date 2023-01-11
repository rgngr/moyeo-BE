package com.hanghae.finalProject.rest.attendant.repository;

import com.hanghae.finalProject.rest.attendant.dto.AttendantListResponseDto;
import com.hanghae.finalProject.rest.user.model.User;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.hanghae.finalProject.rest.attendant.model.QAttendant.attendant;
import static com.hanghae.finalProject.rest.user.model.QUser.user;
import static com.hanghae.finalProject.rest.follow.model.QFollow.follow;

@RequiredArgsConstructor
@Repository
public class AttendantCustomRepositoryImpl implements AttendantCustomRepository {
     
     private final JPAQueryFactory jpaQueryFactory;
     
     // 유저정보, 팔로우정보 가진 참석자리스트
     @Override
     public List<AttendantListResponseDto> findByMeetingIdAndFollow(Long meetingId, User loggedInUser) {
          Long loggedId = loggedInUser != null ? loggedInUser.getId() : null;
          
          return jpaQueryFactory
               .select(Projections.fields(
                    AttendantListResponseDto.class,
                    attendant.user.id.as("userId"),
                    user.username,
                    user.profileUrl,
                    follow.user.id.isNotNull().as("followed")))
               .from(attendant)
               .join(user)
               .on(attendant.meeting.id.eq(meetingId), attendant.user.id.eq(user.id)) // 참석자 유저정보
               .leftJoin(follow)
               .on(follow.user.id.eq(loggedId), follow.followId.eq(user.id)) // 로그인유저의 팔로우유무
               .where(attendant.meeting.id.eq(meetingId), user.deleted.isFalse())
               .fetch();
          
     }
}
