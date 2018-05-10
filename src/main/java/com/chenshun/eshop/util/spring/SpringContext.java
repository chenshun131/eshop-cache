package com.chenshun.eshop.util.spring;

import org.springframework.context.ApplicationContext;

/**
 * User: mew <p />
 * Time: 18/5/10 17:11  <p />
 * Version: V1.0  <p />
 * Description: spring上下文 <p />
 */
public class SpringContext {

    private static ApplicationContext applicationContext;

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static void setApplicationContext(ApplicationContext applicationContext) {
        SpringContext.applicationContext = applicationContext;
    }

}
