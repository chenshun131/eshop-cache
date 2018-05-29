package com.chenshun.eshop.model;

import lombok.Data;

/**
 * User: mew <p />
 * Time: 18/5/11 10:30  <p />
 * Version: V1.0  <p />
 * Description:  <p />
 */
@Data
public class Message {

    private String serviceId;

    private Object data;

    public Message() {
    }

    public Message(String serviceId) {
        this.serviceId = serviceId;
    }

}
