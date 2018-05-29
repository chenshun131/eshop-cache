package com.chenshun.eshop.hystrix.command;

import com.alibaba.fastjson.JSONObject;
import com.chenshun.eshop.model.ShopInfo;
import com.chenshun.eshop.util.SerializationUtil;
import com.chenshun.eshop.util.spring.SpringContext;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.JedisCluster;

/**
 * User: mew <p />
 * Time: 18/5/24 12:29  <p />
 * Version: V1.0  <p />
 * Description:  <p />
 */
@Slf4j
public class SaveShopInfo2RedisCacheCommand extends HystrixCommand<Boolean> {

    /** 设置缓存时间一小时 */
    private int timeout = 60 * 60;

    private ShopInfo shopInfo;

    public SaveShopInfo2RedisCacheCommand(ShopInfo shopInfo) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("RedisGroup"))
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                        .withExecutionTimeoutInMilliseconds(100)
                        .withCircuitBreakerRequestVolumeThreshold(1000)
                        .withCircuitBreakerErrorThresholdPercentage(70)
                        .withCircuitBreakerSleepWindowInMilliseconds(60 * 1000)));
        this.shopInfo = shopInfo;
    }

    @Override
    protected Boolean run() throws Exception {
        JedisCluster jedisCluster = SpringContext.getApplicationContext().getBean(JedisCluster.class);
        String key = "shop_info_" + shopInfo.getId();

        String value = JSONObject.toJSONString(shopInfo);
        jedisCluster.setex(SerializationUtil.serialize(key), timeout, SerializationUtil.serialize(value));
        log.debug("save ShopInfo to Redis => {}={}", key, value);
        return true;
    }

    @Override
    protected Boolean getFallback() {
        return false;
    }

}
