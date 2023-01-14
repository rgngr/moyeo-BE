package com.hanghae.finalProject.config.model;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.CacheKeyPrefix;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

//@EnableRedisRepositories //  Redis Repository 활성화
@Configuration
@EnableCaching
public class RedisConfig {
     
     @Value ("${spring.redis.port}")
     private int port;
     
     @Value("${spring.redis.host}")
     private String host;
     @Bean
     public RedisConnectionFactory redisConnectionFactory(){
          return new LettuceConnectionFactory(host, port);
     }
     
     @Bean
     public RedisTemplate<String, Object> redisTemplate() {
          RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
          redisTemplate.setConnectionFactory(redisConnectionFactory());
          redisTemplate.setKeySerializer(new StringRedisSerializer());
          redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
          return redisTemplate;
     }
     
     @Bean
     public CacheManager redisCacheManager(){
          // defaultCacheConfig : default 캐시 전략
          RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig()
               .disableCachingNullValues() // null value의 경우 캐시 X
               .entryTtl(Duration.ofSeconds(CacheKey.DEFAULT_EXPIRE_SEC)) //캐시의 기본 유효시간 설정
               .computePrefixWith(CacheKeyPrefix.simple()) // value와 key로 만들어지는 Key값을 ::로 구분
               // 캐시 Key를 직렬화-역직렬화 하는데 사용하는 Pair를 지정
               // -> String으로 지정
               .serializeKeysWith(
                    RedisSerializationContext.SerializationPair
                         .fromSerializer(new StringRedisSerializer()))
               // 캐시 Value를 직렬화-역직렬화 하는데 사용하는 Pair를 지정
               //  Value는 다양한 자료구조가 올 수 있으므로 JsonSerializer 사용
               .serializeValuesWith(RedisSerializationContext
                    .SerializationPair
                    .fromSerializer(new GenericJackson2JsonRedisSerializer()));

          // 캐시키 별 default 유효시간 설정
          Map<String, RedisCacheConfiguration> cacheConfiguration = new HashMap<>();
          //(키를 조합할 때 사용하는 Value값, TTL) 형태의 key-value 구조로 캐시 키별 유효시간 설정 가능, put으로 추가 가능
          cacheConfiguration.put(CacheKey.ZONE,RedisCacheConfiguration.defaultCacheConfig()
               .entryTtl(Duration.ofSeconds(CacheKey.ZONE_EXPIRE_SEC)));

          return RedisCacheManager.RedisCacheManagerBuilder
               .fromConnectionFactory(redisConnectionFactory())
               .cacheDefaults(configuration)
               .withInitialCacheConfigurations(cacheConfiguration)
               .build();
     }
}
