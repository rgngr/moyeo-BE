package com.hanghae.finalProject.rest.user.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
@AllArgsConstructor
@ToString
@Setter
@Getter
public class Email {

    @Id
    private String email;

    private String ePw;


}
