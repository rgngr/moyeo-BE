package com.hanghae.finalProject.rest.meeting.service;

import com.hanghae.finalProject.config.errorcode.Code;
import com.hanghae.finalProject.config.exception.RestApiException;
import com.hanghae.finalProject.config.util.RedisUtil;
import com.hanghae.finalProject.config.util.SecurityUtil;
import com.hanghae.finalProject.rest.alarm.repository.AlarmRepository;
import com.hanghae.finalProject.rest.alarm.service.AlarmService;
import com.hanghae.finalProject.rest.attendant.model.Attendant;
import com.hanghae.finalProject.rest.attendant.repository.AttendantRepository;
import com.hanghae.finalProject.rest.calendar.repository.CalendarRepository;
import com.hanghae.finalProject.rest.follow.repository.FollowRepository;
import com.hanghae.finalProject.rest.meeting.dto.*;
import com.hanghae.finalProject.rest.meeting.model.CategoryCode;
import com.hanghae.finalProject.rest.meeting.model.Meeting;
import com.hanghae.finalProject.rest.meeting.repository.MeetingRepository;
import com.hanghae.finalProject.rest.review.repository.ReviewRepository;
import com.hanghae.finalProject.rest.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
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
     private final AttendantRepository attendantRepository;
     private final FollowRepository followRepository;
     private final AlarmService alarmService;
     @Autowired
     private ApplicationContext applicationContext;
     
     // 모임 상세조회
     @Transactional
     public MeetingDetailResponseDto getMeeting(Long id) {
          User user = SecurityUtil.getCurrentUser();
          // 비회원도 공유를 통해서 페이지를 볼 수 있어야 되니까 null 예외 처리 XX
          
          // 모임 존재여부 확인
          MeetingDetailResponseDto meetingDetailResponseDto = meetingRepository.findByIdAndAttendAndAlarmAndLike(id, user);
          if (meetingDetailResponseDto == null) throw new RestApiException(Code.NO_MEETING);
          
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
          // 비밀방일경우 비번4글자 확인
          if (requestDto.isSecret()) {
               if (requestDto.getPassword().length() != 4) {
                    throw new RestApiException(Code.WRONG_SECRET_PASSWORD);
               }
          }
          Meeting meeting = meetingRepository.saveAndFlush(new Meeting(requestDto, user));
          
          // 참석자리스트에 방장 추가
          Attendant attendant = new Attendant(meeting, user);
          attendantRepository.save(attendant);
          
          return new MeetingCreateResponseDto(meeting);
     }
     
     // 모임수정
     @Transactional
     public void updateAllMeeting(Long id, MeetingUpdateRequestDto requestDto) {
          User user = SecurityUtil.getCurrentUser();
          if (user == null) throw new RestApiException(Code.NOT_FOUND_AUTHORIZATION_IN_SECURITY_CONTEXT);
          // 비밀방일경우 비번4글자 확인
          if (requestDto.isSecret()) {
               if (requestDto.getPassword().length() != 4) {
                    throw new RestApiException(Code.WRONG_SECRET_PASSWORD);
               }
          }
          Meeting meeting = meetingRepository.findById(id).orElseThrow(() -> new RestApiException(Code.NO_MEETING));
          LocalDateTime dateOrigin = meeting.getStartTime();
          if (meeting.isDeleted()) {
               throw new RestApiException(Code.NO_MEETING);
          }
          
          if (user.getId() == meeting.getUser().getId()) {
               meeting.updateAll(requestDto);
               List<Attendant> attendantList = attendantRepository.findAllByMeetingId(meeting.getId()).stream()
                    // 캘린더 캐시데이터 삭제
                    .peek(
                         a -> getSpringProxy().deleteCache(a.getUser().getId(), dateOrigin.getYear(), dateOrigin.getMonthValue())
                    ).collect(Collectors.toList());
               // 알림보내기
               alarmService.alarmUpdateMeeting(meeting);
          } else {
               throw new RestApiException(Code.INVALID_USER);
          }
     }
     
     // GET 모임수정페이지
     @Transactional (readOnly = true)
     public MeetingUpdatePageResponseDto getUpdatePage(Long id) {
          User user = SecurityUtil.getCurrentUser();
          if (user == null) throw new RestApiException(Code.NOT_FOUND_AUTHORIZATION_IN_SECURITY_CONTEXT);
          
          Meeting meeting = meetingRepository.findById(id).orElseThrow(() -> new RestApiException(Code.NO_MEETING));
          
          if (meeting.isDeleted()) {
               throw new RestApiException(Code.NO_MEETING);
          }
          
          if (user.getId() == meeting.getUser().getId()) {
               return new MeetingUpdatePageResponseDto(meeting);
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
               alarmService.alarmUpdateLink(meeting);
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
               alarmService.alarmDeleteMeeting(meeting);
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
          List<MeetingListResponseDto.ResponseDto> responseDtoList = (sortBy.equals("new")) ?
               meetingRepository.findAllSortByNewAndCategory(category, meetingIdx) // 신규순
               : meetingRepository.findAllSortByPopularAndCategory(category, meetingIdx); // 인기순
          
          responseDtoList = responseDtoList.stream()
               // meeting 작성자의 id와 로그인 유저의 아이디 비교
               .peek(m -> {
                    // master 처리
                    m.setMaster(m.getMasterId().equals(user.getId()));
                    // attendantsNum 처리,
                    // getAttendantsList 안에 null인 경우도 넘어와서 객체 생겨버림
                    if (m.getAttendantsList().size() == 1 && m.getAttendantsList().get(0).getUserId() == null) {
                         m.setAttendantsList(null);
                         m.setAttendantsNum(0);
                    } else {
                         // 총 참석인원
                         m.setAttendantsNum(m.getAttendantsList().size());
                         // 참석자리스트에 로그인유저가 있는가
                         m.setAttend(m.getAttendantsList().stream().anyMatch(a -> a.getUserId().equals(user.getId())));
                    }
               }).collect(Collectors.toList());
          
          // 인기순일 경우 : 재정렬 필요 > 인기순 + 마감날짜빠른순 + 최신순
          if (sortBy.equals("popular")) {
               responseDtoList = responseDtoList.stream().sorted(Comparator.comparing(MeetingListResponseDto.ResponseDto::getAttendantsNum).reversed()
                    .thenComparing(MeetingListResponseDto.ResponseDto::getStartTime).reversed()
                    .thenComparing(MeetingListResponseDto.ResponseDto::getId).reversed()).collect(Collectors.toList()
               );
          }
          
          return response.addMeetingList(responseDtoList);
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
     
     private RedisUtil getSpringProxy() {
          return applicationContext.getBean(RedisUtil.class);
     }
}
