package com.chenshun.eshop.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User: mew <p />
 * Time: 18/5/10 10:33  <p />
 * Version: V1.0  <p />
 * Description: 商品信息 <p />
 */
@NoArgsConstructor
@Data
public class ProductInfo {

    private Long id;

    private String name;

    private Double price;

    private String pictureList;

    private String specification;

    private String service;

    private String color;

    private String size;

    private Long shopId;

    private String modifiedTime;

}
