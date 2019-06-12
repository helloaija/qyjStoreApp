package com.qyjstore.qyjstoreapp.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import com.qyjstore.qyjstoreapp.activity.InitialActivity;
import com.qyjstore.qyjstoreapp.activity.LoginActivity;
import com.qyjstore.qyjstoreapp.base.BaseApplication;
import org.json.JSONObject;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.Date;

/**
 * @Author shitl
 * @Description
 * @date 2019-05-22
 */
public class AppUtil {

    /**
     * 获取屏幕信息
     * 屏幕宽度（像素）width = dm.widthPixels
     * 屏幕高度（像素）height = dm.heightPixels
     * 屏幕密度 density = dm.density
     * 屏幕密度（dpi） densityDpi = dm.densityDpi
     * 屏幕宽度（dp） screenWidth = (int) (width / density)
     * 屏幕高度（dp） screenHeight = (int) (height / density)
     * @param context
     * @return
     */
    public static DisplayMetrics getWindowDisplayMetrics(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm;
    }

    /**
     * 登录过期跳转登录页面
     * @param mContext
     * @param responseCode
     * @return
     */
    public static Boolean handleLoginExpire(final Context mContext, int responseCode) {
        if (401 == responseCode) {
            Intent intent = new Intent(mContext, LoginActivity.class);
            mContext.startActivity(intent);
            return true;
        }

        return false;
    }

    /**
     * http返回code非200情况
     * @param mContext
     * @param responseCode
     * @return
     */
    public static boolean handleHttpResponseCode(final Context mContext, int responseCode) {
        if (200 == responseCode) {
            return false;
        }

        Looper.prepare();
        if (401 == responseCode) {
            Intent intent = new Intent(mContext, LoginActivity.class);
            mContext.startActivity(intent);
        } else {
            ToastUtil.makeText(mContext, "服务器异常");
        }
        Looper.loop();

        return true;
    }

    /**
     * http请求异常处理
     * @param mContext
     * @param e
     * @return
     */
    public static void handleHttpException(final Context mContext, Exception e) {
        if (e instanceof SocketTimeoutException) {
            ToastUtil.makeText(mContext, "请求超时");
        } else if (e instanceof ConnectException) {
            ToastUtil.makeText(mContext, "服务器连接失败");
        } else {
            ToastUtil.makeText(mContext, "系统异常");
        }
    }

    /**
     * 获取对象toString
     * @param o
     * @return
     */
    public static String getString(Object o) {
        if (o == null) {
            return "";
        }

        return o.toString();
    }

    public static void main(String[] args) {
        System.out.println(getString("12332"));
    }
}
