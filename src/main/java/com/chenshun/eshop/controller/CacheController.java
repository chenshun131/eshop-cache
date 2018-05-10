package com.chenshun.eshop.controller;

import com.chenshun.eshop.model.ProductInfo;
import com.chenshun.eshop.service.CacheService;
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

}
