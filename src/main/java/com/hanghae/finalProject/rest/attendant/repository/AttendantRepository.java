package com.hanghae.finalProject.rest.attendant.repository;

import com.hanghae.finalProject.rest.attendant.model.Attendant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendantRepository extends JpaRepository<Attendant, Long> {
}