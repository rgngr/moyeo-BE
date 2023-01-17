package com.hanghae.finalProject.rest.dropMember.controller;

import com.hanghae.finalProject.config.dto.ResponseDto;
import com.hanghae.finalProject.config.errorcode.Code;
import com.hanghae.finalProject.rest.dropMember.service.DropMemberService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping ("/api/meetings/{meetingId}")
public class DropMemberController {
     
     private final DropMemberService dropMemberService;
     
     @Operation (summary = "참석자 추방")
     @PostMapping ("/drop/{memberId}")
     public ResponseDto dropMember(@PathVariable Long meetingId, @PathVariable Long memberId) {
          dropMemberService.dropMember(meetingId, memberId);
          return ResponseDto.of(true, Code.OK);
     }
}
