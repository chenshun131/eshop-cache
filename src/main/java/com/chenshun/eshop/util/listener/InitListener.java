package com.chenshun.eshop.util.listener;

import com.chenshun.eshop.util.spring.SpringContext;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * User: mew <p />
 * Time: 18/5/10 17:57  <p />
 * Version: V1.0  <p />
 * Description:  <p />
 */
@WebListener
public class InitListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // init SpringContext
        ServletContext sc = sce.getServletContext();
        ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(sc);
        SpringContext.setApplicationContext(context);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }

}
