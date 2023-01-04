package com.hanghae.finalProject.rest.meeting.repository;

import com.hanghae.finalProject.rest.meeting.model.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Repository
public interface MeetingRepository extends JpaRepository<Meeting, Long>, MeetingRepositoryCustom {
    Optional<Meeting> findByIdAndDeletedIsFalse(Long meetingId);
    
    // http://jojoldu.tistory.com/528 무한스크롤
     List<Meeting> findAllByOrderByIdDesc();
}