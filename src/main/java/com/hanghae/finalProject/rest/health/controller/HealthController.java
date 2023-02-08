package com.hanghae.finalProject.rest.health.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping ("/")
public class HealthController {
     @ApiOperation (value = "모임 상세 조회")
     @GetMapping ("/health")
     public void getMeeting() {
     }
}
