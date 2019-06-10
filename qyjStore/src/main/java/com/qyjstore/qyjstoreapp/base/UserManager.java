package com.qyjstore.qyjstoreapp.base;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qyjstore.qyjstoreapp.bean.UserInfoBean;
import com.qyjstore.qyjstoreapp.utils.ConfigUtil;
import com.qyjstore.qyjstoreapp.utils.OkHttpUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import java.io.IOException;

/**
 * @Author shitl
 * @Description 用户管理类
 * @date 2019-05-27
 */
public class UserManager {

    private final UserInfoBean userInfoBean = new UserInfoBean();
    private final static UserManager INSTANCE = new UserManager();

    private UserManager() {
    }

    public static UserManager getInstance() {
        return INSTANCE;
    }

    /**
     * 获取用户数据
     * @return
     */
    public UserInfoBean getUser() {
        return userInfoBean;
    }

    public void loadUserInfo(final Callback callBack) {
        OkHttpUtil.doGet(ConfigUtil.SYS_SERVICE_GET_USER_INFO, callBack);
    }

    /**
     * 加载用户数据
     */
    public void loadUserInfo(final OkHttpUtil.HttpCallBack callBack) {
        OkHttpUtil.doGet(ConfigUtil.SYS_SERVICE_GET_USER_INFO, new OkHttpUtil.HttpCallBack(callBack.getContext()) {
            @Override
            public void onFailure(Call call, IOException e) {
                callBack.onFailure(call, e);
            }

            @Override
            public void onResponse(Call call, Response response, String responseText) {
                JSONObject responseJson = JSON.parseObject(responseText);
                if ("0000".equals(responseJson.getString("resultCode"))) {
                    JSONObject userInfoJson = responseJson.getJSONObject("result");
                    convertUserJson(userInfoJson);
                }
                callBack.onResponse(call, response, responseText);
            }
        });
    }

    public void convertUserJson(JSONObject userInfoJson) {
        this.userInfoBean.setUserId(userInfoJson.getLong("id"));
        this.userInfoBean.setUserName(userInfoJson.getString("userName"));
    }
}
