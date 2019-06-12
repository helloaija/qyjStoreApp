package com.qyjstore.qyjstoreapp.bean;

import java.io.Serializable;

/**
 * @Author shitl
 * @Description 销售产品
 * @date 2019-05-20
 */
public class SellProductBean implements Serializable {
    private Long id;

    /** 产品ID */
    private Long productId;

    /** 产品名称 */
    private String productTitle;
    /** 单位 */
    private String productUnit;
    /** 进价 */
    private Double stockAmount;
    /** 售价 */
    private Double sellAmount;
    /** 数量 */
    private Integer number;

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

    public Double getStockAmount() {
        return stockAmount;
    }

    public void setStockAmount(Double stockAmount) {
        this.stockAmount = stockAmount;
    }

    public Double getSellAmount() {
        return sellAmount;
    }

    public void setSellAmount(Double sellAmount) {
        this.sellAmount = sellAmount;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }
}
