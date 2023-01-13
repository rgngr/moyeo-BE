package com.hanghae.finalProject;

import com.hanghae.finalProject.rest.redis.model.Student;
import com.hanghae.finalProject.rest.redis.repository.RedisRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@SpringBootTest
class FinalProjectApplicationTests {
     
     @Autowired
     RedisRepository studentRepository;
     
     //insert
     @Test
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
     
     //select
     @Test
     void selectTest(){
          
          Student selectStudent1 = studentRepository.findById("1").get();
          Student selectStudent2 = studentRepository.findById("2").get();
          Student selectStudent3 = studentRepository.findById("3").get();
          Student selectStudent4 = studentRepository.findById("4").get();
          Student selectStudent5 = studentRepository.findById("5").get();
          
          System.out.println(selectStudent1.toString());
          System.out.println(selectStudent2.toString());
          System.out.println(selectStudent3.toString());
          System.out.println(selectStudent4.toString());
          System.out.println(selectStudent5.toString());
     }
     
     //update
     @Test
     void updateTest(){
          
          Student selectStudent1 = studentRepository.findById("1").get();
          selectStudent1.setName("updated Name");
          studentRepository.save(selectStudent1);
          System.out.println(selectStudent1.toString());
          
     }
     
     //delete
     @Test
     void deleteTest(){
          studentRepository.deleteById("2");
          Assertions.assertThrows(NoSuchElementException.class, () -> studentRepository.findById("2").get());
     }
     
     //selectAll
     @Test
     void selectAllTest(){
          List<Student> students = new ArrayList<>();
          studentRepository.findAll().forEach(students::add);
          System.out.println(students.toString());
     }
}
