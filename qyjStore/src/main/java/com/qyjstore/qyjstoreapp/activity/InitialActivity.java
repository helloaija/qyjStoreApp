package com.qyjstore.qyjstoreapp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import com.qyjstore.qyjstoreapp.R;
import com.qyjstore.qyjstoreapp.utils.ConstantUtil;

import java.lang.ref.WeakReference;

/**
 * @Author shitl
 * @Description 启动页
 * @date 2019-05-21
 */
public class InitialActivity extends AppCompatActivity {

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

        // 判断跳转到哪个页面
        this.toPage();
    }

    /**
     * 判断跳转哪个页面
     * 1、如果isLoginState为false或者authentication为空，就跳到登录页面
     * 2、根据authentication获取用户信息。
     *    ①如果登录过期，就跳到登录页面。
     *    ②如果登录没过期：a)没有设置手势密码就去设置手势密码页面；b)已经设置手势密码就去验证手势密码页面。
     *
     */
    private void toPage() {
        SharedPreferences sp = this.getSharedPreferences(ConstantUtil.PRO_NAME_USER_INFO, Context.MODE_PRIVATE);
        // 获取登录状态
        boolean isLoginState = sp.getBoolean("isLoginState", false);
        String authentication = sp.getString("authentication", null);
        // 如果isLoginState为false或者authentication为空，就跳到登录页面
        if (!isLoginState || TextUtils.isEmpty(authentication)) {
            mHandler.sendEmptyMessageDelayed(TO_LOGIN, GO_DELAY_MILLIS);
            return;
        }
        // 根据authentication获取用户信息
        long userId = 0;
        // 登录过期跳到登录页
        if (userId == 0) {
            mHandler.sendEmptyMessageDelayed(TO_LOGIN, GO_DELAY_MILLIS);
            return;
        }

        // 获取手势密码
        String gesturePassword = sp.getString("gesturePassword", null);
        // 没有设置手势密码，就去设置手势密码页
        if (TextUtils.isEmpty(gesturePassword)) {
            mHandler.sendEmptyMessageDelayed(TO_GESTURE_SETTING, GO_DELAY_MILLIS);
            return;
        }

        long oldUserId = sp.getLong("userId", 0);
        // 如果保存的用户id和获取到的用户id不一致，要重新设置手势密码
        if (oldUserId != userId) {
            mHandler.sendEmptyMessageDelayed(TO_GESTURE_SETTING, GO_DELAY_MILLIS);
            SharedPreferences.Editor spEditor = sp.edit();
            spEditor.putLong("userId", userId);
            spEditor.commit();
            return;
        }

        // 跳到验证手势密码页面
        mHandler.sendEmptyMessageDelayed(TO_GESTURE_VERIFY, GO_DELAY_MILLIS);
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
        Intent intent = new Intent(InitialActivity.this, GestureVerifyActivity.class);
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
