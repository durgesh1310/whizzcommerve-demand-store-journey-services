package com.ouat.checkout.lock;

public enum Lock {
    
	/**
	 * inventory update api takes min 200 microsec  to max 600 microsec so we will take 4000 microsec 
	 * which is equal to 100 mili sec for lockAredTime out time 
	 * and we will take  four times  the inventory update time for retry that is 2000 microsec 
	 * which is equal to 10 mili sec and 2 mili sec for lock acquired time  
	 */

    INVENTORY(4,2 , 8 + System.currentTimeMillis()),
	INVENTORYV1(8, 4, 16 +  System.currentTimeMillis()),
	/**
	 *  2 min  lock acuired
	 *  1 min  lock retry time
	 *  4 min lockacquired time out 
	 */
	PLACEORDER(2*1000, (1)*1000, 4*1000 +  System.currentTimeMillis());

    private final long lockAcquiredTime;
    private final long defaultRetryTime;
    private final long lockAcquiredTimeoutTime;

    Lock(long lockAcquiredTime, long defaultRetryTime, long lockAcquiredTimeoutTime) {
        this.lockAcquiredTime = lockAcquiredTime;
        this.defaultRetryTime = defaultRetryTime;
        this.lockAcquiredTimeoutTime = lockAcquiredTimeoutTime;
    }

    public long getLockAcquiredTime() {
        return lockAcquiredTime;
    }

    public long getDefaultRetryTime() {
        return defaultRetryTime;
    }

    public long getLockAcquiredTimeoutTime() {
        return lockAcquiredTimeoutTime;
    }

}
