package com.hanghae.finalProject.rest.meeting.service;

import com.hanghae.finalProject.config.controller.errorcode.Code;
import com.hanghae.finalProject.config.exception.RestApiException;
import com.hanghae.finalProject.config.util.SecurityUtil;
import com.hanghae.finalProject.rest.alarm.repository.AlarmRepository;
import com.hanghae.finalProject.rest.calendar.model.Calendar;
import com.hanghae.finalProject.rest.calendar.repository.CalendarRepository;
import com.hanghae.finalProject.rest.meeting.dto.*;
import com.hanghae.finalProject.rest.meeting.model.Meeting;
import com.hanghae.finalProject.rest.meeting.repository.MeetingRepository;
import com.hanghae.finalProject.rest.review.repository.ReviewRepository;
import com.hanghae.finalProject.rest.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        Meeting meeting = meetingRepository.findById(id).orElseThrow(
                () -> new RestApiException(Code.NO_MEETING)
        );

        if (meeting.isDeleted()) {
            throw new RestApiException(Code.NO_MEETING);
        }

        boolean isMaster = false;
        if(user.getId() == meeting.getUser().getId()) {
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
        if(user.getId() == meeting.getUser().getId()) {
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

        Meeting meeting = meetingRepository.findById(id).orElseThrow(
                () -> new RestApiException(Code.NO_MEETING)
        );

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

        Meeting meeting = meetingRepository.findById(id).orElseThrow(
                () -> new RestApiException(Code.NO_MEETING)
        );

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

        Meeting meeting = meetingRepository.findById(id).orElseThrow(
                () -> new RestApiException(Code.NO_MEETING)
        );

        if (meeting.isDeleted()) {
            throw new RestApiException(Code.NO_MEETING);
        }

        if (user.getId() == meeting.getUser().getId()) {
            meeting.deleteMeeting();
        } else {
            throw new RestApiException(Code.INVALID_USER);
        }

    }

//     @Transactional
//     public PostResponseDto.createResponse createPost(PostRequestDto postRequestDto) {
//          User user = SecurityUtil.getCurrentUser();
//          MultipartFile file = postRequestDto.getFile();
//          String imgUrl = null;
//          if(file != null && file.getContentType() != null) {
//               imgUrl = s3Uploader.upload(file, "postImage");
//          }
//          Post post = postRepository.saveAndFlush(new Post(postRequestDto, user.getUsername(), imgUrl));
//          return new PostResponseDto.createResponse(post, user.getNickname());
//     }
}
