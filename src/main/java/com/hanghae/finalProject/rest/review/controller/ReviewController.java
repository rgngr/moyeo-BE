package com.hanghae.finalProject.rest.review.controller;

import com.hanghae.finalProject.config.dto.DataResponseDto;
import com.hanghae.finalProject.config.dto.ResponseDto;
import com.hanghae.finalProject.rest.review.dto.ReviewRequestDto;
import com.hanghae.finalProject.rest.review.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping ("/api/meetings/{meetingId}")
public class ReviewController {
     
     private final ReviewService reviewService;
     
     @Operation (summary = "후기 작성")
     @PostMapping ("/review")
     public ResponseDto createReview(@PathVariable Long meetingId, @RequestBody ReviewRequestDto review) {
          return DataResponseDto.of(reviewService.createReview(meetingId, review));
     }
}
