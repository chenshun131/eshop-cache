package com.chenshun.eshop.hystrix.command;

import com.chenshun.eshop.model.ShopInfo;
import com.chenshun.eshop.util.SerializationUtil;
import com.chenshun.eshop.util.spring.SpringContext;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;
import redis.clients.jedis.JedisCluster;

/**
 * User: mew <p />
 * Time: 18/5/24 11:52  <p />
 * Version: V1.0  <p />
 * Description:  <p />
 */
public class GetShopInfoFromRedisCacheCommand extends HystrixCommand<ShopInfo> {

    private Long shopId;

    public GetShopInfoFromRedisCacheCommand(Long shopId) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("RedisGroup"))
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                        .withExecutionTimeoutInMilliseconds(100)
                        .withCircuitBreakerRequestVolumeThreshold(1000)
                        .withCircuitBreakerErrorThresholdPercentage(70)
                        .withCircuitBreakerSleepWindowInMilliseconds(60 * 1000)));
        this.shopId = shopId;
    }

    @Override
    protected ShopInfo run() throws Exception {
        JedisCluster jedisCluster = SpringContext.getApplicationContext().getBean(JedisCluster.class);
        String key = "shop_info_" + shopId;
        byte[] result = jedisCluster.get(SerializationUtil.serialize(key));
        if (result == null) {
            return null;
        }
        return SerializationUtil.deserialize(result, ShopInfo.class);
    }

    @Override
    protected ShopInfo getFallback() {
        return null;
    }

}
