package com.hanghae.finalProject.rest.review.repository;

import com.hanghae.finalProject.rest.meeting.model.Meeting;
import com.hanghae.finalProject.rest.review.model.Review;
import com.hanghae.finalProject.rest.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
     Boolean existsByUserAndMeeting(User user, Meeting meeting);
}