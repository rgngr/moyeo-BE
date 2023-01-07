package com.hanghae.finalProject.config.handler;

import com.hanghae.finalProject.config.dto.ErrorResponseDto;
import com.hanghae.finalProject.config.errorcode.Code;
import com.hanghae.finalProject.config.exception.RestApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
     
     // RestApiException 에러 핸들링
//     @ExceptionHandler (RestApiException.class)
//     public ResponseEntity<Object> handleCustomException(RestApiException e) {
//          Code statusCode = e.getErrorCode();
//          return handleExceptionInternal(statusCode);
//     }
     
     //ResponseStatusException 에러 핸들링
//     @ExceptionHandler(ResponseStatusException.class)
//     public ResponseEntity<Object> handleResponseStatus(ResponseStatusException e) {
//          log.warn("handleResponseStatus", e);
//          return ResponseEntity.status( e.getStatus())
//               .body(ErrorResponseDto.builder()
//                    .statusCode(e.getRawStatusCode())
//                    .statusMsg(e.getMessage())
//                    .build());
//     }
     // MissingServletRequestPartException 에러 핸들링
//     @ExceptionHandler (IllegalStateException.class)
//     public ResponseEntity<Object> missingServletRequestPartException(IllegalStateException e) {
//          log.warn("missingServletRequestPartException", e);
//          return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//               .body(ErrorResponseDto.builder()
//                    .statusCode(HttpStatus.BAD_REQUEST.value())
//                    .statusMsg(e.getMessage())
//                    .build());
//     }
     // IllegalArgumentException 에러 핸들링
//     @ExceptionHandler(IllegalArgumentException.class)
//     public ResponseEntity<Object> handleIllegalArgument(IllegalArgumentException e) {
//          log.warn("handleIllegalArgument", e);
//          Code statusCode = Code.INVALID_PARAMETER;
//          return handleExceptionInternal(statusCode, e.getMessage());
//     }
     

     
     // ConstraintViolationException 에러 핸들링
     
     @org.springframework.web.bind.annotation.ExceptionHandler
     public ResponseEntity<Object> validation(ConstraintViolationException e, WebRequest request) {
          return handleExceptionInternal(e, Code.INTERNAL_SERVER_ERROR, request);
     }
     @org.springframework.web.bind.annotation.ExceptionHandler
     public ResponseEntity<Object> general(RestApiException e, WebRequest request) {
          return handleExceptionInternal(e, e.getErrorCode(), request);
     }
     
     // MethodArgumentNotValid 에러 핸들링
     @Override
     protected ResponseEntity<Object> handleMethodArgumentNotValid(
                    MethodArgumentNotValidException e,
                    HttpHeaders headers,
                    HttpStatus status,
                    WebRequest request) {
          log.warn("handleMethodArgumentNotValid", e);
          String errorFieldName = e.getBindingResult().getFieldError().getField();
          Code statusCode = Code.INVALID_PARAMETER;
          if(errorFieldName.equals("email")){
               statusCode = Code.WRONG_EMAIL_PATTERN;
          }else if(errorFieldName.equals("password")){
               statusCode = Code.WRONG_PASSWORD_PATTERN;
          }else if(errorFieldName.equals("username")){
               statusCode = Code.WRONG_USERNAME_PATTERN;
          }
          return handleExceptionInternal(e, statusCode, request);
     }
     
     @org.springframework.web.bind.annotation.ExceptionHandler
     public ResponseEntity<Object> exception(Exception e, WebRequest request) {
          return handleExceptionInternal(e, Code.INTERNAL_SERVER_ERROR, request);
     }
     
     
     // 그외 에러들 핸들링
//     @ExceptionHandler({Exception.class})
//     public ResponseEntity<Object> handleAllException(Exception ex) {
//          log.warn(">>>>>>>>>handleAllException", ex);
//          ex.printStackTrace();
//          Code statusCode = Code.INTERNAL_SERVER_ERROR;
//          return handleExceptionInternal(statusCode);
//     }
     
     // ErrorCode 만 있는 에러 ResponseEntity 생성
//     private ResponseEntity<Object> handleExceptionInternal(Code statusCode) {
//          return ResponseEntity.status(statusCode.getStatusCode())
//               // ErrorCode 만 있는 에러 responseEntity body만들기
//               .body(makeErrorResponse(statusCode));
//     }
     
//     private ErrorResponseDto makeErrorResponse(Code statusCode) {
//          return ErrorResponseDto.builder()
//               .statusCode(statusCode.getStatusCode())
//               .statusMsg(statusCode.getStatusMsg())
//               .build();
//     }
     
     // ErrorCode + message따로 있는 에러 ResponseEntity 생성
//     private ResponseEntity<Object> handleExceptionInternal(Code statusCode, String message) {
//          return ResponseEntity.status(statusCode.getStatusCode())
//               .body(makeErrorResponse(statusCode, message));
//     }
     
//     private ErrorResponseDto makeErrorResponse(Code statusCode, String message) {
//          return ErrorResponseDto.builder()
//               .statusCode(statusCode.getStatusCode())
//               .statusMsg(message)
//               .build();
//     }
     @Override
     protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body,
                                                              HttpHeaders headers, HttpStatus status, WebRequest request) {
          return handleExceptionInternal(ex, Code.valueOf(status), headers, status, request);
     }
     
     // no header, no status
     private ResponseEntity<Object> handleExceptionInternal(Exception e, Code errorCode,
                                                            WebRequest request) {
          return handleExceptionInternal(e, errorCode, HttpHeaders.EMPTY, errorCode.getStatusCode(),
               request);
     }
     
     // Allargs Constructor
     private ResponseEntity<Object> handleExceptionInternal(Exception e, Code errorCode,
                                                            HttpHeaders headers, HttpStatus status, WebRequest request) {
          return super.handleExceptionInternal(
               e,
//               ErrorResponseDto.of(errorCode, errorCode.getStatusMsg(e)),
               ErrorResponseDto.of(errorCode),
               headers,
               status,
               request
          );
     }
}
