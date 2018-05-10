package com.chenshun.eshop.dao;

import com.chenshun.eshop.util.SerializationUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.JedisCluster;

import javax.annotation.Resource;

/**
 * User: mew <p />
 * Time: 18/5/7 09:41  <p />
 * Version: V1.0  <p />
 * Description: Redis 操作类 <p />
 */
@CacheConfig(cacheNames = "redisCache")
@Repository
public class RedisDAO {

    /** 设置缓存时间一小时 */
    @Value("${common.redis.timeout}")
    private int timeout;

    @Resource
    private JedisCluster jedisCluster;

    public String set(String key, String value) {
        return jedisCluster.setex(SerializationUtil.serialize(key), timeout, SerializationUtil.serialize(value));
    }

    public String get(String key) {
        byte[] result = jedisCluster.get(SerializationUtil.serialize(key));
        return SerializationUtil.deserialize(result, String.class);
    }

    /**
     * delete redis data
     *
     * @param key
     */
    public void delete(String key) {
        jedisCluster.del(SerializationUtil.serialize(key));
    }

}
