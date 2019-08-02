package com.qyjstore.qyjstoreapp.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author shitl
 * @Description 销售产品
 * @date 2019-05-20
 */
public class SellProductBean implements Serializable, Cloneable {
    private Long id;

    /**
     * 产品ID
     */
    private Long productId;

    /**
     * 产品名称
     */
    private String productTitle;
    /**
     * 单位
     */
    private String productUnit;
    /**
     * 进价
     */
    private Double stockPrice;
    /**
     * 售价
     */
    private Double price;
    /**
     * 数量
     */
    private Integer number;

    /** 单据编号 */
    private String orderNumber;

    /** 交易时间 */
    private Date orderTime;

    /** 购买人 */
    private String userName;

    /** 订单状态 */
    private String orderStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public String getProductUnit() {
        return productUnit;
    }

    public void setProductUnit(String productUnit) {
        this.productUnit = productUnit;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Double getStockPrice() {
        return stockPrice;
    }

    public void setStockPrice(Double stockPrice) {
        this.stockPrice = stockPrice;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
