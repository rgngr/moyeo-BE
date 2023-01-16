package com.hanghae.finalProject.config.util;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@NoArgsConstructor
public class RedisUtil {
     @CacheEvict (value = "Calendar", key = "#userId+','+#year+','+#month")
     public void deleteCache(Long userId, Integer year, Integer month){
          //processing : for ex delete from the database and also remove from cache
          log.info("delete Cache Calendar{},{},{}", userId, year, month );
     }
}
