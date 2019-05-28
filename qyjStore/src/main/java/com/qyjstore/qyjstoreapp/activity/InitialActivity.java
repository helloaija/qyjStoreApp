package com.qyjstore.qyjstoreapp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import com.alibaba.fastjson.JSONObject;
import com.qyjstore.qyjstoreapp.R;
import com.qyjstore.qyjstoreapp.base.BaseActivity;
import com.qyjstore.qyjstoreapp.base.UserManager;
import com.qyjstore.qyjstoreapp.utils.AppUtil;
import com.qyjstore.qyjstoreapp.utils.ConstantUtil;
import com.qyjstore.qyjstoreapp.utils.HttpUtil;
import com.qyjstore.qyjstoreapp.utils.ToastUtil;

import java.lang.ref.WeakReference;

/**
 * @Author shitl
 * @Description 启动页
 * @date 2019-05-21
 */
public class InitialActivity extends BaseActivity {

    /** 首页标志 */
    private static final int TO_HOME = 1000;
    /** 登录页标志 */
    private static final int TO_LOGIN = 1001;
    /** 设置手势密码页标志 */
    private static final int TO_GESTURE_SETTING = 1002;
    /** 验证手势密码页标志 */
    private static final int TO_GESTURE_VERIFY = 1003;

    /** 跳转延迟时间 */
    private static final long GO_DELAY_MILLIS = 1500;

    private Handler mHandler = new GoPageHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial);

        // 加载用户数据
        this.loadUserInfo();
    }

    /**
     * 加载用户数据
     */
    private void loadUserInfo() {
        SharedPreferences sp = this.getSharedPreferences(ConstantUtil.PRO_NAME_USER_INFO, Context.MODE_PRIVATE);
        // 获取登录状态
        boolean isLoginState = sp.getBoolean("isLoginState", false);
        String authentication = sp.getString("authentication", null);
        // 如果isLoginState为false或者authentication为空，就跳到登录页面
        if (!isLoginState || TextUtils.isEmpty(authentication)) {
            mHandler.sendEmptyMessageDelayed(TO_LOGIN, GO_DELAY_MILLIS);
            return;
        }

        // 根据authentication加载用户信息
        UserManager.getInstance().loadUserInfo(new HttpUtil.CallBack() {
            @Override
            public void onSuccess(JSONObject json) {
                Looper.prepare();
                if (!"0000".equals(getResultCode(json))) {
                    ToastUtil.makeText(InitialActivity.this, getResultMessage(json));
                } else {
                    // 判断跳转到哪个页面
                    toPage();
                }
                Looper.loop();
            }

            @Override
            public void onError(int responseCode, String msg) {
                Looper.prepare();
                if (AppUtil.handleLoginExpire(InitialActivity.this, responseCode)) {
                    Looper.loop();
                    return;
                }

                ToastUtil.makeText(InitialActivity.this, "系统异常");
                Looper.loop();
            }
        });
    }

    /**
     * 判断跳转哪个页面
     * 1、如果isLoginState为false或者authentication为空，就跳到登录页面
     * 2、根据authentication获取用户信息。
     * ①如果登录过期，就跳到登录页面。
     * ②如果登录没过期：a)没有设置手势密码就去设置手势密码页面；b)已经设置手势密码就去验证手势密码页面。
     */
    private void toPage() {
        // 获取用户id
        long userId = UserManager.getInstance().getUser().getUserId();
        // 登录过期跳到登录页
        if (userId == 0) {
            mHandler.sendEmptyMessageDelayed(TO_LOGIN, GO_DELAY_MILLIS);
            return;
        }

        SharedPreferences sp = this.getSharedPreferences(ConstantUtil.PRO_NAME_USER_INFO, Context.MODE_PRIVATE);

        long oldUserId = sp.getLong("userId", 0);
        // 如果保存的用户id和获取到的用户id不一致，要重新设置手势密码
        if (oldUserId != userId) {
            SharedPreferences.Editor spEditor = sp.edit();
            spEditor.putLong("userId", userId);
            spEditor.putString("gesturePassword", "");
            spEditor.apply();
            mHandler.sendEmptyMessageDelayed(TO_GESTURE_SETTING, GO_DELAY_MILLIS);
            return;
        }

        // 获取手势密码
        String gesturePassword = sp.getString("gesturePassword", null);
        // 没有设置手势密码，就去设置手势密码页
        if (TextUtils.isEmpty(gesturePassword)) {
            mHandler.sendEmptyMessageDelayed(TO_GESTURE_SETTING, GO_DELAY_MILLIS);
            return;
        }

        // 跳到验证手势密码页面
        mHandler.sendEmptyMessageDelayed(TO_GESTURE_VERIFY, GO_DELAY_MILLIS);

        finish();
    }


    /**
     * 跳转页面
     * @param msg
     */
    private void handlerToPage(Message msg) {
        switch (msg.what) {
            case TO_HOME:
                toHome();
                break;
            case TO_LOGIN:
                toLogin();
                break;
            case TO_GESTURE_SETTING:
                setToGestureSetting();
                break;
            case TO_GESTURE_VERIFY:
                toToGestureVerify();
                break;
            default:
                break;
        }
        finish();
    }

    /**
     * 跳转到主页面
     */
    private void toHome() {
        Intent intent = new Intent(InitialActivity.this, GestureEditActivity.class);
        startActivity(intent);
    }

    /**
     * 跳转到登录页面
     */
    private void toLogin() {
        Intent intent = new Intent(InitialActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    /**
     * 跳转到设置手势密码页面
     */
    private void setToGestureSetting() {
        Intent intent = new Intent(InitialActivity.this, GestureEditActivity.class);
        startActivity(intent);
    }

    /**
     * 跳转到验证手势密码页面
     */
    private void toToGestureVerify() {
        Intent intent = new Intent(InitialActivity.this, MainActivity.class);
        startActivity(intent);
        intent = new Intent(InitialActivity.this, GestureVerifyActivity.class);
        startActivity(intent);
    }

    /**
     * 静态内部类-处理页面跳转
     */
    private static class GoPageHandler extends Handler {
        WeakReference<InitialActivity> weakReference;

        private GoPageHandler(InitialActivity initialActivity) {
            weakReference = new WeakReference<>(initialActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            weakReference.get().handlerToPage(msg);
            super.handleMessage(msg);
        }
    }
}
