package com.qyjstore.qyjstoreapp.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

    private Context mContext;

    /** 手势标志 */
    private GestureIndicatorView indicator;
    private GestureContentView gestureContent;
    /** 密码 */
    private String password;
    /** 确认密码*/
    private String comfirePassword;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_gesture_edit);

        indicator = findViewById(R.id.activity_gesture_edit_indicator);

        gestureContent = findViewById(R.id.activity_gesture_edit_content);
        gestureContent.setGestureContentListener(new GestureContentView.GestureContentListener() {
            @Override
            public void onDrawFinished(String resultPassword) {
                if (EDIT_TYPE_SETTING.equals(editType)) {
                    password = resultPassword;
                    indicator.setPath(resultPassword);
                } else {
                    comfirePassword = resultPassword;
                }

                gestureContent.setStatus(GestureContentView.STATUS_WRONG);
                gestureContent.reset(1000);
            }
        });
    }

}
