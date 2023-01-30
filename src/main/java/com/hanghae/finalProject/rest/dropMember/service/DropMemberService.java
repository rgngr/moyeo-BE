package com.hanghae.finalProject.rest.dropMember.service;

import com.hanghae.finalProject.config.errorcode.Code;
import com.hanghae.finalProject.config.exception.RestApiException;
import com.hanghae.finalProject.config.util.SecurityUtil;
import com.hanghae.finalProject.rest.attendant.model.Attendant;
import com.hanghae.finalProject.rest.attendant.repository.AttendantRepository;
import com.hanghae.finalProject.rest.dropMember.model.DropMember;
import com.hanghae.finalProject.rest.dropMember.repository.DropMemberRepository;
import com.hanghae.finalProject.rest.meeting.model.Meeting;
import com.hanghae.finalProject.rest.meeting.repository.MeetingRepository;
import com.hanghae.finalProject.rest.user.model.User;
import com.hanghae.finalProject.rest.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class DropMemberService {
     private final UserRepository userRepository;
     private final DropMemberRepository dropMemberRepository;
     private final MeetingRepository meetingRepository;
     private final AttendantRepository attendantRepository;
     
     // 그룹장이 참석자 추방하기
     @Transactional
     public void dropMember(Long meetingId, Long memberId) {
          User user = SecurityUtil.getCurrentUser();
          if (user == null) throw new RestApiException(Code.NOT_FOUND_AUTHORIZATION_IN_SECURITY_CONTEXT);
          
          // 존재하는 유저인가
          User droppedUser = userRepository.findById(memberId).orElseThrow(() -> new RestApiException(Code.NO_USER));
          
          // 로그인유저가 방장이어야 함 & 강퇴할 유저가 방장이 아니어야 함
          Meeting meeting = meetingRepository.findById(meetingId).orElseThrow(() -> new RestApiException(Code.NO_MEETING));
          if (!Objects.equals(meeting.getUser().getId(), user.getId()) || Objects.equals(meeting.getUser().getId(), memberId))
               throw new RestApiException(Code.INVALID_USER_DELETE);
          
          // 추방하려는 멤버가 참석자리스트에 있어야 함
          Attendant attendant = attendantRepository.findByMeetingAndUser(meeting, droppedUser)
               .orElseThrow(() -> new RestApiException(Code.NO_USER));
          
          // 추방테이블에 추가 (기존에 없었다면)
          if (!dropMemberRepository.existsByMeetingAndUser(meeting, droppedUser)) {
               dropMemberRepository.save(new DropMember(meeting, attendant.getUser()));
          }
          // 참석자리스트에서 삭제
          attendantRepository.delete(attendant);
          meeting.cancelAttend();
     }
}
