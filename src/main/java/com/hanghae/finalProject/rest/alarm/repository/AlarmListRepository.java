package com.hanghae.finalProject.rest.alarm.repository;

import com.hanghae.finalProject.rest.alarm.model.AlarmList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlarmListRepository extends JpaRepository<AlarmList, Long> {
}
