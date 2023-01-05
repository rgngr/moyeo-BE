package com.hanghae.finalProject.rest.meeting.repository;

import com.hanghae.finalProject.rest.meeting.model.CategoryCode;
import com.hanghae.finalProject.rest.meeting.model.Meeting;

import java.util.List;

public interface MeetingCustomRepository {
     
     // 신규순 + 카테고리
     List<Meeting> findAllSortByNewAndCategory(CategoryCode category, Long meetingIdx);
     
     // 인기순 + 카테고리
     List<Meeting> findAllSortByPopularAndCategory(CategoryCode category, Long meetingIdx);
     
     // 검색 리스트 + 카테고리
     List<Meeting> findAllBySearchAndCategory(String search, CategoryCode category, Long meetingId);
     
}
