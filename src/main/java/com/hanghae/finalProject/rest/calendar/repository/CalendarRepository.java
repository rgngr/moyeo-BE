package com.hanghae.finalProject.rest.calendar.repository;

import com.hanghae.finalProject.rest.calendar.model.Calendar;
import com.hanghae.finalProject.rest.meeting.model.Meeting;
import com.hanghae.finalProject.rest.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface CalendarRepository extends JpaRepository<Calendar, Long>, CalendarCustomRepository {
}
