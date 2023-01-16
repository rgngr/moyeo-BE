//package com.hanghae.finalProject;
//
//import com.hanghae.finalProject.rest.user.model.Email;
//import com.hanghae.finalProject.rest.user.repository.EmailRepository;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.NoSuchElementException;
//
//@SpringBootTest
//public class RedisTest {
//
//    @Autowired
//    EmailRepository emailRepository;
//
//    //insert
//    @Test
//    void saveTest(){
//        Email student1 = new Email("1", "zzarbttoo1", Email.Gender.FEMALE, 1);
//        Email student2 = new Email("2", "zzarbttoo2", Email.Gender.FEMALE, 2);
//        Email student3 = new Email("3", "zzarbttoo3", Email.Gender.FEMALE, 3);
//        Email student4 = new Email("4", "zzarbttoo4", Email.Gender.FEMALE, 4);
//        Email student5 = new Email("5", "zzarbttoo5", Email.Gender.FEMALE, 5);
//
//        emailRepository.save(student1);
//        emailRepository.save(student2);
//        emailRepository.save(student3);
//        emailRepository.save(student4);
//        emailRepository.save(student5);
//    }
//
//    //select
//    @Test
//    void selectTest(){
//
//        Email selectStudent1 = emailRepository.findById("1").get();
//        Email selectStudent2 = emailRepository.findById("2").get();
//        Email selectStudent3 = emailRepository.findById("3").get();
//        Email selectStudent4 = emailRepository.findById("4").get();
//        Email selectStudent5 = emailRepository.findById("5").get();
//
//        System.out.println(selectStudent1.toString());
//        System.out.println(selectStudent2.toString());
//        System.out.println(selectStudent3.toString());
//        System.out.println(selectStudent4.toString());
//        System.out.println(selectStudent5.toString());
//    }
//
//    //update
//    @Test
//    void updateTest(){
//
//        Email selectStudent1 = emailRepository.findById("1").get();
//        selectStudent1.setName("updated Name");
//        emailRepository.save(selectStudent1);
//        System.out.println(selectStudent1.toString());
//
//    }
//
//    //delete
//    @Test
//    void deleteTest(){
//        emailRepository.deleteById("2");
//        Assertions.assertThrows(NoSuchElementException.class, () -> emailRepository.findById("2").get());
//    }
//
//    //selectAll
//    @Test
//    void selectAllTest(){
//        List<Email> emails = new ArrayList<>();
//        emailRepository.findAll().forEach(emails::add);
//        System.out.println(emails.toString());
//    }
//
//}
//
