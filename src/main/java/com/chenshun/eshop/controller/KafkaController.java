package com.chenshun.eshop.controller;

import com.chenshun.eshop.util.jms.provider.KafkaProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * User: mew <p />
 * Time: 18/5/11 10:36  <p />
 * Version: V1.0  <p />
 * Description:  <p />
 */
@RequestMapping
@RestController
public class KafkaController {

    @Autowired
    private KafkaProvider kafkaProvider;

    @RequestMapping("sendProductInfo")
    public String sendProductInfo() {
        kafkaProvider.sendProductInfo();
        return "success";
    }

    @RequestMapping("sendShopInfo")
    public String sendShopInfo() {
        kafkaProvider.sendShopInfo();
        return "success";
    }

}
