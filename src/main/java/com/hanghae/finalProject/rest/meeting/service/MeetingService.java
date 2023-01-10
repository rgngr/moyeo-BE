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
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MeetingService {
     private final MeetingRepository meetingRepository;
     private final ReviewRepository reviewRepository;
     private final CalendarRepository calendarRepository;
     private final AlarmRepository alarmRepository;
     
     // 모임 상세조회
     @Transactional
     public MeetingDetailResponseDto getMeeting(Long id) {
          User user = SecurityUtil.getCurrentUser();
          // 비회원도 공유를 통해서 페이지를 볼 수 있어야 되니까 null 예외 처리 XX
          
          // 모임 존재여부 확인
          MeetingDetailResponseDto meetingDetailResponseDto = meetingRepository.findByIdAndAttendAndAlarmAndLike(id, user);
          if(meetingDetailResponseDto==null) throw new RestApiException(Code.NO_MEETING);
          
          if (user != null && Objects.equals(user.getId(), meetingDetailResponseDto.getMasterId())) {
               meetingDetailResponseDto.isMaster(true);
          }
          return meetingDetailResponseDto;
     }
     
     // 모임생성
     @Transactional
     public MeetingCreateResponseDto createMeeting(MeetingRequestDto requestDto) {
          User user = SecurityUtil.getCurrentUser();
          if (user == null) throw new RestApiException(Code.NOT_FOUND_AUTHORIZATION_IN_SECURITY_CONTEXT);
          
          Meeting meeting = meetingRepository.saveAndFlush(new Meeting(requestDto, user));
          
          boolean isMaster = false;
          if (user.getId() == meeting.getUser().getId()) {
               isMaster = true;
          }
          
          return new MeetingCreateResponseDto(meeting, isMaster);
     }
     
     // 모임수정
     @Transactional
     public void updateAllMeeting(Long id, MeetingUpdateRequestDto requestDto) {
          User user = SecurityUtil.getCurrentUser();
          if (user == null) throw new RestApiException(Code.NOT_FOUND_AUTHORIZATION_IN_SECURITY_CONTEXT);
          
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
     
     // 링크 업데이트
     @Transactional
     public void updateLink(Long id, MeetingLinkRequestDto requestDto) {
          User user = SecurityUtil.getCurrentUser();
          if (user == null) throw new RestApiException(Code.NOT_FOUND_AUTHORIZATION_IN_SECURITY_CONTEXT);
          
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
     
     // 모임 삭제
     @Transactional
     public void deleteMeeting(Long id) {
          User user = SecurityUtil.getCurrentUser();
          if (user == null) throw new RestApiException(Code.NOT_FOUND_AUTHORIZATION_IN_SECURITY_CONTEXT);
          
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
          List<MeetingListResponseDto.ResponseDto> responseDtoList = (sortBy.equals("new")) ? meetingRepository.findAllSortByNewAndCategory(category, meetingIdx) // 신규순
               : meetingRepository.findAllSortByPopularAndCategory(category, meetingIdx); // 인기순
          
          response.addMeetingList(responseDtoList.stream()
               // meeting 작성자의 id와 로그인 유저의 아이디 비교
               .peek(m -> {
                    // master 처리 ,attendantsNum 처리,
                    m.setMaster(m.getMasterId().equals(user.getId()));
                    if (m.getAttendantsList().size() == 1 && m.getAttendantsList().get(0).getUserId() == null) {
                         // getAttendantsList 안에 null인 경우도 넘어와서 객체 생겨버림
                         m.setAttendantsList(null);
                         m.setAttendantsNum(0);
                    } else {
                         m.setAttendantsNum(m.getAttendantsList().size());
                    }
               }).collect(Collectors.toList()));
          return response;
     }
     
     // 제목 검색 모임리스트 불러오기
     @Transactional (readOnly = true)
     public MeetingListResponseDto getMeetingsBySearch(String search, CategoryCode category, Long meetingId) {
          User user = SecurityUtil.getCurrentUser(); // 비회원일경우(토큰못받았을경우) null
          if (user == null) throw new RestApiException(Code.NOT_FOUND_AUTHORIZATION_IN_SECURITY_CONTEXT);
          
          MeetingListResponseDto response = new MeetingListResponseDto();
          response.addMeetingList(meetingRepository.findAllBySearchAndCategory(search, category, meetingId).stream()
               // meeting 작성자의 id와 로그인 유저의 아이디 비교
               .peek(m -> {
                    // master 처리 ,attendantsNum 처리,
                    m.setMaster(m.getMasterId().equals(user.getId()));
                    if (m.getAttendantsList().size() == 1 && m.getAttendantsList().get(0).getUserId() == null) {
                         // getAttendantsList 안에 null인 경우도 넘어와서 객체 생겨버림
                         m.setAttendantsList(null);
                         m.setAttendantsNum(0);
                    } else {
                         // 로그인유저의 참석유무
                         m.setAttend(m.getAttendantsList().stream().anyMatch(a -> a.getUserId().equals(user.getId())));
                         m.setAttendantsNum(m.getAttendantsList().size());
                    }
               }).collect(Collectors.toList()));
          return response;
          
     }
}
