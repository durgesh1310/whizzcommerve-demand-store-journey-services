package com.ouat.checkout.lock;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

@Configuration
public class RedisLockUtil<T> {
    @Autowired
    private RedisTemplate<String, T> redisTemplate;
    private ValueOperations<String, T> valueOperations;
    @Autowired
    RedisLockUtil(RedisTemplate<String, T> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.valueOperations = redisTemplate.opsForValue();
    }
    
    public void delete(String key) {
   	 redisTemplate.delete(key);
    }
    
    public boolean setIfAbsent(final String key,  T value , final Long timeOut, final TimeUnit timeUnit ) {
    	return valueOperations.setIfAbsent(key, value, timeOut, timeUnit);
    }
}
