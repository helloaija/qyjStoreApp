package com.qyjstore.qyjstoreapp.base;

import android.util.Log;
import com.alibaba.fastjson.JSONObject;
import com.qyjstore.qyjstoreapp.bean.UserInfoBean;
import com.qyjstore.qyjstoreapp.utils.ConfigUtil;
import com.qyjstore.qyjstoreapp.utils.HttpUtil;

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

    /**
     * 加载用户数据
     */
    public void loadUserInfo(final HttpUtil.CallBack callBack) {
        HttpUtil.doGetAsyn(ConfigUtil.SYS_SERVICE_GET_USER_INFO, null, new HttpUtil.CallBack() {
            @Override
            public void onSuccess(JSONObject json) {
                if ("0000".equals(json.getString("resultCode"))) {
                    JSONObject userInfoJson = json.getJSONObject("result");
                    convertUserJson(userInfoJson);
                }
                callBack.onSuccess(json);
            }

            @Override
            public void onError(int responseCode, String msg) {
                callBack.onError(responseCode, msg);
            }
        });
    }

    public void convertUserJson(JSONObject userInfoJson) {
        this.userInfoBean.setUserId(userInfoJson.getLong("id"));
        this.userInfoBean.setUserName(userInfoJson.getString("userName"));
    }
}
