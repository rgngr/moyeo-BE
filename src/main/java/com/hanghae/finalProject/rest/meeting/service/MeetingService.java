package com.hanghae.finalProject.rest.meeting.service;

import com.hanghae.finalProject.config.errorcode.Code;
import com.hanghae.finalProject.config.exception.RestApiException;
import com.hanghae.finalProject.config.util.SecurityUtil;
import com.hanghae.finalProject.rest.alarm.repository.AlarmRepository;
import com.hanghae.finalProject.rest.calendar.repository.CalendarRepository;
import com.hanghae.finalProject.rest.meeting.dto.*;
import com.hanghae.finalProject.rest.meeting.model.CategoryCode;
import com.hanghae.finalProject.rest.meeting.model.Meeting;
import com.hanghae.finalProject.rest.meeting.repository.MeetingRepository;
import com.hanghae.finalProject.rest.review.repository.ReviewRepository;
import com.hanghae.finalProject.rest.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MeetingService {
     private final MeetingRepository meetingRepository;
     private final ReviewRepository reviewRepository;
     private final CalendarRepository calendarRepository;
     private final AlarmRepository alarmRepository;
     
     @Transactional
     public MeetingDetailResponseDto getMeeting(Long id) {
          User user = SecurityUtil.getCurrentUser();
          
          Meeting meeting = meetingRepository.findById(id).orElseThrow(() -> new RestApiException(Code.NO_MEETING));
          
          if (meeting.isDeleted()) {
               throw new RestApiException(Code.NO_MEETING);
          }
          
          boolean isMaster = false;
          if (user.getId() == meeting.getUser().getId()) {
               isMaster = true;
          }

//        Calendar calendar = calendarRepository.findByMeetingIdAndUser(id, user).orElse(null);
          boolean isAttend = false;
//                calendar.isAttend();
          
          boolean isAlarm = alarmRepository.existsByMeetingIdAndUser(id, user);
          
          int likeNum = 0;
//                reviewRepository.countByMeetingIdAndLikeIsTrue(meeting.getId()).orElse(0L);
          int hateNum = 0;
//                reviewRepository.countByMeetingIdAndLikeIsFalse(meeting.getId()).orElse(0L);
          
          return new MeetingDetailResponseDto(meeting, isMaster, isAttend, isAlarm, likeNum, hateNum);
          
     }
     
     @Transactional
     public MeetingCreateResponseDto createMeeting(MeetingRequestDto requestDto) {
          User user = SecurityUtil.getCurrentUser();
          // user null일 경우 에러처리 추가 필요
          
          Meeting meeting = meetingRepository.saveAndFlush(new Meeting(requestDto, user));
          
          boolean isMaster = false;
          if (user.getId() == meeting.getUser().getId()) {
               isMaster = true;
          }
          
          int likeNum = 0;
//                reviewRepository.countByMeetingIdAndLikeIsTrue(meeting.getId()).orElse(0L);
          int hateNum = 0;
//                reviewRepository.countByMeetingIdAndLikeIsFalse(meeting.getId()).orElse(0L);
          
          return new MeetingCreateResponseDto(meeting, isMaster, likeNum, hateNum);
     }
     
     @Transactional
     public void updateAllMeeting(Long id, MeetingUpdateRequestDto requestDto) {
          User user = SecurityUtil.getCurrentUser();
          
          Meeting meeting = meetingRepository.findById(id).orElseThrow(() -> new RestApiException(Code.NO_MEETING));
          
          if (meeting.isDeleted()) {
               throw new RestApiException(Code.NO_MEETING);
          }
          
          if (user.getId() == meeting.getUser().getId()) {
               meeting.updateAll(requestDto);
          } else {
               throw new RestApiException(Code.INVALID_USER);
          }
     }
     
     @Transactional
     public void updateLink(Long id, MeetingLinkRequestDto requestDto) {
          User user = SecurityUtil.getCurrentUser();
          
          Meeting meeting = meetingRepository.findById(id).orElseThrow(() -> new RestApiException(Code.NO_MEETING));
          
          if (meeting.isDeleted()) {
               throw new RestApiException(Code.NO_MEETING);
          }
          
          if (user.getId() == meeting.getUser().getId()) {
               meeting.updateLink(requestDto);
          } else {
               throw new RestApiException(Code.INVALID_USER);
          }
     }
     
     @Transactional
     public void deleteMeeting(Long id) {
          User user = SecurityUtil.getCurrentUser();
          
          Meeting meeting = meetingRepository.findById(id).orElseThrow(() -> new RestApiException(Code.NO_MEETING));
          
          if (meeting.isDeleted()) {
               throw new RestApiException(Code.NO_MEETING);
          }
          
          if (user.getId() == meeting.getUser().getId()) {
               meeting.deleteMeeting();
          } else {
               throw new RestApiException(Code.INVALID_USER);
          }
          
     }
     
     // 모임 전체리스트 불러오기
     @Transactional (readOnly = true)
     public MeetingListResponseDto getMeetings(String sortBy, CategoryCode category, Long meetingIdx) {
          User user = SecurityUtil.getCurrentUser(); // 비회원일경우(토큰못받았을경우) null
          if (user == null) throw new RestApiException(Code.NOT_FOUND_AUTHORIZATION_IN_SECURITY_CONTEXT);
          
          MeetingListResponseDto response = new MeetingListResponseDto();
          // 참석 기능 구현 후 참석여부 추가필요
          List<Meeting> meetingList = (sortBy.equals("new")) ?
               meetingRepository.findAllSortByNewAndCategory(category, meetingIdx) // 신규순
               : meetingRepository.findAllSortByPopularAndCategory(category, meetingIdx); // 인기순
          
          List<MeetingListResponseDto.ResponseDto> responseDtoList = meetingList.stream()
               // meeting 작성자의 id와 로그인 유저의 아이디 비교
               .map(m -> new MeetingListResponseDto.ResponseDto(m, user.getId()))
               .collect(Collectors.toList());
          response.addMeetingList(responseDtoList);
          return response;
     }
     
}
