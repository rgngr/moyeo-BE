package com.hanghae.finalProject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.redis.core.RedisKeyValueAdapter;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

//@EnableSqs
@EnableRedisRepositories (enableKeyspaceEvents = RedisKeyValueAdapter.EnableKeyspaceEvents.ON_STARTUP)
@EnableJpaAuditing
@EnableScheduling
@EnableAsync
@SpringBootApplication
public class FinalProjectApplication {
	public static void main(String[] args) {
		SpringApplication.run(FinalProjectApplication.class, args);
	}
}
