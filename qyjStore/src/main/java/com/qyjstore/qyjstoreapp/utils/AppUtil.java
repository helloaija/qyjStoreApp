package com.qyjstore.qyjstoreapp.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

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
}