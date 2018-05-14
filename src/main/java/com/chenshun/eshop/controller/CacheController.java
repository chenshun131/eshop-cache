package com.chenshun.eshop.controller;

import com.alibaba.fastjson.JSONObject;
import com.chenshun.eshop.model.ProductInfo;
import com.chenshun.eshop.model.ShopInfo;
import com.chenshun.eshop.service.CacheService;
import com.chenshun.eshop.util.prewarm.CachePrewarmThread;
import com.chenshun.eshop.util.rebuild.RebuildCacheQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * User: mew <p />
 * Time: 18/5/10 10:40  <p />
 * Version: V1.0  <p />
 * Description: 缓存 Controller <p />
 */
@RestController
public class CacheController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CacheService cacheService;

    @RequestMapping("testPutCache")
    public String testPutCache(ProductInfo productInfo) {
        cacheService.saveLocalCache(productInfo);
        return "success";
    }

    @RequestMapping("testGetCache")
    public ProductInfo testGetCache(Long id) {
        return cacheService.getLocalCache(id);
    }

    @RequestMapping("getProductInfo")
    public ProductInfo getProductInfo(Long productId) {
        if (productId == null) {
            return null;
        }
        // 先从 Redis 中获取数据
        ProductInfo productInfo = cacheService.getProductInfoFromRedisCache(productId);
        logger.debug("从redis中获取缓存，商品信息={}", productInfo);

        // 如果 Redis 没有数据，再从 Ehcache 中获取数据
        if (productInfo == null) {
            productInfo = cacheService.getProductInfoFromLocalCache(productId);
            logger.debug("从ehcache中获取缓存，商品信息={}", productInfo);
        }

        // 如果 Ehcahce 也没有数据，再从数据源拉取数据，重建缓存
        if (productInfo == null) {
            // TODO 需要从数据源获取数据
            String productInfoJSON = "{\"id\": 2, \"name\": \"iphone7手机\", \"price\": 5599, \"pictureList\":\"a.jpg,b.jpg\", \"specification\": " +
                    "\"iphone7的规格\", \"service\": \"iphone7的售后服务\", \"color\": \"红色,白色,黑色\", \"size\": \"5.5\", \"shopId\": 1, \"modified_time\": " +
                    "\"2017-01-01 12:01:00\"}";
            productInfo = JSONObject.parseObject(productInfoJSON, ProductInfo.class);
            // 将数据推送到一个内存队列中
            RebuildCacheQueue.getInstance().putProductInfo(productInfo);
        }
        return productInfo;
    }

    @RequestMapping("getShopInfo")
    public ShopInfo getShopInfo(Long shopId) {
        if (shopId == null) {
            return null;
        }
        // 先从 Redis 中获取数据
        ShopInfo shopInfo = cacheService.getShopInfoFromRedisCache(shopId);
        logger.debug("从redis中获取缓存，店铺信息={}", shopInfo);

        // 如果 Redis 没有数据，再从 Ehcache 中获取数据
        if (shopInfo == null) {
            shopInfo = cacheService.getShopInfoFromLocalCache(shopId);
            logger.debug("从ehcache中获取缓存，店铺信息={}", shopInfo);
        }

        // 如果 Ehcahce 也没有数据，再从数据源拉取数据，重建缓存
        if (shopInfo == null) {
        }
        return shopInfo;
    }

    /**
     * 预热缓存
     */
    @RequestMapping("prewarmCache")
    public void prewarmCache() {
        new Thread(new CachePrewarmThread()).start();
    }

}
