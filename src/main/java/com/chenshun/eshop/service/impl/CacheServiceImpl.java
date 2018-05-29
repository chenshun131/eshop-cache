package com.chenshun.eshop.service.impl;

import com.chenshun.eshop.hystrix.command.GetProductInfoFromRedisCacheCommand;
import com.chenshun.eshop.hystrix.command.GetShopInfoFromRedisCacheCommand;
import com.chenshun.eshop.hystrix.command.SaveProductInfo2RedisCacheCommand;
import com.chenshun.eshop.hystrix.command.SaveShopInfo2RedisCacheCommand;
import com.chenshun.eshop.model.ProductInfo;
import com.chenshun.eshop.model.ShopInfo;
import com.chenshun.eshop.service.CacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * User: mew <p />
 * Time: 18/5/10 10:36  <p />
 * Version: V1.0  <p />
 * Description: 缓存实现类 <p />
 */
@Slf4j
@CacheConfig(cacheNames = "cacheService")
@Service
public class CacheServiceImpl implements CacheService {

    private static final String CACHE_NAME = "local";

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
    public Boolean saveProductInfo2RedisCache(ProductInfo productInfo) {
        return new SaveProductInfo2RedisCacheCommand(productInfo).execute();
    }

    @Override
    public ProductInfo getProductInfoFromRedisCache(Long productId) {
        return new GetProductInfoFromRedisCacheCommand(productId).execute();
    }

    /**
     * 将店铺信息保存到本地的 Redis 缓存中
     *
     * @param shopInfo
     */
    @Override
    public Boolean savShopInfo2RedisCache(ShopInfo shopInfo) {
        return new SaveShopInfo2RedisCacheCommand(shopInfo).execute();
    }

    @Override
    public ShopInfo getShopInfoFromRedisCache(Long shopId) {
        return new GetShopInfoFromRedisCacheCommand(shopId).execute();
    }

}
