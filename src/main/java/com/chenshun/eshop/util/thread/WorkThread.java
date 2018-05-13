package com.chenshun.eshop.util.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * User: chenshun131 <p />
 * Time: 18/5/12 15:55  <p />
 * Version: V1.0  <p />
 * Description:  <p />
 */
public class WorkThread extends Thread {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /** 线程执行目标 */
    private Runnable target;

    private AtomicInteger counter;

    public WorkThread(Runnable target, AtomicInteger counter) {
        this.target = target;
        this.counter = counter;
    }

    @Override
    public void run() {
        try {
            target.run();
        } finally {
            logger.debug("terminate no {} Threads", counter.getAndDecrement());
        }
    }

}
