package com.hanghae.finalProject;

import com.hanghae.finalProject.rest.redis.controller.RedisController;
import com.hanghae.finalProject.rest.redis.model.Student;
import com.hanghae.finalProject.rest.redis.repository.RedisRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.Order;

@SpringBootTest
@TestInstance (TestInstance.Lifecycle.PER_CLASS)
public class RedisWithMysqlTest {
     
     @Autowired
     RedisRepository studentRepository;
     
     @Autowired
     RedisController studentController;
     
     @Test
     @Order (1)
     void saveTest(){
          Student student1 = new Student("1", "zzarbttoo1", Student.Gender.FEMALE, 1);
          Student student2 = new Student("2", "zzarbttoo2", Student.Gender.FEMALE, 2);
          Student student3 = new Student("3", "zzarbttoo3", Student.Gender.FEMALE, 3);
          Student student4 = new Student("4", "zzarbttoo4", Student.Gender.FEMALE, 4);
          Student student5 = new Student("5", "zzarbttoo5", Student.Gender.FEMALE, 5);
          
          studentRepository.save(student1);
          studentRepository.save(student2);
          studentRepository.save(student3);
          studentRepository.save(student4);
          studentRepository.save(student5);
          
     }
     
     
     @Test
     @Order(2)
     void selectTest(){
          
          long start1 = System.currentTimeMillis();
          
          System.out.println(studentController.findStudentById("1").toString());
          System.out.println(studentController.findStudentById("2").toString());
          System.out.println(studentController.findStudentById("3").toString());
          System.out.println(studentController.findStudentById("4").toString());
          System.out.println(studentController.findStudentById("5").toString());
          
          long end1 = System.currentTimeMillis();
          System.out.println(end1 - start1);
          
          long start2 = System.currentTimeMillis();
          
          System.out.println(studentController.findStudentById("1").toString());
          System.out.println(studentController.findStudentById("2").toString());
          System.out.println(studentController.findStudentById("3").toString());
          System.out.println(studentController.findStudentById("4").toString());
          System.out.println(studentController.findStudentById("5").toString());
          
          long end2 = System.currentTimeMillis();
          
          System.out.println(end2 - start2);
          
     }
     
     @Test
     @Order(3)
     void updateTest(){
          
          Student updateStudent = studentRepository.findById("1").get();
          updateStudent.setName("updated Name");
          studentRepository.save(updateStudent);
          
          
          Student selectStudent = studentRepository.findById("1").get();
          System.out.println(selectStudent.toString());
          
          Student redisStudent = studentController.findStudentById("1");
          System.out.println(redisStudent.toString());
          
     }
}
