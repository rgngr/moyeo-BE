package com.hanghae.finalProject.rest.attendant.controller;

import com.hanghae.finalProject.config.dto.DataResponseDto;
import com.hanghae.finalProject.config.dto.ResponseDto;
import com.hanghae.finalProject.rest.attendant.service.AttendantService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/meetings/{meetingId}")
public class AttendantController {
    private final AttendantService attendantService;
//    private final AttendantMessageConsumers attendantMessageConsumers;

    @Operation(summary = "모임 참석/취소")
    @PatchMapping("/attendance")
    public ResponseDto addAttendant(@PathVariable Long meetingId) {
        
        return DataResponseDto.of(attendantService.syncAddAttendant(meetingId));
    }

    @Operation(summary = "모임 참석자 리스트")
    @GetMapping("/attendants")
    public ResponseDto getAttendantList(@PathVariable Long meetingId) {
        return DataResponseDto.of(attendantService.getAttendantList(meetingId));
    }

    @Operation(summary = "모임 입장")
    @PatchMapping("/entrance")
    public ResponseDto enter(@PathVariable Long meetingId) {
        return ResponseDto.of(true, attendantService.enter(meetingId));
    }
}
