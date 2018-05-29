package com.chenshun.eshop.util.listener;

import com.chenshun.eshop.util.rebuild.RebuildCacheThread;
import com.chenshun.eshop.util.spring.SpringContext;
import com.chenshun.eshop.util.thread.WorkThreadFactory;
import com.chenshun.eshop.util.zookeeper.ZookeeperSession;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * User: mew <p />
 * Time: 18/5/10 17:57  <p />
 * Version: V1.0  <p />
 * Description: 系统初始化的监听器 <p />
 */
@WebListener
public class InitListener implements ServletContextListener {

    private static int THREAD_COUNT = 1;

    private ThreadPoolExecutor threadPoolExecutor;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // init SpringContext
        ServletContext sc = sce.getServletContext();
        ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(sc);
        SpringContext.setApplicationContext(context);

        // init zookeeper
        ZookeeperSession.init();

        // 通过线程池管理线程
        threadPoolExecutor = new ThreadPoolExecutor(THREAD_COUNT, THREAD_COUNT, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(), new WorkThreadFactory());
        // 启动 缓存重建线程
        threadPoolExecutor.execute(new RebuildCacheThread());
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // 关闭线程
        threadPoolExecutor.shutdownNow();
    }

}
