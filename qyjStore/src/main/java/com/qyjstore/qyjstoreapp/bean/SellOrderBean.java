package com.qyjstore.qyjstoreapp.bean;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author shitl
 * @Description 销售单
 * @date 2019-05-20
 */
public class SellOrderBean {
    /** 订单编号 */
    private String orderNumber;
    /** 购买人 */
    private String userName;
    /** 销售时间 */
    private Date orderTime;
    /** 订单金额 */
    private BigDecimal orderAmount;
    /** 订单利润 */
    private BigDecimal profitAmount;
    /** 订单状态 */
    private String orderStatus;

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

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public BigDecimal getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(BigDecimal orderAmount) {
        this.orderAmount = orderAmount;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public BigDecimal getProfitAmount() {
        return profitAmount;
    }

    public void setProfitAmount(BigDecimal profitAmount) {
        this.profitAmount = profitAmount;
    }
}
