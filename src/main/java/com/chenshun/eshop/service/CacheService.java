package com.chenshun.eshop.service;

import com.chenshun.eshop.model.ProductInfo;
import com.chenshun.eshop.model.ShopInfo;

/**
 * User: mew <p />
 * Time: 18/5/10 10:32  <p />
 * Version: V1.0  <p />
 * Description: 缓存 <p />
 */
public interface CacheService {

    /**
     * 将商品信息保存到本地缓存中
     *
     * @param productInfo
     * @return
     */
    ProductInfo saveLocalCache(ProductInfo productInfo);

    /**
     * 从本地缓存中获取商品信息
     *
     * @param id
     * @return
     */
    ProductInfo getLocalCache(Long id);

    /**
     * 将商品信息保存到本地的 Ehcache 缓存中
     *
     * @param productInfo
     * @return
     */
    ProductInfo saveProductInfo2LocalCache(ProductInfo productInfo);

    /**
     * 从本地 ehcache 缓存中获取商品信息
     *
     * @param productId
     * @return
     */
    ProductInfo getProductInfoFromLocalCache(Long productId);

    /**
     * 将店铺信息保存到 Ehcache 缓存中
     *
     * @param shopInfo
     * @return
     */
    ShopInfo saveShopInfo2LocalCache(ShopInfo shopInfo);

    /**
     * 从本地 ehcache 缓存中获取店铺信息
     *
     * @param shopId
     * @return
     */
    ShopInfo getShopInfoFromLocalCache(Long shopId);

    /**
     * 将商品信息保存到本地的 Redis 缓存中
     *
     * @param productInfo
     */
    void saveProductInfo2RedisCache(ProductInfo productInfo);

    /**
     * 从 Redis 中获取商品信息
     *
     * @param productId
     * @return
     */
    ProductInfo getProductInfoFromRedisCache(Long productId);

    /**
     * 将店铺信息保存到本地的 Redis 缓存中
     *
     * @param shopInfo
     */
    void savShopInfo2RedisCache(ShopInfo shopInfo);

    /**
     * 从 Redis 中获取店铺信息
     *
     * @param shopId
     * @return
     */
    ShopInfo getShopInfoFromRedisCache(Long shopId);

}
