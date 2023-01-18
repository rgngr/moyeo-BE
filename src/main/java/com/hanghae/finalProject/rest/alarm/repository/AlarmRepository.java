package com.hanghae.finalProject.rest.alarm.repository;

import com.hanghae.finalProject.rest.alarm.model.Alarm;
import com.hanghae.finalProject.rest.user.model.User;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {
    boolean existsByMeetingIdAndUser(Long meetingId, User user);
    
    Optional<Alarm> findByMeetingIdAndUser(Long meetingId, User user);

    boolean existsByMeetingIdAndUserId(Long id, Long attendantId);

    List<Alarm> findAllByMeetingId(Long id);
}