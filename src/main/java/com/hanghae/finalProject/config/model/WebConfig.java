package com.hanghae.finalProject.config.model;

import com.hanghae.finalProject.config.converter.PlatFormCodeRequestConverter;
import com.hanghae.finalProject.config.jwt.JwtUtil;
import com.hanghae.finalProject.config.converter.CategoryCodeRequestConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
     @Override
     public void addCorsMappings(CorsRegistry registry) {
          registry.addMapping("/**")
               // 서버에서 응답하는 리소스에 접근가능한 출처 명시
               .allowedOrigins("http://localhost:3000", "https://final-project-fe-3.vercel.app") // , "https://www.dear-mylove.com"
               .allowedHeaders("*")
               .allowedMethods("*")
          .exposedHeaders(JwtUtil.AUTHORIZATION_HEADER); //JSON 으로 Token 내용 전달
     }
     
     @Override
     public void addFormatters(FormatterRegistry registry) {
          WebMvcConfigurer.super.addFormatters(registry);
          registry.addConverter(new CategoryCodeRequestConverter());
          registry.addConverter(new PlatFormCodeRequestConverter());
     }
}
