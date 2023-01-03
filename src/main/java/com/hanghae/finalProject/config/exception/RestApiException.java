package com.hanghae.finalProject.config.exception;

import com.hanghae.finalProject.config.controller.errorcode.Code;
import lombok.Getter;

@Getter
public class RestApiException extends RuntimeException{
//      https://velog.io/@leeeeeyeon/Spring-boot-Response-%ED%98%95%EC%8B%9D-%EB%A7%8C%EB%93%A4%EA%B8%B0#-errorresponsedto
     // 필드값
     private final Code errorCode;
     
     //getter
     public Code getErrorCode(){
          return this.errorCode;
     }
     // 생성자
     public RestApiException() {
          super(Code.INTERNAL_SERVER_ERROR.getStatusMsg());
          this.errorCode = Code.INTERNAL_SERVER_ERROR;
     }
     
     public RestApiException(String message) {
          super(Code.INTERNAL_SERVER_ERROR.getStatusMsg(message));
          this.errorCode = Code.INTERNAL_SERVER_ERROR;
     }
     
     public RestApiException(String message, Throwable cause) {
          super(Code.INTERNAL_SERVER_ERROR.getStatusMsg(message), cause);
          this.errorCode = Code.INTERNAL_SERVER_ERROR;
     }
     
     public RestApiException(Throwable cause) {
          super(Code.INTERNAL_SERVER_ERROR.getStatusMsg(cause));
          this.errorCode = Code.INTERNAL_SERVER_ERROR;
     }
     public RestApiException(Code statusCode){
          super(statusCode.getStatusMsg());
          this.errorCode = statusCode;
     }
     public RestApiException(Code errorCode, String message) {
          super(errorCode.getStatusMsg(message));
          this.errorCode = errorCode;
     }
     
     public RestApiException(Code errorCode, String message, Throwable cause) {
          super(errorCode.getStatusMsg(message), cause);
          this.errorCode = errorCode;
     }
     
     public RestApiException(Code errorCode, Throwable cause) {
          super(errorCode.getStatusMsg(cause), cause);
          this.errorCode = errorCode;
     }
}
