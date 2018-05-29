package com.chenshun.eshop.model;

import lombok.Data;

/**
 * User: mew <p />
 * Time: 18/5/10 17:55  <p />
 * Version: V1.0  <p />
 * Description:  <p />
 */
@Data
public class ShopInfo {

    private Long id;

    private String name;

    private Integer level;

    private Double goodCommentRate;

}
