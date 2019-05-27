package com.qyjstore.qyjstoreapp.base;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.qyjstore.qyjstoreapp.activity.GestureVerifyActivity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author shitl
 * @Description
 * @date 2019-05-21
 */
public class BaseApplication extends Application {

    /** Application实例 */
    private static BaseApplication instance;
    /** 保存打开的activity */
    List<Activity> activityList = new ArrayList<>();
    /** 当前activity */
    private WeakReference<Activity> topActivity;

    @Override
    public void onCreate() {
        instance = this;
        registerActivityLifecycleCallbacks(new BaseActivityLifecycleCallbacks());
        super.onCreate();
    }

    /**
     * 获取Application实例
     * @return
     */
    public static BaseApplication getInstance() {
        return instance;
    }

    /**
     * 退出app
     */
    public void exit() {
        try {
            for (Activity activity : activityList) {
                if (activity != null) {
                    activity.finish();
                }
            }
        } finally {
            System.exit(0);
        }
    }

    /**
     * 获取打开的activity列表
     * @return
     */
    public List<Activity> getActivityList() {
        return this.activityList;
    }

    /**
     * 获取当前activity
     * @return
     */
    public Activity getTopActivity() {
        if (topActivity == null) {
            return null;
        }
        return topActivity.get();
    }

    private class BaseActivityLifecycleCallbacks implements ActivityLifecycleCallbacks {
        /** 切换到后台的时间点 */
        private long backgrounderTimeAt = 0L;
        /** 当前活动页面时间 */
        private int activityCount = 0;
        /** 前后台切换间隔时间，需要解锁 */
        private long shiftSpaceMillis = 5000L;

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            BaseApplication.getInstance().getActivityList().add(activity);
        }

        @Override
        public void onActivityStarted(Activity activity) {
            topActivity = new WeakReference<>(activity);

            if (this.activityCount == 0) {
                // backgrounderTimeAt等于0，说明是启动app。不等于0是后台切换到前台
                if (this.backgrounderTimeAt != 0) {
                    // 后台运行超过shiftSpaceMillis，就需要重新解锁
                    if (System.currentTimeMillis() - backgrounderTimeAt > shiftSpaceMillis) {
                        Intent intent = new Intent(activity, GestureVerifyActivity.class);
                        startActivity(intent);
                    }
                }
            }
            this.activityCount++;
        }

        @Override
        public void onActivityResumed(Activity activity) {
        }

        @Override
        public void onActivityPaused(Activity activity) {
        }

        @Override
        public void onActivityStopped(Activity activity) {
            this.activityCount--;
            if (this.activityCount == 0) {
                this.backgrounderTimeAt = System.currentTimeMillis();
            }
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            BaseApplication.getInstance().getActivityList().remove(activity);
        }
    }
}
