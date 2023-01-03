package com.hanghae.finalProject.rest.review.repository;

import com.hanghae.finalProject.rest.review.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Optional<Long> countByMeetingIdAndReviewIsTrue(Long id);

    Optional<Long> countByMeetingIdAndReviewIsFalse(Long id);
}