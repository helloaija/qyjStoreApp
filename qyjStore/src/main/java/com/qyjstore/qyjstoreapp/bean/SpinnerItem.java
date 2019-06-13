package com.qyjstore.qyjstoreapp.bean;

/**
 * @Author shitl
 * @Description 定义下拉控件item
 * @date 2019-06-13
 */
public class SpinnerItem {
    private String name;
    private String value;

    public SpinnerItem(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return name == null ? "" : name;
    }
}
