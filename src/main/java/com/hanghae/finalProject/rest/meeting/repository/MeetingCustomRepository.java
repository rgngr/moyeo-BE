package com.hanghae.finalProject.rest.meeting.repository;

import com.hanghae.finalProject.rest.meeting.dto.MeetingListResponseDto;
import com.hanghae.finalProject.rest.meeting.model.CategoryCode;
import com.hanghae.finalProject.rest.meeting.model.Meeting;

import java.util.List;

public interface MeetingCustomRepository {
     
     // 신규순 + 카테고리
     List<Meeting> findAllSortByNewAndCategory3(CategoryCode category, Long meetingIdx);
     List<MeetingListResponseDto.ResponseDto> findAllSortByNewAndCategory(CategoryCode category, Long meetingIdx);
     
     // 인기순 + 카테고리
     List<Meeting> findAllSortByPopularAndCategory3(CategoryCode category, Long meetingIdx);
     List<MeetingListResponseDto.ResponseDto> findAllSortByPopularAndCategory(CategoryCode category, Long meetingIdx);
     
     // 검색 리스트 + 카테고리
     List<Meeting> findAllBySearchAndCategory3(String search, CategoryCode category, Long meetingId);
     
}
