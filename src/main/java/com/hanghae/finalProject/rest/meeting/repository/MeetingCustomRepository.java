package com.hanghae.finalProject.rest.meeting.repository;

import com.hanghae.finalProject.rest.meeting.dto.MeetingDetailResponseDto;
import com.hanghae.finalProject.rest.meeting.dto.MeetingListResponseDto;
import com.hanghae.finalProject.rest.meeting.model.CategoryCode;
import com.hanghae.finalProject.rest.user.model.User;

import java.util.List;

public interface MeetingCustomRepository {
     
     // 신규순 + 카테고리
     List<MeetingListResponseDto.ResponseDto> findAllSortByNewAndCategory(CategoryCode category, Long meetingId);
     
     // 인기순 + 카테고리
     List<MeetingListResponseDto.ResponseDto> findAllSortByPopularAndCategory(CategoryCode category, Long pageNum);
     
     // 검색 리스트 + 카테고리
     List<MeetingListResponseDto.ResponseDto> findAllBySearchAndCategory(String search, CategoryCode category, Long meetingId);
     
     MeetingDetailResponseDto findByIdAndAttendAndAlarmAndLike(Long id, User user);
}
