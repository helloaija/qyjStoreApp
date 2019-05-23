package com.qyjstore.qyjstoreapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.TextView;
import com.qyjstore.qyjstoreapp.R;
import com.qyjstore.qyjstoreapp.view.GestureContentView;
import com.qyjstore.qyjstoreapp.view.GestureIndicatorView;

/**
 * @Author shitl
 * @Description 设置手势
 * @date 2019-05-22
 */
public class GestureEditActivity extends AppCompatActivity {
    /** 绘制类型 */
    private String editType = EDIT_TYPE_SETTING;
    /** 绘制类型-设置 */
    public static final String EDIT_TYPE_SETTING = "setting";
    /** 绘制类型-确认 */
    public static final String EDIT_TYPE_COMFIRM = "comfirm";

    /** 手势标志 */
    private GestureIndicatorView indicator;
    private GestureContentView gestureContent;
    /** 标题 */
    private TextView titleTextView;
    /** 提示信息 */
    private TextView tipTextView;
    /** 密码 */
    private String password;
    /** 确认密码*/
    private String comfirePassword;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_gesture_edit);
        titleTextView = findViewById(R.id.activity_gesture_edit_tv_title);
        indicator = findViewById(R.id.activity_gesture_edit_indicator);
        tipTextView = findViewById(R.id.activity_gesture_edit_tv_tip);

        gestureContent = findViewById(R.id.activity_gesture_edit_content);
        gestureContent.setGestureContentListener(new GestureContentView.GestureContentListener() {
            @Override
            public void onDrawFinished(String resultPassword) {
                if (EDIT_TYPE_SETTING.equals(editType)) {
                    password = resultPassword;
                    indicator.setPath(resultPassword);
                    if (!validPassword(resultPassword)) {
                        gestureContent.setStatus(GestureContentView.STATUS_WRONG);
                        gestureContent.reset(1000);
                        return;
                    }
                    initComfirmView();
                } else {
                    comfirePassword = resultPassword;
                    if (validPassword(resultPassword) && comfirePassword.equals(password)) {
                        // 跳转到首页
                        Intent intent = new Intent(GestureEditActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }

            }
        });
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
            tipTextView.setText(R.string.gesture_label_len);
            tipTextView.setTextColor(getResources().getColor(R.color.colorRed, null));
            return false;
        }

        tipTextView.setTextColor(getResources().getColor(R.color.mtrl_scrim_color, null));
        return true;
    }

    /**
     * 初始化确认手势
     */
    private void initComfirmView() {
        gestureContent.reset();
        titleTextView.setText(R.string.gesture_label_comfirm);
        tipTextView.setText(R.string.gesture_label_comfirm);
        editType = EDIT_TYPE_COMFIRM;
    }
}
