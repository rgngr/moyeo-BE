package com.hanghae.finalProject.rest.meeting.repository;

import com.hanghae.finalProject.rest.meeting.model.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface MeetingRepository extends JpaRepository<Meeting, Long>, MeetingCustomRepository {
     
     
     Optional<Meeting> findByIdAndDeletedIsFalse(Long meetingId);

    List<Meeting> findAllByStartDate(LocalDate today);
}