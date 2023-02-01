package com.hanghae.finalProject.rest.user.repository;

import com.hanghae.finalProject.rest.user.dto.MypageResponseDto;
import com.hanghae.finalProject.rest.user.model.User;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import static com.hanghae.finalProject.rest.attendant.model.QAttendant.attendant;
import static com.hanghae.finalProject.rest.follow.model.QFollow.follow;
import static com.hanghae.finalProject.rest.user.model.QUser.user;
import static com.querydsl.jpa.JPAExpressions.select;

@Slf4j
@RequiredArgsConstructor
@Repository
public class UserCustomRepositoryImpl implements UserCustomRepository {
     
     private final JPAQueryFactory jpaQueryFactory;
     
     @Override
     public MypageResponseDto findByUserAndAttendantAndFollow(User loggedUser) {
          
          return jpaQueryFactory
               .select(Projections.fields(
                    MypageResponseDto.class,
                    // 로그인유저의 참석모임 수
                    ExpressionUtils.as(
                         select(attendant.count().intValue())
                              .from(attendant)
                              .where(attendant.user.id.eq(loggedUser.getId()))
                         , "attendantsNum"),
                    // 로그인유저의 팔로워수 (나를 팔로우 한 사람들)
                    ExpressionUtils.as(
                         select(follow.count().intValue())
                              .from(follow)
                              .where(follow.following.id.eq(loggedUser.getId()))
                         , "followersNum"),
                    // 로그인유저의 팔로잉수 (내가 팔로우한 사람들)
                    ExpressionUtils.as(select(follow.count().intValue())
                         .from(follow)
                         .where(follow.user.id.eq(loggedUser.getId()))
                         , "followingsNum")
               ))
               .from(user)
               .where(user.id.eq(loggedUser.getId()))
               .fetchOne();
     }
}
