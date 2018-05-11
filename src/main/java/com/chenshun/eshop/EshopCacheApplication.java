package com.chenshun.eshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.EnableKafka;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashSet;
import java.util.Set;

@EnableKafka
@EnableCaching
@SpringBootApplication
public class EshopCacheApplication {

    private String host = "ci-server";

    @Bean
    public JedisCluster jedisCluster() {
        // 数据库链接池配置
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(100);
        config.setMaxIdle(50);
        config.setMinIdle(20);
        config.setMaxWaitMillis(6 * 1000);
        config.setTestOnBorrow(true);
        // Redis集群的节点集合
        Set<HostAndPort> jedisClusterNodes = new HashSet<>();
        jedisClusterNodes.add(new HostAndPort(host, 7111));
        jedisClusterNodes.add(new HostAndPort(host, 7112));
        jedisClusterNodes.add(new HostAndPort(host, 7113));
        jedisClusterNodes.add(new HostAndPort(host, 7114));
        jedisClusterNodes.add(new HostAndPort(host, 7115));
        jedisClusterNodes.add(new HostAndPort(host, 7116));
        // 根据节点集创集群链接对象
        return new JedisCluster(jedisClusterNodes, 2000, 2000, 100, config);
    }

    public static void main(String[] args) {
        SpringApplication.run(EshopCacheApplication.class, args);
    }

}
