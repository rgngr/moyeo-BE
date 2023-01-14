package com.hanghae.finalProject.rest.review.service;

import com.hanghae.finalProject.config.errorcode.Code;
import com.hanghae.finalProject.config.exception.RestApiException;
import com.hanghae.finalProject.config.util.SecurityUtil;
import com.hanghae.finalProject.rest.attendant.model.Attendant;
import com.hanghae.finalProject.rest.attendant.repository.AttendantRepository;
import com.hanghae.finalProject.rest.meeting.model.Meeting;
import com.hanghae.finalProject.rest.meeting.repository.MeetingRepository;
import com.hanghae.finalProject.rest.review.dto.ReviewRequestDto;
import com.hanghae.finalProject.rest.review.dto.ReviewResponseDto;
import com.hanghae.finalProject.rest.review.model.Review;
import com.hanghae.finalProject.rest.review.repository.ReviewRepository;
import com.hanghae.finalProject.rest.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewService {
     private final MeetingRepository meetingRepository;
     private final ReviewRepository reviewRepository;
     private final AttendantRepository attendantRepository;
     
     // 후기작성
     @Transactional
     public ReviewResponseDto createReview(Long meetingId, ReviewRequestDto requestDto) {
          User user = SecurityUtil.getCurrentUser();
          if (user == null) throw new RestApiException(Code.NOT_FOUND_AUTHORIZATION_IN_SECURITY_CONTEXT);
          
          // 존재하는 모임인가
          Meeting meeting = meetingRepository.findById(meetingId).orElseThrow(() -> new RestApiException(Code.NO_MEETING));
          // 참석했던 모임인가
          Attendant attendant = attendantRepository.findByUserAndMeeting(user, meeting).orElseThrow(
               () -> new RestApiException(Code.NO_AUTH_REVIEW)
          );
          // 입장했던 모임인가
          if(!attendant.isEntrance()){
               throw new RestApiException(Code.NO_AUTH_REVIEW);
          }
          // 이미 후기를 작성하였는가
          if(reviewRepository.existsByUserAndMeeting(user,meeting)){
               throw new RestApiException(Code.NO_MORE_REVIEW);
          };
          // 후기 저장
          Review review = reviewRepository.save(new Review(meeting, user, requestDto.isLike()));
          // 참석자테이블에 review true 업데이트
          attendant.makeReview(true);
          
          return new ReviewResponseDto(review.getId(), user.getUsername(), review.isReview());
     }
}
