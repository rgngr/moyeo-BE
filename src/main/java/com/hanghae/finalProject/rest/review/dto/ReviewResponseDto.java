package com.hanghae.finalProject.rest.review.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReviewResponseDto {
     public Long reviewId;
     public String username;
     public Boolean like;
}
