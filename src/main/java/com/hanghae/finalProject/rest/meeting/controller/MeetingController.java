package com.hanghae.finalProject.rest.meeting.controller;

import com.hanghae.finalProject.config.errorcode.Code;
import com.hanghae.finalProject.config.dto.DataResponseDto;
import com.hanghae.finalProject.config.dto.ResponseDto;
import com.hanghae.finalProject.rest.meeting.dto.MeetingLinkRequestDto;
import com.hanghae.finalProject.rest.meeting.dto.MeetingRequestDto;
import com.hanghae.finalProject.rest.meeting.dto.MeetingUpdateRequestDto;
import com.hanghae.finalProject.rest.meeting.model.CategoryCode;
import com.hanghae.finalProject.rest.meeting.service.MeetingService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/meetings")
public class MeetingController {

    private final MeetingService  meetingService;
    
    @ApiOperation (value = "모임 상세 조회")
    @GetMapping("/{id}")
    public ResponseDto getMeeting(@PathVariable Long id) {
        return DataResponseDto.of(meetingService.getMeeting(id));
    }
    
    @ApiOperation (value = "모임 전체 조회")
    @GetMapping("")
    public ResponseDto getMeetings(
         @RequestParam(value="sortby", defaultValue = "popular", required = false) String sortBy,
         @RequestParam(value="category", required = false) CategoryCode category,
         @RequestParam(value="meetingId", required = false) Long meetingId
    ){
        return DataResponseDto.of(meetingService.getMeetings(sortBy, category, meetingId));
    }
    
    @ApiOperation (value = "모임명 검색")
    @GetMapping("/search")
    public ResponseDto getMeetingsBySearch(
         @RequestParam(value="searchBy", defaultValue = "", required = false) String search,
         @RequestParam(value="category", required = false) CategoryCode category,
         @RequestParam(value="meetingId", required = false) Long meetingId
    ){
        return DataResponseDto.of(meetingService.getMeetingsBySearch(search, category, meetingId));
    }
    
    @ApiOperation (value = "모임 생성")
    @PostMapping("")
    public ResponseDto createMeeting(@RequestBody @Valid MeetingRequestDto requestDto) {
        return DataResponseDto.of(meetingService.createMeeting(requestDto), Code.CREATE_MEETING.getStatusMsg());
    }
    
    @ApiOperation (value = "모임 수정")
    @PatchMapping("/{id}")
    public ResponseDto updateAllMeeting(@PathVariable Long id, @RequestBody @Valid MeetingUpdateRequestDto requestDto) {
        meetingService.updateAllMeeting(id,requestDto);
        return ResponseDto.of(true, Code.UPDATE_MEETING);
    }
    
    @ApiOperation (value = "모임 링크 수정")
    @PatchMapping("/{id}/link")
    public ResponseDto updateLink(@PathVariable Long id, @RequestBody @Valid MeetingLinkRequestDto requestDto) {
        meetingService.updateLink(id, requestDto);
        return ResponseDto.of(true, Code.UPDATE_LINK);
    }
    
    @ApiOperation (value = "모임 삭제")
    @DeleteMapping("/{id}")
    public ResponseDto deleteMeeting(@PathVariable Long id) {
        meetingService.deleteMeeting(id);
        return ResponseDto.of(true, Code.DELETE_MEETING);
    }
    
    @ApiOperation (value = "나의 월별 캘린더 불러오기")
    @GetMapping("/mine")
    public ResponseDto getMyMeetings(
         @RequestParam(value="year", required = false) Long year,
         @RequestParam(value="month", required = false) Long month
    ){
        log.info(String.valueOf(LocalDateTime.now().getMonth()));
        return DataResponseDto.of(meetingService.getMyMeetings(year, month));
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
