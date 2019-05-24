package com.qyjstore.qyjstoreapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.qyjstore.qyjstoreapp.R;
import com.qyjstore.qyjstoreapp.base.BaseApplication;
import com.qyjstore.qyjstoreapp.view.GestureContentView;
import com.qyjstore.qyjstoreapp.view.GestureIndicatorView;

/**
 * @Author shitl
 * @Description 验证手势
 * @date 2019-05-22
 */
public class GestureVerifyActivity extends AppCompatActivity {

    /** 手势绘制 */
    private GestureContentView gestureContent;
    /** 提示信息 */
    private TextView tipTextView;
    /** 忘记密码标签 */
    private TextView forgetTextView;
    /** 使用账号密码登录标签 */
    private TextView useAccountTextView;
    /** 密码 */
    private String password;
    /** 剩余次数 */
    private int times = 5;
    /** 点击返回键的时间 */
    private long keyBackDownAt = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.password = "12357";

        this.setContentView(R.layout.activity_gesture_verify);
        this.tipTextView = findViewById(R.id.activity_gesture_verify_tv_tip);
        this.forgetTextView = findViewById(R.id.activity_gesture_verify_tv_forget);
        this.useAccountTextView = findViewById(R.id.activity_gesture_verify_tv_use_account);

        this.gestureContent = findViewById(R.id.activity_gesture_verify_content);
        this.gestureContent.setGestureContentListener(new GestureContentView.GestureContentListener() {
            @Override
            public void onDrawFinished(String resultPassword) {
                if (!validPassword(resultPassword)) {
                    gestureContent.setStatus(GestureContentView.STATUS_WRONG);
                    gestureContent.reset(1000);
                    return;
                }

                finish();
            }
        });

        // 忘记手势密码
        forgetTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        // 使用账号密码登录
        useAccountTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0) {
                // 点击两次回退，时间间隔超过两秒就提示，两秒以内就退出
                if (keyBackDownAt == 0 || System.currentTimeMillis() - keyBackDownAt > 2000L) {
                    Toast.makeText(GestureVerifyActivity.this, "提示的内容", Toast.LENGTH_SHORT).show();
                    keyBackDownAt = System.currentTimeMillis();
                } else {
                    BaseApplication.getInstance().exit();
                }
            }

            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    /**
     * 校验密码
     * @param password
     * @return
     */
    private boolean validPassword(String password) {
        if (TextUtils.isEmpty(password)) {
            return false;
        }

        if (password.length() <= 3) {
            this.tipTextView.setText(R.string.gesture_label_len);
            return false;
        }

        if (!password.equals(this.password)) {
            this.times = this.times - 1;
            this.tipTextView.setText(String.format(getResources().getString(R.string.gesture_label_entry_times), this.times));
            if (this.times == 0) {
                // 剩余0次，不让画
                this.gestureContent.setDrawEnable(false);
            }
            return false;
        }

        return true;
    }

}
