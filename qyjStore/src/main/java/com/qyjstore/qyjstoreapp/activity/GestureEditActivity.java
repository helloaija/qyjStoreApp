package com.qyjstore.qyjstoreapp.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.qyjstore.qyjstoreapp.R;
import com.qyjstore.qyjstoreapp.view.GestureIndicator;

/**
 * @Author shitl
 * @Description 设置手势
 * @date 2019-05-22
 */
public class GestureEditActivity extends AppCompatActivity {

    /** 手势标志 */
    private GestureIndicator indicator;
    /** 密码 */
    private String password;
    /** 确认密码*/
    private String comfirePassword;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_gesture_edit);


    }
}
