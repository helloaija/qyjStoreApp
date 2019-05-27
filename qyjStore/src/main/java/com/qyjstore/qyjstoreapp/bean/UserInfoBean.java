package com.qyjstore.qyjstoreapp.bean;

/**
 * @Author shitl
 * @Description 用户信息类
 * @date 2019-05-27
 */
public class UserInfoBean {
    private long userId;
    private String userName;

    public UserInfoBean() {}

    public UserInfoBean(long userId, String userName) {
        this.userId = userId;
        this.userName = userName;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
