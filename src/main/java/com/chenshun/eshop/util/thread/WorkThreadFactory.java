package com.chenshun.eshop.util.thread;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * User: chenshun131 <p />
 * Time: 18/5/12 15:51  <p />
 * Version: V1.0  <p />
 * Description:  <p />
 */
@Slf4j
public class WorkThreadFactory implements ThreadFactory {

    private AtomicInteger atomicInteger = new AtomicInteger(0);

    @Override
    public Thread newThread(Runnable r) {
        int c = atomicInteger.getAndIncrement();
        log.debug("create no {} Threads", c);
        // 通过计数器，可以更好的管理线程
        return new WorkThread(r, atomicInteger);
    }

}
