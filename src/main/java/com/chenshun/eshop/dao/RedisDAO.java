package com.chenshun.eshop.dao;

import com.chenshun.eshop.util.SerializationUtil;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.JedisCluster;

import javax.annotation.Resource;

/**
 * User: mew <p />
 * Time: 18/5/7 09:41  <p />
 * Version: V1.0  <p />
 * Description: Redis 操作类 <p />
 */
@Repository
public class RedisDAO {

    /** 设置缓存时间一小时 */
    private int timeout = 60 * 60;

    @Resource
    private JedisCluster jedisCluster;

    public String set(String key, String value) {
        return jedisCluster.setex(SerializationUtil.serialize(key), timeout, SerializationUtil.serialize(value));
    }

    public String get(String key) {
        byte[] result = jedisCluster.get(SerializationUtil.serialize(key));
        if (result == null) {
            return null;
        }
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
