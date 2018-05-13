package com.chenshun.eshop.util.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * User: chenshun131 <p />
 * Time: 18/5/12 15:51  <p />
 * Version: V1.0  <p />
 * Description:  <p />
 */
public class WorkThreadFactory implements ThreadFactory {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private AtomicInteger atomicInteger = new AtomicInteger(0);

    @Override
    public Thread newThread(Runnable r) {
        int c = atomicInteger.getAndIncrement();
        logger.debug("create no {} Threads", c);
        // 通过计数器，可以更好的管理线程
        return new WorkThread(r, atomicInteger);
    }

}
