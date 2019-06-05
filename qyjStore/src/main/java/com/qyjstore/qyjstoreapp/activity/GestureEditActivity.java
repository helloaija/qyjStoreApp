package com.qyjstore.qyjstoreapp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import com.qyjstore.qyjstoreapp.R;
import com.qyjstore.qyjstoreapp.utils.ConstantUtil;
import com.qyjstore.qyjstoreapp.view.GestureContentView;
import com.qyjstore.qyjstoreapp.view.GestureIndicatorView;

/**
 * @Author shitl
 * @Description 设置手势、确认手势
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
    /** 重置标签 */
    private TextView resetTextView;
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
        resetTextView = findViewById(R.id.activity_gesture_edit_tv_reset);

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

                    if (!validPassword(resultPassword)) {
                        gestureContent.setStatus(GestureContentView.STATUS_WRONG);
                        gestureContent.reset(1000);
                        return;
                    }
                    if (!comfirePassword.equals(password)) {
                        tipTextView.setText(R.string.gesture_label_comfirm_wrong);
                        tipTextView.setTextColor(getResources().getColor(R.color.app_color_theme_1, null));
                        gestureContent.setStatus(GestureContentView.STATUS_WRONG);
                        gestureContent.reset(1000);
                        return;
                    }
                    tipTextView.setText(R.string.gesture_label_suc);

                    SharedPreferences sp = getSharedPreferences(ConstantUtil.PRO_NAME_USER_INFO, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("gesturePassword", password);
                    editor.apply();

                    // 跳转到首页
                    Intent intent = new Intent(GestureEditActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        });

        resetTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initSettingView();
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
            tipTextView.setTextColor(getResources().getColor(R.color.app_color_theme_1, null));
            return false;
        }

        this.tipTextView.setTextColor(getResources().getColor(R.color.mtrl_scrim_color, null));
        return true;
    }

    /**
     * 初始化确认手势
     */
    private void initComfirmView() {
        this.gestureContent.reset();
        this.titleTextView.setText(R.string.gesture_label_comfirm);
        this.tipTextView.setText(R.string.gesture_label_comfirm);
        this.resetTextView.setVisibility(View.VISIBLE);
        this.editType = EDIT_TYPE_COMFIRM;
    }

    /**
     * 初始化设置手势
     */
    private void initSettingView() {
        this.gestureContent.reset();
        this.titleTextView.setText(R.string.gesture_label_setting);
        this.tipTextView.setText(R.string.gesture_label_setting);
        this.resetTextView.setVisibility(View.INVISIBLE);
        this.editType = EDIT_TYPE_SETTING;
        this.password = "";
        this.comfirePassword = "";
        this.indicator.setPath(password);
    }
}
