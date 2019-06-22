package com.qyjstore.qyjstoreapp.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 进货订单商品
 * @author CTF_stone
 */
public class StockProductBean implements Serializable, Cloneable {

    private Long id;

    /**
     * 订单id
     */
    private Long stockId;

    /**
     * 产品id
     */
    private Long productId;

    /**
     * 产品名称
     */
    private String productTitle;

    /**
     * 产品单位
     */
    private String productUnit;

    /**
     * 产品单价
     */
    private BigDecimal price;

    /**
     * 购买数量
     */
    private Integer number;

    /**
     * 创建时间
     */
    private Date createTime;

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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getStockId() {
        return stockId;
    }

    public void setStockId(Long stockId) {
        this.stockId = stockId;
    }

    public String getProductUnit() {
        return productUnit;
    }

    public void setProductUnit(String productUnit) {
        this.productUnit = productUnit;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}