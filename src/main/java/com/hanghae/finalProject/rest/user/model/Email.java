package com.hanghae.finalProject.rest.user.model;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@RedisHash()
@AllArgsConstructor
@ToString
@Setter
public class Email {

    @Id
    private String email;

    private String ePw;


}
