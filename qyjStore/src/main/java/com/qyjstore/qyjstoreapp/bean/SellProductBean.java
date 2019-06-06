package com.qyjstore.qyjstoreapp.bean;

/**
 * @Author shitl
 * @Description 销售产品
 * @date 2019-05-20
 */
public class SellProductBean {
    private Long id;

    /** 产品ID */
    private Long productId;

    /** 产品名称 */
    private String productTitle;
    /** 单位 */
    private String productUnit;
    /** 进价 */
    private double stockAmount;
    /** 售价 */
    private double sellAmount;
    /** 数量 */
    private int number;

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

    public double getStockAmount() {
        return stockAmount;
    }

    public void setStockAmount(double stockAmount) {
        this.stockAmount = stockAmount;
    }

    public double getSellAmount() {
        return sellAmount;
    }

    public void setSellAmount(double sellAmount) {
        this.sellAmount = sellAmount;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
