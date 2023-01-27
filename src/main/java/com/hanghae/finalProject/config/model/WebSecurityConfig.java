package com.hanghae.finalProject.config.model;

import com.hanghae.finalProject.config.jwt.JwtAuthFilter;
import com.hanghae.finalProject.config.jwt.JwtUtil;
import com.hanghae.finalProject.config.security.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity // 스프링 Security 지원을 가능하게 함
@EnableGlobalMethodSecurity (securedEnabled = true) // @Secured 어노테이션 활성화
@RequiredArgsConstructor
public class WebSecurityConfig {
     private final JwtUtil jwtUtil;
     private final UserDetailsServiceImpl userDetailsService;
     
     @Bean // 비밀번호 암호화 기능 등록
     public PasswordEncoder passwordEncoder() {
          return new BCryptPasswordEncoder();
     }
     
     @Bean
     public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
          // cors 설정
          http.cors();
          // CSRF 설정
          http.csrf().disable();

          // 기본 설정인 Session 방식은 사용하지 않고 JWT 방식을 사용하기 위한 설정
          http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
          http.authorizeRequests()
               // 토큰검증 필요없는 페이지 설정
               .antMatchers(HttpMethod.POST, "/api/users/**").permitAll()
               .antMatchers(HttpMethod.GET, "/api/users/kakao/callback").permitAll()
               .antMatchers(HttpMethod.GET, "/api/users/mail-code/**").permitAll()
               .antMatchers(HttpMethod.GET, "/api/meetings/{\\d+}").permitAll() // 모임 상세 조회
               .antMatchers(HttpMethod.GET, "/api/meetings/{\\d+}/attendants").permitAll() // 모임 참석자 리스트
               .antMatchers( "/alarm.html").permitAll()
               .antMatchers(HttpMethod.GET, "/api/alarm/subscribe/{\\d+}").permitAll()
               .antMatchers(HttpMethod.GET, "/api/users/passwordChange").permitAll()
               .antMatchers("/api/doc").permitAll()
               .antMatchers("/swagger-ui/**").permitAll() //스웨거 권한설정 X
               .antMatchers("/swagger-resources/**").permitAll() //스웨거 권한설정 X
               .antMatchers("/swagger-ui.html").permitAll() //스웨거 권한설정 X
               .antMatchers("/v2/api-docs").permitAll() //스웨거 권한설정 X
               .antMatchers("/v3/api-docs").permitAll() //스웨거 권한설정 X
               .antMatchers("/webjars/**").permitAll() //스웨거 권한설정 X
               .anyRequest().authenticated()
               //서버는 JWT 토큰을 검증하고 토큰의 정보를 사용하여 사용자의 인증을 진행해주는 Spring Security 에 등록한 JwtAuthFilter 를 사용하여 인증/인가를 처리한다.
               .and().addFilterBefore(new JwtAuthFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);
          
          // 접근 제한 페이지 이동 설정
//          http.exceptionHandling().accessDeniedPage("/api/user/forbidden");
          
          return http.build();
     }
}