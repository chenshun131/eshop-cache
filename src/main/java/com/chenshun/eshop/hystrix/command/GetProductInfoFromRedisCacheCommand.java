package com.chenshun.eshop.hystrix.command;

import com.chenshun.eshop.model.ProductInfo;
import com.chenshun.eshop.util.SerializationUtil;
import com.chenshun.eshop.util.spring.SpringContext;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;
import redis.clients.jedis.JedisCluster;

/**
 * User: mew <p />
 * Time: 18/5/24 11:38  <p />
 * Version: V1.0  <p />
 * Description:  <p />
 */
public class GetProductInfoFromRedisCacheCommand extends HystrixCommand<ProductInfo> {

    private Long productId;

    public GetProductInfoFromRedisCacheCommand(Long productId) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("RedisGroup"))
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                        .withExecutionTimeoutInMilliseconds(100)
                        .withCircuitBreakerRequestVolumeThreshold(1000)
                        .withCircuitBreakerErrorThresholdPercentage(70)
                        .withCircuitBreakerSleepWindowInMilliseconds(60 * 1000)));
        this.productId = productId;
    }

    @Override
    protected ProductInfo run() throws Exception {
        JedisCluster jedisCluster = SpringContext.getApplicationContext().getBean(JedisCluster.class);
        String key = "product_info_" + productId;
        byte[] result = jedisCluster.get(SerializationUtil.serialize(key));
        if (result == null) {
            return null;
        }
        return SerializationUtil.deserialize(result, ProductInfo.class);
    }

    @Override
    protected ProductInfo getFallback() {
        return null;
    }

}
