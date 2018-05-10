package com.chenshun.eshop.service;

import com.chenshun.eshop.model.ProductInfo;

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

}
