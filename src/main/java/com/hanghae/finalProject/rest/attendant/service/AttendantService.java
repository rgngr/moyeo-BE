package com.hanghae.finalProject.rest.attendant.service;

import com.hanghae.finalProject.config.errorcode.Code;
import com.hanghae.finalProject.config.exception.RestApiException;
import com.hanghae.finalProject.config.util.SecurityUtil;
import com.hanghae.finalProject.rest.alarm.dto.AlarmResponseDto;
import com.hanghae.finalProject.rest.alarm.model.Alarm;
import com.hanghae.finalProject.rest.alarm.repository.AlarmRepository;
import com.hanghae.finalProject.rest.attendant.dto.AttendantResponseDto;
import com.hanghae.finalProject.rest.attendant.dto.AttendantListResponseDto;
import com.hanghae.finalProject.rest.attendant.model.Attendant;
import com.hanghae.finalProject.rest.attendant.repository.AttendantRepository;
import com.hanghae.finalProject.rest.meeting.model.Meeting;
import com.hanghae.finalProject.rest.meeting.repository.MeetingRepository;
import com.hanghae.finalProject.rest.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AttendantService {
     
     private final AttendantRepository attendantRepository;
     private final MeetingRepository meetingRepository;
     private final AlarmRepository alarmRepository;
     
     // 모임 참석/취소
     @Transactional
     public AttendantResponseDto addAttendant(Long meetingId) {
          User user = SecurityUtil.getCurrentUser();
          if (user == null) throw new RestApiException(Code.NOT_FOUND_AUTHORIZATION_IN_SECURITY_CONTEXT);
          
          Meeting meeting = meetingRepository.findByIdAndDeletedIsFalse(meetingId).orElseThrow(
               () -> new RestApiException(Code.NO_MEETING)
          );
          // 최대정원 도달시 참석불가
          List<Attendant> attendantList = attendantRepository.findAllByMeetingId(meetingId);
          if (meeting.getMaxNum() <= attendantList.size()) {
               throw new RestApiException(Code.NO_MORE_SEAT);
          }
          Attendant oriAttendant = attendantRepository.findByMeetingIdAndUser(meetingId, user).orElseGet(new Attendant());
          if (oriAttendant== null) {
               // 참석하지 않은 유저인 경우 참석으로
               Attendant attendant = attendantRepository.save(new Attendant(meeting, user));
               // 참석시 알람받기가 기본임
               alarmRepository.save(new Alarm(user, meeting));
               return new AttendantResponseDto(attendant);
          } else {
               // 기존에 참석했던 유저의 경우
               oriAttendant.cancelAttendant(meeting);
               // 알람받기 리스트에 있을경우 알람 삭제필요
               Alarm alarm = alarmRepository.findByMeetingIdAndUser(meeting.getId(), user).orElseGet(new Alarm());
               if(alarm!=null){
                    alarmRepository.delete(alarm);
               }
               // 참석자 명단에서 삭제
               attendantRepository.delete(oriAttendant);
               return null;
          }
     }
     
     // 모임 참석자 리스트 조회
     @Transactional (readOnly = true)
     public List<AttendantListResponseDto> getAttendantList(Long meetingId) {
          User user = SecurityUtil.getCurrentUser();
          // 링크공유받은 비회원유저도 요청들어옴. exception 처리 x
          // 팔로우여부도 같이 들고오기
          List<AttendantListResponseDto> attendants = attendantRepository.findByMeetingIdAndFollow(meetingId, user);
          
          return attendants;
     }
     
     // 모임 입장
     @Transactional
     public Code enter(Long meetingId) {
          User user = SecurityUtil.getCurrentUser();
          if (user == null) throw new RestApiException(Code.NOT_FOUND_AUTHORIZATION_IN_SECURITY_CONTEXT);
          
          // 존재하는 모임인가
          Meeting meeting = meetingRepository.findByIdAndDeletedIsFalse(meetingId).orElseThrow(
               () -> new RestApiException(Code.NO_MEETING)
          );
          // 참석하기로한 모임인가
          Attendant attendant = attendantRepository.findByMeetingIdAndUser(meetingId, user).orElseGet(new Attendant());
          if(attendant==null){
               // 참석하기 누르지않은 모임일 경우
               throw new RestApiException(Code.NOT_ATTENDANCE_YET);
          }
          attendant.enter(meeting);
          attendantRepository.save(attendant);
          return Code.CREATE_ENTER;
     }
     
     @Transactional
     public AlarmResponseDto getAlarm(Long meetingId) {
          User user = SecurityUtil.getCurrentUser();
          if (user == null) throw new RestApiException(Code.NOT_FOUND_AUTHORIZATION_IN_SECURITY_CONTEXT);
          
          // 모임존재유무 확인
          Meeting meeting = meetingRepository.findByIdAndDeletedIsFalse(meetingId).orElseThrow(
               () -> new RestApiException(Code.NO_MEETING)
          );
          // 모임에 참석하기로한 유저인가
          if(!attendantRepository.existsByMeetingAndUser(meeting, user)){
               throw new RestApiException(Code.NOT_ATTENDANCE_YET);
          }
          Alarm alarm = alarmRepository.findByMeetingIdAndUser(meeting.getId(), user).orElseGet(new Alarm());
          
          if (alarm == null) {
               // 알람 안받기로한 사람이었던 경우 알람받기 추가
               alarm = alarmRepository.save(new Alarm(user, meeting));
               return new AlarmResponseDto(alarm.getUser().getId(), alarm.getMeeting().getId());
          } else {
               // 알람받기로한 사람인 경우 알림 음소거로 변경
               alarmRepository.delete(alarm);
               return null;
          }
     }
     
}
