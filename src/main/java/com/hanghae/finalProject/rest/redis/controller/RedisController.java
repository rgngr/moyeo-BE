package com.hanghae.finalProject.rest.redis.controller;

import com.hanghae.finalProject.rest.redis.model.Student;
import com.hanghae.finalProject.rest.redis.repository.RedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;


@Controller
@RequiredArgsConstructor
public class RedisController {
     public final RedisRepository studentRepository;
     
     @Cacheable (value = "Student")
     public Student findStudentById(String id){
          
          return studentRepository.findById(id)
               .orElseThrow(RuntimeException::new);
     }
}
