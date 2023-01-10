package com.hanghae.finalProject.rest.follow.controller;


import com.hanghae.finalProject.config.dto.DataResponseDto;
import com.hanghae.finalProject.config.dto.ResponseDto;
import com.hanghae.finalProject.config.errorcode.Code;
import com.hanghae.finalProject.rest.follow.service.FollowService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping ("/api/follow/{followId}")
public class FollowController {
     private final FollowService followService;
     
     @Operation (summary = "팔로우/언팔로우")
     @PostMapping ("")
     public ResponseDto follow(@PathVariable Long followId) {
          return ResponseDto.of(true, followService.follow(followId));
     }
}
