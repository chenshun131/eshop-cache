package com.chenshun.eshop.model;

/**
 * User: mew <p />
 * Time: 18/5/10 17:55  <p />
 * Version: V1.0  <p />
 * Description:  <p />
 */
public class ShopInfo {

    private Long id;

    private String name;

    private Integer level;

    private Double goodCommentRate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Double getGoodCommentRate() {
        return goodCommentRate;
    }

    public void setGoodCommentRate(Double goodCommentRate) {
        this.goodCommentRate = goodCommentRate;
    }

}