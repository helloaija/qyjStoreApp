package com.qyjstore.qyjstoreapp.base;

import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import com.qyjstore.qyjstoreapp.bean.UserInfoBean;
import com.qyjstore.qyjstoreapp.utils.AppUtil;
import com.qyjstore.qyjstoreapp.utils.ConfigUtil;
import com.qyjstore.qyjstoreapp.utils.HttpUtil;
import com.qyjstore.qyjstoreapp.utils.ToastUtil;
import org.json.JSONObject;

/**
 * @Author shitl
 * @Description 用户管理类
 * @date 2019-05-27
 */
public class UserManager {

    private final UserInfoBean userInfoBean = new UserInfoBean();
    private static UserManager instance = new UserManager();
    private Context context;
    private int userLoadFinish = 0;

    private UserManager() {
    }

    public static UserManager getInstance() {
        return instance;
    }

    /**
     * 获取用户数据
     * @return
     */
    public UserInfoBean getUser() {
        return userInfoBean;
    }

    /**
     * 加载并获取用户数据
     * @param context
     * @return
     */
    public UserInfoBean loadAndGetUser(Context context) {
        this.context = context;
        if (userInfoBean.getUserId() == 0) {
            this.loadUserInfo();
        }
        for (;;) {
            if (userLoadFinish != 0) {
                break;
            }
        }
        return userInfoBean;
    }

    /**
     * 加载用户数据
     */
    public void loadUserInfo() {
        userLoadFinish = 0;
        try {
            HttpUtil.doGetAsyn(ConfigUtil.SYS_SERVICE_GET_USER_INFO, null, new HttpUtil.CallBack() {
                @Override
                public void onSuccess(JSONObject json) {
                    userLoadFinish = 1;
                    try {
                        if ("0000".equals(json.getString("resultCode"))) {
                            JSONObject userInfoJson = json.getJSONObject("result");
                            convertUserJson(userInfoJson);
                        } else {
                            ToastUtil.makeText(context, "获取用户信息失败！");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(int responseCode, String msg) {
                    if (AppUtil.handleLoginExpire(context, responseCode)) {
                        return;
                    }
                    userLoadFinish = 1;
                    ToastUtil.makeText(context, "系统异常");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            userLoadFinish = 1;
            ToastUtil.makeText(context, "系统异常");
        }


    }

    private void convertUserJson(JSONObject userInfoJson) throws Exception {
        this.userInfoBean.setUserId(userInfoJson.getLong("id"));
        this.userInfoBean.setUserName(userInfoJson.getString("userName"));
    }
}
