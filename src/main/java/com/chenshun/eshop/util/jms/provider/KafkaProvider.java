package com.chenshun.eshop.util.jms.provider;

import com.alibaba.fastjson.JSONObject;
import com.chenshun.eshop.model.Message;
import com.chenshun.eshop.model.ProductInfo;
import com.chenshun.eshop.model.ShopInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * User: mew <p />
 * Time: 18/5/11 09:24  <p />
 * Version: V1.0  <p />
 * Description: Kafka 生产者 <p />
 */
@Component
public class KafkaProvider {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendProductInfo() {
        Message message = new Message("productInfoService");

        ProductInfo productInfo = new ProductInfo();
        productInfo.setId(1L);
        productInfo.setName("iphone7手机");
        productInfo.setPrice(5599d);
        productInfo.setPictureList("a.jpg,b.jpg");
        productInfo.setSpecification("iphone7的规格");
        productInfo.setService("iphone7的售后服务");
        productInfo.setColor("红色,白色,黑色");
        productInfo.setSize("5.5");
        productInfo.setShopId(1L);

        message.setData(productInfo);
        String data = JSONObject.toJSONString(message);
        logger.debug("发送 ProductInfo 消息[cache-message] => {}", data);
        kafkaTemplate.send("cache-message", data);
    }

    public void sendShopInfo() {
        Message message = new Message("shopInfoService");

        ShopInfo shopInfo = new ShopInfo();
        shopInfo.setId(1L);
        shopInfo.setName("小王的手机店");
        shopInfo.setLevel(5);
        shopInfo.setGoodCommentRate(0.99d);

        message.setData(shopInfo);
        String data = JSONObject.toJSONString(message);
        logger.debug("发送 ShopInfo 消息[cache-message] => {}", data);
        kafkaTemplate.send("cache-message", data);
    }

}
