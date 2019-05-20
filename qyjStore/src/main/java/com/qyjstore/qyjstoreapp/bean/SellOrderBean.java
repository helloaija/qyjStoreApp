package com.qyjstore.qyjstoreapp.bean;

import java.util.Date;

/**
 * @Author shitl
 * @Description 销售单
 * @date 2019-05-20
 */
public class SellOrderBean {
    private String userName;
    private Date orderTime;

    public SellOrderBean() {
    }

    public SellOrderBean(String userName, Date orderTime) {
        this.userName = userName;
        this.orderTime = orderTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }
}
