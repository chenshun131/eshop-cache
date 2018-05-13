package com.chenshun.eshop.util.zookeeper;

import org.apache.zookeeper.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * User: chenshun131 <p />
 * Time: 18/5/12 13:28  <p />
 * Version: V1.0  <p />
 * Description:  <p />
 */
public class ZookeeperSession {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private static CountDownLatch countDownLatch = new CountDownLatch(1);

    private ZooKeeper zooKeeper;

    private ZookeeperSession() {
        try {
            this.zooKeeper = new ZooKeeper("ci-server:2181", 5000, new ZookeeperWatcher());
            // 给一个状态 CONNECTING，连接中
            logger.debug("Zookeeper session State => ", zooKeeper.getState());
            countDownLatch.await();
            logger.debug("ZooKeeper session established......");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取分布式锁
     *
     * @param productId
     */
    public void acquireDistributedLock(Long productId) {
        String path = "/product-lock-" + productId;
        try {
            zooKeeper.create(path, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
            logger.debug("success to acquire lock for product[id={}]", productId);
        } catch (KeeperException | InterruptedException e) {
            logger.debug("fail[1] to acquire lock for product[id={}]", productId);
            // 如果对应那个商品的锁的 node 已经存在，就是已经被别人加锁，那么就会报错，抛出 NodeExistsException
            int count = 0;
            while (true) {
                try {
                    Thread.sleep(20);
                    zooKeeper.create(path, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
                } catch (InterruptedException | KeeperException e1) {
                    e1.printStackTrace();
                    count++;
                    logger.debug("the {} times try to acquire lock for product[id={}]......", count, productId);
                    continue;
                }
                logger.debug("success to acquire lock for product[id={}] after {} times try......", productId, count);
                break;
            }
        }
    }

    /**
     * 释放掉一个分布式锁
     *
     * @param productId
     */
    public void releaseDistributedLock(Long productId) {
        try {
            // 删除掉所有匹配节点版本的 node
            zooKeeper.delete("/product-lock-" + productId, -1);
            logger.debug("release the lock for product[id={}]......", productId);
        } catch (InterruptedException | KeeperException e) {
            e.printStackTrace();
        }
    }

    /**
     * 建立 zookeeper session 的 Watcher
     */
    private class ZookeeperWatcher implements Watcher {

        @Override
        public void process(WatchedEvent event) {
            logger.debug("Receive watched event: {}", event.getState());
            if (Event.KeeperState.SyncConnected == event.getState()) {
                countDownLatch.countDown();
            }
        }
    }

    private static class Singleton {

        private static ZookeeperSession instance;

        static {
            instance = new ZookeeperSession();
        }

        public static ZookeeperSession getInstance() {
            return instance;
        }

    }

    /**
     * 获取 ZookeeperSession 实例
     *
     * @return
     */
    public static ZookeeperSession getInstance() {
        return Singleton.getInstance();
    }

    /**
     * 初始化单例的便捷方式
     */
    public static void init() {
        getInstance();
    }

}
