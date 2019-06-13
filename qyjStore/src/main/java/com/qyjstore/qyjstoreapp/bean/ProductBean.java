package com.qyjstore.qyjstoreapp.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 产品信息实体类
 * @author CTF_stone
 */
public class ProductBean implements Serializable {

    private static final long serialVersionUID = -5320457761225802641L;

    /** 主键ID **/
    private Long id;

    /** 产品标题 */
    private String title;

    /** 单位价格 */
    private BigDecimal price;

    /** 产品类型 */
    private String productType;

    /** 产品状态 */
    private String productStatus;

    /** 产品库存 */
    private Integer number;

    /** 已售数量 */
    private Integer soldNumber;

    /** 未支付数量 */
    private Integer unpayNumber;

    /** 产品数量单位 */
    private String productUnit;

    /** 产品说明 */
    private String remark;

    /** 版本号 **/
    private Integer version = 0;

    /** 创建人id */
    private Long createUser;

    /** 创建时间 **/
    private Date createTime;

    /** 更新人id */
    private Long updateUser;

    private Date updateTime;

    /** 已经被删除[0：未删除，1：已删除] */
    private int isDel;

    private Long delUser;

    private Date delTime;

    private String[] stockPrices;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(String productStatus) {
        this.productStatus = productStatus;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getSoldNumber() {
        return soldNumber;
    }

    public void setSoldNumber(Integer soldNumber) {
        this.soldNumber = soldNumber;
    }

    public Integer getUnpayNumber() {
        return unpayNumber;
    }

    public void setUnpayNumber(Integer unpayNumber) {
        this.unpayNumber = unpayNumber;
    }

    public String getProductUnit() {
        return productUnit;
    }

    public void setProductUnit(String productUnit) {
        this.productUnit = productUnit;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Long getCreateUser() {
        return createUser;
    }

    public void setCreateUser(Long createUser) {
        this.createUser = createUser;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(Long updateUser) {
        this.updateUser = updateUser;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getIsDel() {
        return isDel;
    }

    public void setIsDel(int isDel) {
        this.isDel = isDel;
    }

    public Long getDelUser() {
        return delUser;
    }

    public void setDelUser(Long delUser) {
        this.delUser = delUser;
    }

    public Date getDelTime() {
        return delTime;
    }

    public void setDelTime(Date delTime) {
        this.delTime = delTime;
    }

    public String[] getStockPrices() {
        return stockPrices;
    }

    public void setStockPrices(String[] stockPrices) {
        this.stockPrices = stockPrices;
    }

    @Override
    public String toString() {
        return "QyjProductEntity{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", price=" + price +
                ", productType='" + productType + '\'' +
                ", productStatus='" + productStatus + '\'' +
                ", number=" + number +
                ", soldNumber=" + soldNumber +
                ", unpayNumber=" + unpayNumber +
                ", productUnit='" + productUnit + '\'' +
                ", remark='" + remark + '\'' +
                ", version=" + version +
                ", createUser=" + createUser +
                ", createTime=" + createTime +
                ", updateUser=" + updateUser +
                ", updateTime=" + updateTime +
                ", isDel=" + isDel +
                ", delUser=" + delUser +
                ", delTime=" + delTime +
                '}';
    }

}