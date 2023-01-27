package com.hanghae.finalProject.rest.alarm.repository;

import com.hanghae.finalProject.rest.alarm.model.AlarmList;
import com.hanghae.finalProject.rest.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlarmListRepository extends JpaRepository<AlarmList, Long> {

    List<AlarmList> findAllByUserOrderByCreatedAtDesc(User user);
}
