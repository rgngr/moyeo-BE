package com.hanghae.finalProject.config.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanghae.finalProject.config.dto.ErrorResponseDto;
import com.hanghae.finalProject.config.controller.errorcode.Code;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
     
     private final JwtUtil jwtUtil;
     
     @Override
     protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
     
          // request header에서 토큰을 가져오기
          String token = jwtUtil.resolveToken(request);
          
          if(token != null) {
               // 토큰 검증
               if(!jwtUtil.validateToken(token)){
//                    throw new RestApiException(Code.INVALID_TOKEN);
                    jwtExceptionHandler(response, Code.INVALID_TOKEN);
                    return;
               }
               // 토큰에서 유저정보 뽑기
               Claims info = jwtUtil.getUserInfoFromToken(token);
               // subject로 저장한 username 값 SecurityContextHolder에 저장
               setAuthentication(info.getSubject());
          }
          filterChain.doFilter(request,response);
     }
     
     public void setAuthentication(String username) {
          SecurityContext context = SecurityContextHolder.createEmptyContext();
          // 인증된 유저 생성
          Authentication authentication = jwtUtil.createAuthentication(username);
          context.setAuthentication(authentication);
          
          // >> 여기서 설정한 것을 @AuthenticationPrincipal 여기서 뽑아쓸 수 있음
          SecurityContextHolder.setContext(context);
     }
     
     // 토큰에러 예외처리
     public void jwtExceptionHandler(HttpServletResponse response, Code code) throws IOException {
          response.setStatus(code.getStatusCode().value()); // HttpStatus.UNAUTHORIZED.value()
          response.setContentType("application/json");
          try {
               String json = new ObjectMapper().writeValueAsString(ErrorResponseDto.of(code));
               response.getWriter().write(json);
          } catch (Exception e) {
               log.error(e.getMessage());
               throw e;
          }
     }
     
}