package com.hanghae.finalProject.rest.meeting.repository;

import com.hanghae.finalProject.rest.meeting.model.CategoryCode;
import com.hanghae.finalProject.rest.meeting.model.Meeting;

import java.util.List;

public interface MeetingCustomRepository {
     
     // http://jojoldu.tistory.com/528 무한스크롤
     
     // 무한스크롤 o, 카테고리 o , sortby x,
     List<Meeting> findAllSortByNewOrderByIdDesc(CategoryCode category, Long meetingIdx);
     
     List<Meeting> findAllSortByPopularOrderByIdDesc(CategoryCode category, Long meetingIdx);
     
}
