package com.chenshun.eshop.hystrix.command;

import com.chenshun.eshop.model.ProductInfo;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixThreadPoolProperties;

/**
 * User: mew <p />
 * Time: 18/5/24 14:55  <p />
 * Version: V1.0  <p />
 * Description:  <p />
 */
public class GetProductInfoCommand extends HystrixCommand<ProductInfo> {

    private Long productId;

    public GetProductInfoCommand(Long productId) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("ProductService"))
                .andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter()
                        .withCoreSize(10)
                        .withMaximumSize(30)
                        .withAllowMaximumSizeToDivergeFromCoreSize(true)
                        .withKeepAliveTimeMinutes(1)
                        .withMaxQueueSize(50)
                        .withQueueSizeRejectionThreshold(100)));
        this.productId = productId;
    }

    @Override
    protected ProductInfo run() throws Exception {
        // TODO 此处应该为访问 MySQL数据库操作
        ProductInfo productInfo = new ProductInfo();
        productInfo.setId(this.productId);
        productInfo.setName("iphone7手机");
        productInfo.setPrice(5599d);
        productInfo.setPictureList("a.jpg,b.jpg");
        productInfo.setSpecification("iphone7的规格");
        productInfo.setService("iphone7的售后服务");
        productInfo.setColor("红色,白色,黑色");
        productInfo.setSize("5.5");
        productInfo.setShopId(1L);
        productInfo.setModifiedTime("2018-05-24 15:02:00");

        return productInfo;
    }

    @Override
    protected ProductInfo getFallback() {
        return new HBaseColdDataCommand(this.productId).execute();
    }

    private class HBaseColdDataCommand extends HystrixCommand<ProductInfo> {

        private Long productId;

        public HBaseColdDataCommand(Long productId) {
            super(HystrixCommandGroupKey.Factory.asKey("HBaseGroup"));
            this.productId = productId;
        }

        @Override
        protected ProductInfo run() throws Exception {
            // TODO 此处应该为访问 HBaae数据库操作
            ProductInfo productInfo = new ProductInfo();
            productInfo.setId(this.productId);
            productInfo.setName("iphone7手机");
            productInfo.setPrice(5599d);
            productInfo.setPictureList("a.jpg,b.jpg");
            productInfo.setSpecification("iphone7的规格");
            productInfo.setService("iphone7的售后服务");
            productInfo.setColor("红色,白色,黑色");
            productInfo.setSize("5.5");
            productInfo.setShopId(1L);
            productInfo.setModifiedTime("2018-05-24 15:02:00");
            return productInfo;
        }

        @Override
        protected ProductInfo getFallback() {
            ProductInfo productInfo = new ProductInfo();
            productInfo.setId(this.productId);
            // TODO 找些残缺的数据拼装上去
            return productInfo;
        }
    }

}
