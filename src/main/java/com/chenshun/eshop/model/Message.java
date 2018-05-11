package com.chenshun.eshop.model;

/**
 * User: mew <p />
 * Time: 18/5/11 10:30  <p />
 * Version: V1.0  <p />
 * Description:  <p />
 */
public class Message {

    private String serviceId;

    private Object data;

    public Message() {
    }

    public Message(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

}
