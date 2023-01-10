package com.hanghae.finalProject.rest.attendant.repository;

import com.hanghae.finalProject.rest.attendant.model.Attendant;
import com.hanghae.finalProject.rest.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AttendantRepository extends JpaRepository<Attendant, Long>, AttendantCustomRepository {
    Optional<Attendant> findByMeetingIdAndUser(Long meetingId, User user);
     List<Attendant> findAllByMeetingId(Long meetingId);
     
}