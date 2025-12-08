package com.ouat.homepage.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Service
public class RedisHelper {
	@Autowired
	private JedisPool jedisPool;

	/**
	 * Store string key value pairs, permanently valid
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public String set(String key, String value) {
		Jedis jedis = jedisPool.getResource();
		try {
			return jedis.set(key, value);
		} catch (Exception e) {
			return "-1";
		} finally {
			// The business operation is completed and the connection is returned to the
			// connection pool
			jedis.close();
		}
	}

	/**
	 * Get the specified Value according to the passed in key
	 * 
	 * @param key
	 * @return
	 */
	public String get(String key) {
		Jedis jedis = jedisPool.getResource();
		try {
			return jedis.get(key);
		} catch (Exception e) {
			return "-1";
		} finally {
			jedis.close();
		}
	}

	/**
	 * Delete string key value pairs
	 * 
	 * @param key
	 * @return
	 */
	public Long del(String key) {
		Jedis jedis = jedisPool.getResource();
		try {
			return jedis.del(key);
		} catch (Exception e) {
			return -1L;
		} finally {
			jedis.close();
		}
	}

	/**
	 * Verify whether the Key value exists
	 */
	public Boolean exists(String key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.exists(key);
		} catch (Exception e) {
			return false;
		} finally {
			// Return connection
			jedis.close();
		}
	}
	
	/**
	 * Store string key value pairs, permanently valid
	 * 
	 * @param key
	 * @param value
	 * @param ttl in seconds
	 * @return
	 */
	public String set(String key, String value, Integer ttl) {
		Jedis jedis = jedisPool.getResource();
		try {
			jedis.expire(key, ttl);
			return jedis.set(key, value);
		} catch (Exception e) {
			return "-1";
		} finally {
			// The business operation is completed and the connection is returned to the
			// connection pool
			jedis.close();
		}
	}
}