package com.hmdp.utils;

public interface ILock {

    /**
     * 获取锁
     * @param timeoutSec
     * @return true代表获取成功，false代表获取锁失败
     */
    boolean tryLock(long timeoutSec);

    /**
     * 释放锁
     */
    void unlock();
}
