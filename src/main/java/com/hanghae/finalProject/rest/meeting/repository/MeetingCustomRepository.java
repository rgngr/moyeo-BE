package com.hanghae.finalProject.rest.meeting.repository;

import com.hanghae.finalProject.rest.meeting.model.Meeting;

import java.util.List;

public interface MeetingCustomRepository {
     
     // http://jojoldu.tistory.com/528 무한스크롤
     
     List<Meeting> findAllByOrderByIdDesc();
}
