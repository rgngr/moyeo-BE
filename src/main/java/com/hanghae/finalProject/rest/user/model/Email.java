package com.hanghae.finalProject.rest.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
@AllArgsConstructor
@ToString
@Setter
@Getter
@RedisHash("Email")
public class Email {

    @Id
    private String email;

    private String ePw;


}
