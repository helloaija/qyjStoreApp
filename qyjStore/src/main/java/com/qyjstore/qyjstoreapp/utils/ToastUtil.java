package com.qyjstore.qyjstoreapp.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * @Author shitl
 * @Description 信息提示工具类
 * @date 2019-05-24
 */
public class ToastUtil {
    public static void makeText(Context context, String msg) {
        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
