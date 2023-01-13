package com.hanghae.finalProject.rest.redis.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

//@RedisHash ("Student")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Setter
public class Student implements Serializable {
     public enum Gender{
          MALE, FEMALE
     }
     @Id
     private String id;
     private String name;
     private Gender gender;
     private int grade;
}
