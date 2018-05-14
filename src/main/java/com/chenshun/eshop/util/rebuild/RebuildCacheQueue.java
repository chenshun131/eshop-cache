package com.chenshun.eshop.util.rebuild;

import com.chenshun.eshop.model.ProductInfo;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * User: chenshun131 <p />
 * Time: 18/5/12 14:21  <p />
 * Version: V1.0  <p />
 * Description: 重建缓存的内存队列 : 队列中的数据将被缓存重建线程取出并与 Redis 进行比较，如果比 Redis 中的数据新则覆盖 Redis 中的数据 <p />
 */
public class RebuildCacheQueue {

    private ArrayBlockingQueue<ProductInfo> queue = new ArrayBlockingQueue<>(1000);

    private RebuildCacheQueue() {
    }

    public void putProductInfo(ProductInfo productInfo) {
        try {
            queue.put(productInfo);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public ProductInfo takeProductInfo() {
        try {
            return queue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static class Singleton {

        private static RebuildCacheQueue instance;

        static {
            instance = new RebuildCacheQueue();
        }

        public static RebuildCacheQueue getInstance() {
            return instance;
        }
    }

    public static RebuildCacheQueue getInstance() {
        return Singleton.getInstance();
    }

    public static void init() {
        getInstance();
    }

}
