package com.hanghae.finalProject.config.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ErrorResponseDto {
     private boolean success = false;
     private final String statusMsg;
     private final int statusCode;
     
     public ErrorResponseDto(String statusMsg, int statusCode){
          this.statusMsg = statusMsg;
          this.statusCode = statusCode;
     }
}
