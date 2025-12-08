package com.ouat.checkout.lock;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 
 * @author : Sourabh Singh
 * @implNote : A distributed locking class which will use in taggd.com to lock the request,
 *  which will help to avoid collision between to concurrent request
 *  there are 4 argument passing  
 *  1. keys (On which we are taking lock )
 *  2. lock acquired time (total time to acquire the lock), 
 *  3. default retry time (if key not locked in one shot there is retry time  to take lock again ), 
 *  4. lockacquiredTimeout time we can retry for certain amount of time after we will through exception)
 *  which we can pass in the lock acquire function for 
 *  @Db : Redis for caching the locked keys 
 *  @keys : product skus
 *
 */

@Service
public class RedisLock {
	private static final Logger REDIS_LOCK = LoggerFactory.getLogger("redis-lock");

	@Autowired
	private RedisLockUtil<String> redisLock;
	
	public void releaseLock(List<String> keys ) {
		REDIS_LOCK.info("releaseing: {}",keys);
		for(String it : keys)redisLock.delete(it);
	}
	
	public Boolean acquireLock(final List<String> keys, Lock lock) throws Exception {
		List<String>lockNotAcquiredKeys = new ArrayList<String>();
		List<String>lockAcquiredKeys = new ArrayList<String>();
			for(String key : keys) {
				Boolean lockAquired = Boolean.FALSE;
				REDIS_LOCK.info("locking : {}",key);
			    lockAquired = redisLock.setIfAbsent(key, key,  180000l, TimeUnit.MILLISECONDS);
				if(lockAquired.equals(Boolean.FALSE)) lockNotAcquiredKeys.add(key);
				else lockAcquiredKeys.add(key);
			}
			if(!lockNotAcquiredKeys.isEmpty()) {
				releaseLock(lockAcquiredKeys);
				REDIS_LOCK.info("keys : {} going to sleep",keys);
				sleep(lock.getDefaultRetryTime());
				if( System.currentTimeMillis() >=  lock.getLockAcquiredTimeoutTime()) {
					REDIS_LOCK.info("failed to acuire lock on : {}",keys);
					return false;
 				}
				acquireLock(keys, lock);
			}
		return true;
 	}
    
	private static void sleep(final long sleepTimeMillis) {
		try {
			Thread.sleep(sleepTimeMillis);
		} catch (final InterruptedException e) {
			Thread.currentThread().interrupt();
			REDIS_LOCK.error(e.getMessage(), e);
		}
	}
}
