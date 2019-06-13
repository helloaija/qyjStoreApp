package com.qyjstore.qyjstoreapp.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Author shitl
 * @Description 销售单
 * @date 2019-05-20
 */
public class SellOrderBean implements Serializable {
    private Long id;

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
    /** 已支付金额 */
    private BigDecimal hasPayAmount;
    /** 支付时间 */
    private Date payTime;
    /** 备注 */
    private String remark;
    /** 销售单产品 */
    private List<SellProductBean> sellProductList;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<SellProductBean> getSellProductList() {
        return sellProductList;
    }

    public void setSellProductList(List<SellProductBean> sellProductList) {
        this.sellProductList = sellProductList;
    }

    public BigDecimal getHasPayAmount() {
        return hasPayAmount;
    }

    public void setHasPayAmount(BigDecimal hasPayAmount) {
        this.hasPayAmount = hasPayAmount;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "SellOrderBean{" +
                "id=" + id +
                ", orderNumber='" + orderNumber + '\'' +
                ", userName='" + userName + '\'' +
                ", orderTime=" + orderTime +
                ", orderAmount=" + orderAmount +
                ", profitAmount=" + profitAmount +
                ", orderStatus='" + orderStatus + '\'' +
                ", sellProductList=" + sellProductList +
                '}';
    }
}
