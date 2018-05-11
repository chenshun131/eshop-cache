package com.chenshun.eshop.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * User: mew <p />
 * Time: 18/5/11 16:53  <p />
 * Version: V1.0  <p />
 * Description:  <p />
 */
@RestController
public class TestController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping("hello1/hello")
    public String hello1(String productId) {
        logger.debug("hello1 >>>>>>>>>>>>>> productId= {}", productId);
        return "hello1";
    }

    @RequestMapping("hello2/hello")
    public String hello2(String productId) {
        logger.debug("hello2 >>>>>>>>>>>>>> productId= {}", productId);
        return "hello2";
    }

}
