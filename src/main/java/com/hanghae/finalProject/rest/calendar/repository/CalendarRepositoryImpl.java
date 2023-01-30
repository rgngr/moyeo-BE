package com.hanghae.finalProject.rest.calendar.repository;

import com.hanghae.finalProject.rest.attendant.repository.AttendantRepository;
import com.hanghae.finalProject.rest.calendar.dto.MyMeetingResponseDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import static com.hanghae.finalProject.rest.attendant.model.QAttendant.attendant;
import static com.hanghae.finalProject.rest.meeting.model.QMeeting.meeting;

@Slf4j
@RequiredArgsConstructor
@Repository
public class CalendarRepositoryImpl implements CalendarCustomRepository{
     
     private final JPAQueryFactory jpaQueryFactory;
     private final AttendantRepository attendantRepository;
     
     // 나의 월별 데이터 들고오기
     @Override
     public List<MyMeetingResponseDto.ResponseDto> findAllByUserIdAndMonth(Long loggedId, int year, int month) {
          LocalDate startDate = LocalDate.of(year, month, 1);
          LocalDate endDate = LocalDate.of(year, month+1, 1).minusDays(1L);
          log.info("startDate : {} , endDate : {} ",startDate, endDate);
          
          return jpaQueryFactory
               .select(Projections.fields(
                    MyMeetingResponseDto.ResponseDto.class,
                    meeting.id.as("id"),
                    meeting.title,
                    meeting.category,
                    meeting.startDate,
                    meeting.startTime,
                    meeting.duration,
                    meeting.platform,
                    meeting.content,
                    meeting.secret,
                    meeting.password,
                    meeting.image,
                    attendant.entrance.as("attend"),
                    attendant.review.as("review")
               ))
               .from(meeting)
               .join(attendant)
               .on(meeting.id.eq(attendant.meeting.id), attendant.user.id.eq(loggedId))
               .where(meeting.startDate.between(startDate, endDate),
                    meeting.deleted.isFalse())
               .orderBy(meeting.startTime.asc())
               .fetch();
     }
}
