package com.hanghae.finalProject.config.controller.errorcode;

import com.hanghae.finalProject.config.exception.RestApiException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Predicate;

@Getter
@RequiredArgsConstructor
public enum Code {
     // 정리필요
     OK("정상", HttpStatus.OK),
     FILE_SAVE_FAIL("파일 저장에 실패하였습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
     WRONG_IMAGE_FORMAT("지원하지 않는 파일 형식입니다.", HttpStatus.BAD_REQUEST),
     POST_LIKE( "좋아요", HttpStatus.OK),
     POST_LIKE_CANCEL( "좋아요취소", HttpStatus.OK),
     DELETE_COMMENT( "댓글 삭제 성공", HttpStatus.OK),
     CREATE_COMMENT( "댓글 작성 성공", HttpStatus.OK),
     UPDATE_COMMENT( "댓글 수정 성공", HttpStatus.OK),
     CREATE_POST( "게시글 작성 성공", HttpStatus.OK),
     UPDATE_POST( "게시글 수정 성공", HttpStatus.OK),
     DELETE_POST( "게시글 삭제 성공", HttpStatus.OK),
     INVALID_PARAMETER("Invalid parameter included",HttpStatus.BAD_REQUEST),
     INTERNAL_SERVER_ERROR("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR),
     BAD_REQUEST("Bad request",HttpStatus.BAD_REQUEST),
     NO_ARTICLE("게시글이 존재하지 않습니다", HttpStatus.NOT_FOUND),
     NO_COMMENT("댓글이 존재하지 않습니다.", HttpStatus.NOT_FOUND),
     INVALID_USER("작성자만 삭제/수정할 수 있습니다.", HttpStatus.BAD_REQUEST),
     DELETE_USER( "회원 탈퇴 성공", HttpStatus.OK),
     NOT_FOUND_AUTHORIZATION_IN_SECURITY_CONTEXT("Security Context에 인증 정보가 없습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
     USER_SIGNUP_SUCCESS("회원가입 성공", HttpStatus.OK),
     USER_SIGNUP_FAIL("회원가입 실패", HttpStatus.BAD_REQUEST),
     USER_LOGIN_SUCCESS("로그인 성공", HttpStatus.OK),
     
     ONLY_FOR_ADMIN("관리자만 가능합니다.", HttpStatus.BAD_REQUEST),
     WRONG_USERNAME_PATTERN("유저명은 최소 4자 이상, 10자 이하이며 알파벳 소문자(a~z), 숫자(0~9)로 구성되어야 합니다.", HttpStatus.BAD_REQUEST),
     WRONG_PASSWORD_PATTERN("비밀번호는 최소 8자 이상, 15자 이하이며 알파벳 대소문자(a~z, A~Z), 숫자(0~9), 특수문자로 구성되어야 합니다.", HttpStatus.BAD_REQUEST),
     WRONG_PASSWORD("비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST),
     NO_USER("회원을 찾을 수 없습니다.",HttpStatus.BAD_REQUEST),
     WRONG_ADMIN_TOKEN("관리자 암호가 틀려 등록이 불가능합니다.", HttpStatus.BAD_REQUEST),
     OVERLAPPED_USERNAME("중복된 username 입니다.", HttpStatus.BAD_REQUEST),
     OVERLAPPED_NICKNAME("중복된 닉네임 입니다.", HttpStatus.BAD_REQUEST),
     OVERLAPPED_EMAIL("중복된 email 입니다",HttpStatus.BAD_REQUEST),
     PASSWORD_CHECK("입력된 비밀번호가 다릅니다.", HttpStatus.BAD_REQUEST),
     임시이넘객체("임시객체입니다", HttpStatus.BAD_REQUEST),
     INVALID_TOKEN("토큰이 유효하지 않습니다.", HttpStatus.UNAUTHORIZED);
     
     private final String StatusMsg;
     private final HttpStatus statusCode;
     
     public String getStatusMsg(Throwable e){
          return this.getStatusMsg(this.getStatusMsg() + " - " + e.getMessage());
          // 결과 예시 - "Validation error - Reason why it isn't valid"
     }
     public String getStatusMsg(String message){
          return Optional.ofNullable(message)
               .filter(Predicate.not(String::isBlank))
               .orElse(this.getStatusMsg());
     }
     
     public static Code valueOf(HttpStatus httpStatus) {
          if (httpStatus == null) {
               throw new RestApiException("HttpStatus is null.");
          }
          
          return Arrays.stream(values())
               .filter(errorCode -> errorCode.getStatusCode() == httpStatus)
               .findFirst()
               .orElseGet(() -> {
                    if (httpStatus.is4xxClientError()) {
                         return Code.BAD_REQUEST;
                    } else if (httpStatus.is5xxServerError()) {
                         return Code.INTERNAL_SERVER_ERROR;
                    } else {
                         return Code.OK;
                    }
               });
     }
     
     @Override
     public String toString() {
          return String.format("%s (%d)", this.name(), this.getStatusCode().value());
     }
}
