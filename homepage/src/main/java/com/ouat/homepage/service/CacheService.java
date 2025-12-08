package com.ouat.homepage.service;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.ouat.homepage.clients.HomepageSetupServiceClient;
import com.ouat.homepage.exception.BusinessProcessException;

@Service
public class CacheService {

	@Autowired
	RedisHelper redisHelper;

	@Autowired
	private HomepageSetupServiceClient homepageSetupServiceClient;

	public LoadingCache<String, String> cache = CacheBuilder.newBuilder().maximumSize(100)
			.expireAfterAccess(60, TimeUnit.SECONDS).build(new CacheLoader<String, String>() {
				public String load(String key) throws BusinessProcessException {
					return loadFromRedis(key);
				}

				private String loadFromRedis(String key) throws BusinessProcessException {
					String value = redisHelper.get(key);
					if (null == value || "-1".equals(value)) {
						throw new BusinessProcessException();
						//value = homepageSetupServiceClient.makeGetCall(CommonConstant.COMPONENT_URL_MAPPING.get(key), null);
						//redisHelper.set(key, value, CommonConstant.TTL);
					}
					return redisHelper.get(key);
				}
			});

}
