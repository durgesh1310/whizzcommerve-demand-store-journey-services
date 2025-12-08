package com.ouat.authServiceDemandStore.service;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisHelper {
	
	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	
	public void setData(String key, String value, Long ttl, TimeUnit timeunit) {
		redisTemplate.opsForValue().set(key, value, ttl, timeunit);
	}
	
	public String getData(String key) {
		return redisTemplate.opsForValue().get(key);
	}
	
}
