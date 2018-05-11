package com.chenshun.eshop.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.chenshun.eshop.dao.RedisDAO;
import com.chenshun.eshop.model.ProductInfo;
import com.chenshun.eshop.model.ShopInfo;
import com.chenshun.eshop.service.CacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisCluster;

/**
 * User: mew <p />
 * Time: 18/5/10 10:36  <p />
 * Version: V1.0  <p />
 * Description: 缓存实现类 <p />
 */
@CacheConfig(cacheNames = "cacheService")
@Service
public class CacheServiceImpl implements CacheService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String CACHE_NAME = "local";

    @Autowired
    private RedisDAO redisDAO;

    /**
     * 将商品信息保存到本地缓存中
     *
     * @param productInfo
     * @return
     */
    @CachePut(value = CACHE_NAME, key = "'key_'+#productInfo.getId()")
    @Override
    public ProductInfo saveLocalCache(ProductInfo productInfo) {
        return productInfo;
    }

    /**
     * 从本地缓存中获取商品信息
     *
     * @param id
     * @return
     */
    @Cacheable(value = CACHE_NAME, key = "'key_'+#id")
    @Override
    public ProductInfo getLocalCache(Long id) {
        return null;
    }

    /**
     * 将商品信息保存到本地的 Ehcache 缓存中
     *
     * @param productInfo
     * @return
     */
    @CachePut(value = CACHE_NAME, key = "'product_info_'+#productInfo.getId()")
    @Override
    public ProductInfo saveProductInfo2LocalCache(ProductInfo productInfo) {
        return productInfo;
    }

    /**
     * 从本地 ehcache 缓存中获取商品信息
     *
     * @param productId
     * @return
     */
    @Cacheable(value = CACHE_NAME, key = "'product_info_'+#productId")
    @Override
    public ProductInfo getProductInfoFromLocalCache(Long productId) {
        return null;
    }

    /**
     * 将店铺信息保存到 Ehcache 缓存中
     *
     * @param shopInfo
     * @return
     */
    @CachePut(value = CACHE_NAME, key = "'shop_info_'+#shopInfo.getId()")
    @Override
    public ShopInfo saveShopInfo2LocalCache(ShopInfo shopInfo) {
        return shopInfo;
    }

    /**
     * 从本地 ehcache 缓存中获取店铺信息
     *
     * @param shopId
     * @return
     */
    @Cacheable(value = CACHE_NAME, key = "'shop_info_'+#shopId")
    @Override
    public ShopInfo getShopInfoFromLocalCache(Long shopId) {
        return null;
    }

    /**
     * 将商品信息保存到本地的 Redis 缓存中
     *
     * @param productInfo
     */
    @Override
    public void saveProductInfo2RedisCache(ProductInfo productInfo) {
        String key = "product_info_" + productInfo.getId();
        String value = JSONObject.toJSONString(productInfo);
        redisDAO.set(key, value);
        logger.debug("save ProductInfo to Redis => {}={}", key, value);
    }

    /**
     * 将店铺信息保存到本地的 Redis 缓存中
     *
     * @param shopInfo
     */
    @Override
    public void savShopInfo2RedisCache(ShopInfo shopInfo) {
        String key = "shop_info_" + shopInfo.getId();
        String value = JSONObject.toJSONString(shopInfo);
        redisDAO.set(key, value);
        logger.debug("save ShopInfo to Redis => {}={}", key, value);
    }

}
