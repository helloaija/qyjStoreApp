package com.qyjstore.qyjstoreapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import com.qyjstore.qyjstoreapp.R;

import java.lang.ref.WeakReference;

/**
 * @Author shitl
 * @Description 启动页
 * @date 2019-05-21
 */
public class InitialActivity extends AppCompatActivity {

    /** 首页标志 */
    private static final int GO_HOME = 1000;

    /** 跳转延迟时间 */
    private static final long GO_DELAY_MILLIS = 1500;

    private Handler mHandler = new GoPageHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial);

        mHandler.sendEmptyMessageDelayed(GO_HOME, GO_DELAY_MILLIS);
    }

    /**
     * 跳转页面
     * @param msg
     */
    private void goPage(Message msg) {
        switch (msg.what) {
            case GO_HOME:
                goHome();
                break;
            default:
                break;
        }
    }

    /**
     * 跳转到主页面
     */
    private void goHome() {
        Intent intent = new Intent(InitialActivity.this, GestureEditActivity.class);
        startActivity(intent);
        finish();
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
            weakReference.get().goPage(msg);
            super.handleMessage(msg);
        }
    }
}
