package com.chenshun.eshop.util.rebuild;

import com.chenshun.eshop.model.ProductInfo;
import com.chenshun.eshop.service.CacheService;
import com.chenshun.eshop.util.spring.SpringContext;
import com.chenshun.eshop.util.zookeeper.ZookeeperSession;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * User: chenshun131 <p />
 * Time: 18/5/12 15:15  <p />
 * Version: V1.0  <p />
 * Description: 缓存重建线程 : 不断从 RebuildCacheQueue 中取出队列的值与 Redis 中的数据进行比较，如果比 Redis 中的数据新则覆盖 Redis 中的数据 <p />
 */
@Slf4j
public class RebuildCacheThread implements Runnable {

    @Override
    public void run() {
        RebuildCacheQueue rebuildCacheQueue = RebuildCacheQueue.getInstance();
        ZookeeperSession zkSession = ZookeeperSession.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        CacheService cacheService = (CacheService) SpringContext.getApplicationContext().getBean("cacheService");
        while (true) {
            ProductInfo productInfo = rebuildCacheQueue.takeProductInfo();
            // 获取分布式锁
            zkSession.acquireDistributedLock(productInfo.getId());
            // 获取 Redis 中相应的数据
            ProductInfo existedProductInfo = cacheService.getProductInfoFromRedisCache(productInfo.getId());
            if (existedProductInfo != null) {
                // 比较当前数据的时间版本比已有数据的时间版本是新还是旧
                try {
                    Date date = sdf.parse(productInfo.getModifiedTime());
                    Date existedDate = sdf.parse(existedProductInfo.getModifiedTime());
                    if (date.before(existedDate)) {
                        log.debug("current date[{}] is before existed date[{}]", productInfo.getModifiedTime(),
                                existedProductInfo.getModifiedTime());
                        continue;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                log.debug("current date[{}] is after existed date[{}]", productInfo.getModifiedTime(), existedProductInfo.getModifiedTime());
            } else {
                log.debug("existed product info is null......");
            }
            // 更新 本地缓存 和 Redis 缓存
            cacheService.saveProductInfo2LocalCache(productInfo);
            cacheService.saveProductInfo2RedisCache(productInfo);
            // 释放分布式锁
            zkSession.releaseDistributedLock(productInfo.getId());
        }
    }

}
