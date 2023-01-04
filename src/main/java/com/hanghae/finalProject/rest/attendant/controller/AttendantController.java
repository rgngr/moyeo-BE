package com.hanghae.finalProject.rest.attendant.controller;

import com.hanghae.finalProject.config.dto.DataResponseDto;
import com.hanghae.finalProject.config.dto.ResponseDto;
import com.hanghae.finalProject.config.errorcode.Code;
import com.hanghae.finalProject.rest.attendant.service.AttendantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Tag(name = "attendant", description = "모임 참석 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/meetings/{meetingId}")
public class AttendantController {
    private final AttendantService attendantService;

    @Operation(summary = "모임 참석/취소")
    @PostMapping("/attendance")
    public ResponseDto addAttendant(@PathVariable Long meetingId) {
        return DataResponseDto.of(attendantService.addAttendant(meetingId));
    }
}
