package com.ouat.checkout.app.config;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisHelper {
	
	@Autowired
	private RedisTemplate redisTemplate;


	public void setOTPOnRedis(String signInId, String otp) {
		redisTemplate.opsForValue().set(signInId, otp, 122, TimeUnit.SECONDS);
	}

	public String getOTPFROMRedis(String signInId) {
		try {
			Object v = redisTemplate.opsForValue().get(signInId);
			if (null != v) {
				return v.toString();
			}
		} catch (Exception e) {
		}
		return null;
	}
	
	public void setKeyForOTPCounter(String signInId) {
		redisTemplate.opsForValue().set(signInId, 1, 30, TimeUnit.MINUTES);
	}
	
	public void increaseCounter(String k) {
		redisTemplate.opsForValue().increment(k);
	}

	public void setKeyValueWithTTL(String k, Object v, long ttl, TimeUnit ttlUnit) {
		redisTemplate.opsForValue().set(k, v, ttl, ttlUnit);
	}
}
