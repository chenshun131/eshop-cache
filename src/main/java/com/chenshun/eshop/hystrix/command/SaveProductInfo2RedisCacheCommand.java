package com.chenshun.eshop.hystrix.command;

import com.alibaba.fastjson.JSONObject;
import com.chenshun.eshop.model.ProductInfo;
import com.chenshun.eshop.util.SerializationUtil;
import com.chenshun.eshop.util.spring.SpringContext;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.JedisCluster;

/**
 * User: mew <p />
 * Time: 18/5/24 11:55  <p />
 * Version: V1.0  <p />
 * Description:  <p />
 */
@Slf4j
public class SaveProductInfo2RedisCacheCommand extends HystrixCommand<Boolean> {

    /** 设置缓存时间一小时 */
    private int timeout = 60 * 60;

    private ProductInfo productInfo;

    public SaveProductInfo2RedisCacheCommand(ProductInfo productInfo) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("RedisGroup"))
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                        .withExecutionTimeoutInMilliseconds(100)
                        .withCircuitBreakerRequestVolumeThreshold(1000)
                        .withCircuitBreakerErrorThresholdPercentage(70)
                        .withCircuitBreakerSleepWindowInMilliseconds(60 * 1000)));
        this.productInfo = productInfo;
    }

    @Override
    protected Boolean run() throws Exception {
        JedisCluster jedisCluster = SpringContext.getApplicationContext().getBean(JedisCluster.class);
        String key = "product_info_" + productInfo.getId();

        String value = JSONObject.toJSONString(productInfo);
        jedisCluster.setex(SerializationUtil.serialize(key), timeout, SerializationUtil.serialize(value));
        log.debug("save ProductInfo to Redis => {}={}", key, value);
        return true;
    }

    @Override
    protected Boolean getFallback() {
        return false;
    }

}
