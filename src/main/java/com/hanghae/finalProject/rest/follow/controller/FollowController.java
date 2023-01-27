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
@RequestMapping ("/api/follow/")
public class FollowController {
     private final FollowService followService;
     
     @Operation (summary = "팔로우/언팔로우")
     @PostMapping ("{followId}")
     public ResponseDto follow(@PathVariable Long followId) {
          return ResponseDto.of(true, followService.follow(followId));
     }
     @Operation (summary = "내 팔로잉 리스트 불러오기")
     @GetMapping ("followingList")
     public ResponseDto followingList() {
          return DataResponseDto.of(followService.followingList(),Code.FOLLOWING_LIST_LOED.getStatusMsg());
     }
     @Operation (summary = "내 팔로워 리스트 불러오기")
     @GetMapping ("followerList")
     public ResponseDto followerList() {
          return DataResponseDto.of(followService.followerList(),Code.FOLLOWERS_LIST_LOED.getStatusMsg());
     }
}
