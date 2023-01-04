package com.hanghae.finalProject.rest.alarm.repository;

import com.hanghae.finalProject.rest.alarm.model.Alarm;
import com.hanghae.finalProject.rest.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {
    boolean existsByMeetingIdAndUser(Long id, User user);
}