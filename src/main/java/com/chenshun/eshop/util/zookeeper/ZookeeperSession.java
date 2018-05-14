package com.chenshun.eshop.util.zookeeper;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
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
            logger.info("Zookeeper session State => {}", zooKeeper.getState());
            countDownLatch.await();
            logger.info("ZooKeeper session established......");
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
                    Thread.sleep(1000);
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
     * 获取分布式锁
     *
     * @param path
     */
    public void acquireDistributedLock(String path) {
        try {
            zooKeeper.create(path, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
            logger.debug("success to acquire lock for {}", path);
        } catch (KeeperException | InterruptedException e) {
            logger.debug("fail[1] to acquire lock for {}", path);
            int count = 0;
            while (true) {
                try {
                    Thread.sleep(1000);
                    zooKeeper.create(path, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
                } catch (InterruptedException | KeeperException e1) {
                    count++;
                    logger.debug("the {} times try to acquire lock for {}......", count, path);
                    continue;
                }
                logger.debug("success to acquire lock for {} after {} times try......", path, count);
                break;
            }
        }
    }

    /**
     * 获取分布式锁
     *
     * @param path
     * @return
     */
    public boolean acquireFastFailedDistributedLock(String path) {
        try {
            zooKeeper.create(path, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
            logger.debug("success to acquire lock for {}", path);
            return true;
        } catch (KeeperException | InterruptedException e) {
            logger.debug("fail to acquire lock for {}", path);
        }
        return false;
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
     * 释放掉一个分布式锁
     *
     * @param path
     */
    public void releaseDistributedLock(String path) {
        try {
            // 删除掉所有匹配节点版本的 node
            zooKeeper.delete(path, -1);
            logger.debug("release the lock for {}......", path);
        } catch (InterruptedException | KeeperException e) {
            e.printStackTrace();
        }
    }

    public String getNodeData(String path) {
        try {
            return new String(zooKeeper.getData(path, false, new Stat()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public void setNodeData(String path, String data) {
        try {
            zooKeeper.setData(path, data.getBytes(), -1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean createNode(String path) {
        try {
            zooKeeper.create(path, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
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
