package com.chenshun.eshop.util.jms.comsumer;

import com.alibaba.fastjson.JSONObject;
import com.chenshun.eshop.model.ProductInfo;
import com.chenshun.eshop.model.ShopInfo;
import com.chenshun.eshop.service.CacheService;
import com.chenshun.eshop.util.zookeeper.ZookeeperSession;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

/**
 * User: mew <p />
 * Time: 18/5/11 08:44  <p />
 * Version: V1.0  <p />
 * Description: kafka消费者 <p />
 */
@Component
public class KafkaConsumer {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private CacheService cacheService;

    /**
     * 每个主题运行在一个线程中加快数据处理速度
     *
     * @param record
     */
    @KafkaListener(topics = {"cache-message"})
    public void listener(ConsumerRecord<String, String> record) {
        Optional<String> kafkaMessage = Optional.ofNullable(record.value());
        if (kafkaMessage.isPresent()) {
            String message = kafkaMessage.get();
            // 将 message 转换成 JSON 对象
            JSONObject messageJSONObject = JSONObject.parseObject(message);
            // 提取消息中对应服务的标识
            String serviceId = messageJSONObject.getString("serviceId");
            switch (serviceId) {
                case "productInfoService":
                    processProductInfoChangeMessage(messageJSONObject.getObject("data", ProductInfo.class));
                    break;
                case "shopInfoService":
                    processShopInfoChangeMessage(messageJSONObject.getObject("data", ShopInfo.class));
                    break;
                default:
                    logger.warn("该服务类型不在 : {}", serviceId);
                    break;
            }
        }
    }

    /**
     * 处理商品信息变更的消息
     *
     * @param productInfo
     */
    private void processProductInfoChangeMessage(ProductInfo productInfo) {
        // 将数据直接写入 Redis 缓存之前，先获取 zk 分布式锁
        ZookeeperSession zkSession = ZookeeperSession.getInstance();
        zkSession.acquireDistributedLock(productInfo.getId());
        // 获取到锁
        // 先从 Redis 中获取
        ProductInfo existedProductInfo = cacheService.getProductInfoFromRedisCache(productInfo.getId());
        if (existedProductInfo != null) {
            // 比较当前数据的时间版本比已有数据的时间版本是新还是旧
            try {
                Date date = sdf.parse(productInfo.getModifiedTime());
                Date existedDate = sdf.parse(existedProductInfo.getModifiedTime());
                if (date.before(existedDate)) {
                    logger.debug("current date[{}] is before existed date[{}]", productInfo.getModifiedTime(), existedProductInfo.getModifiedTime());
                    return;
                }
                logger.debug("current date[{}] is after existed date[{}]", productInfo.getModifiedTime(), existedProductInfo.getModifiedTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            logger.debug("current date[{}] is after existed date[{}]", productInfo.getModifiedTime(), existedProductInfo.getModifiedTime());
        } else {
            logger.debug("existed product info is null......");
        }
        cacheService.saveProductInfo2LocalCache(productInfo);
        logger.debug("获取刚保存到本地缓存的商品信息：{}", cacheService.getProductInfoFromLocalCache(productInfo.getId()));
        cacheService.saveProductInfo2RedisCache(productInfo);
        // 释放分布式锁
        zkSession.releaseDistributedLock(productInfo.getId());
    }

    /**
     * 处理店铺信息变更的消息
     *
     * @param shopInfo
     */
    private void processShopInfoChangeMessage(ShopInfo shopInfo) {
        cacheService.saveShopInfo2LocalCache(shopInfo);
        logger.debug("获取刚保存到本地缓存的店铺信息：{}", cacheService.getShopInfoFromLocalCache(shopInfo.getId()));
        cacheService.savShopInfo2RedisCache(shopInfo);
    }

//    private static ConsumerConfig createConsumerConfig() {
//        Map<String, String> props = new HashMap<>();
//        // 多个 zookeeper 之间使用逗号隔开
//        props.put("zookeeper.connect", "ci-server:2181");
//        props.put("zookeeper.session.timeout.ms", "40000");
//        props.put("zookeeper.sync.time.ms", "200");
//        return new ConsumerConfig(props);
//    }

}
