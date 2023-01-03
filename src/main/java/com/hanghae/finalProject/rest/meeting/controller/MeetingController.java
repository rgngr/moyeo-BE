package com.hanghae.finalProject.rest.meeting.controller;

import com.hanghae.finalProject.config.dto.DataResponseDto;
import com.hanghae.finalProject.config.dto.ResponseDto;
import com.hanghae.finalProject.config.errorcode.Code;
import com.hanghae.finalProject.rest.meeting.dto.MeetingLinkRequestDto;
import com.hanghae.finalProject.rest.meeting.dto.MeetingRequestDto;
import com.hanghae.finalProject.rest.meeting.dto.MeetingUpdateRequestDto;
import com.hanghae.finalProject.rest.meeting.service.MeetingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MeetingController {

    private final MeetingService  meetingService;

    @GetMapping("/meetings/{id}")
    public ResponseDto getMeeting(@PathVariable Long id) {
        return DataResponseDto.of(meetingService.getMeeting(id));
    }

    @PostMapping("/meetings")
    public ResponseDto createMeeting(@RequestBody @Valid MeetingRequestDto requestDto) {
        return DataResponseDto.of(meetingService.createMeeting(requestDto), Code.CREATE_MEETING.getStatusMsg());
    }

    @PatchMapping("/meetings/{id}")
    public ResponseDto updateAllMeeting(@PathVariable Long id, @RequestBody @Valid MeetingUpdateRequestDto requestDto) {
        return ResponseDto.of(true, Code.UPDATE_MEETING);
    }

    @PatchMapping("/meetings/{id}/link")
    public ResponseDto updateLink(@PathVariable Long id, @RequestBody @Valid MeetingLinkRequestDto requestDto) {
        return ResponseDto.of(true, Code.UPDATE_LINK);
    }

    @DeleteMapping("/meetings/{id}")
    public ResponseDto deleteMeeting(@PathVariable Long id) {
        return ResponseDto.of(true, Code.DELETE_MEETING);
    }



    //     public ResponseDto signup(@RequestBody @Valid SignupRequestDto requestDto) {
//          userService.signUp(requestDto);
//          // 1. data o , msg o
////          return DataResponseDto.of("data test", "test 성공"); //data있고 별도 msg보낼 경우
//          // 2. data o msg 정상
////          return DataResponseDto.of("data test");
//          // 3. DATA X, MSG 따로
//          return ResponseDto.of(true, Code.USER_SIGNUP_SUCCESS);
//     }

}
