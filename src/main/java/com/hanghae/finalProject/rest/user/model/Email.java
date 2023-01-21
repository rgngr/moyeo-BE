package com.hanghae.finalProject.rest.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@AllArgsConstructor
@ToString
@Setter
@Getter
@RedisHash(value = "Email", timeToLive = 180L)
public class Email implements Serializable {

    @Id
    private String email;

    private String ePw;


}
