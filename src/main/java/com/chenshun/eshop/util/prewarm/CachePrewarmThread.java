package com.chenshun.eshop.util.prewarm;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chenshun.eshop.model.ProductInfo;
import com.chenshun.eshop.service.CacheService;
import com.chenshun.eshop.util.spring.SpringContext;
import com.chenshun.eshop.util.zookeeper.ZookeeperSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: mew <p />
 * Time: 18/5/14 16:53  <p />
 * Version: V1.0  <p />
 * Description: 缓存预热线程 <p />
 */
public class CachePrewarmThread implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(CachePrewarmThread.class);

    @Override
    public void run() {
        CacheService cacheService = (CacheService) SpringContext.getApplicationContext().getBean("cacheService'");
        ZookeeperSession zkSession = ZookeeperSession.getInstance();
        // 获取 storm taskid 列表
        String taskIdList = zkSession.getNodeData("/taskid-list");
        LOGGER.debug("【CachePrwarmThread获取到taskid列表】taskidList={}", taskIdList);
        if (taskIdList != null && taskIdList.length() > 0) {
            String[] taskListSplited = taskIdList.split(",");
            for (String taskId : taskListSplited) {
                String taskIdLockPath = "/taskid-lock-" + taskId;
                boolean result = zkSession.acquireFastFailedDistributedLock(taskIdLockPath);
                if (!result) {
                    // 创建节点失败，说明有现成已经开始预热数据
                    continue;
                }
                // 正在预热的状态
                String taskIdStatusLockPath = "/taskid-status-lock-" + taskId;
                zkSession.acquireDistributedLock(taskIdStatusLockPath);

                String taskIdStatus = zkSession.getNodeData("/taskid-status-" + taskId);
                LOGGER.debug("【CachePrewarmThread获取task的预热状态】taskid={}, status={}", taskId, taskIdStatus);

                if ("".equals(taskIdStatus)) {
                    // 获取存放在 zookeeper 中的 Topn 数据
                    String productIdList = zkSession.getNodeData("/task-hot-product-list-" + taskId);
                    LOGGER.debug("【CachePrewarmThread获取到task的热门商品列表】productidList={}", productIdList);

                    JSONArray productJSONArray = JSONArray.parseArray(productIdList);
                    for (int i = 0; i < productJSONArray.size(); i++) {
                        Long productId = productJSONArray.getLong(i);
                        // TODO 此处暂时模拟数据
                        String productInfoJSON = "{\"id\": " + productId + ", \"name\": \"iphone7手机\", \"price\": 5599, \"pictureList\":\"a.jpg,b" +
                                ".jpg\", \"specification\": \"iphone7的规格\", \"service\": \"iphone7的售后服务\", \"color\": \"红色,白色,黑色\", \"size\": " +
                                "\"5.5\", \"shopId\": 1, \"modifiedTime\": \"2017-01-01 12:00:00\"}";
                        ProductInfo productInfo = JSONObject.parseObject(productInfoJSON, ProductInfo.class);
                        cacheService.saveProductInfo2LocalCache(productInfo);
                        LOGGER.debug("【CachePrwarmThread将商品数据设置到本地缓存中】productInfo={}", productInfo);
                        cacheService.saveProductInfo2RedisCache(productInfo);
                        LOGGER.debug("【CachePrwarmThread将商品数据设置到redis缓存中】productInfo={]", productInfo);
                    }
                    zkSession.createNode("/taskid-status-" + taskId);
                    zkSession.setNodeData("/taskid-status-" + taskId, "success");
                }
                zkSession.releaseDistributedLock(taskIdStatusLockPath);
                zkSession.releaseDistributedLock(taskIdLockPath);
            }
        }
    }

}
