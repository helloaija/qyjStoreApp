package com.qyjstore.qyjstoreapp.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.qyjstore.qyjstoreapp.R;
import com.qyjstore.qyjstoreapp.utils.HttpUtil;
import com.qyjstore.qyjstoreapp.utils.ToastUtil;
import org.json.JSONObject;

/**
 * @Author shitl
 * @Description 登录界面
 * @date 2019-05-22
 */
public class LoginActivity extends AppCompatActivity {
    private Button btnLogin;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_login);

        btnLogin = findViewById(R.id.activity_login_bt_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpUtil.doPostAsyn("https://www.qyjstore.cn/admin/login?username=shitl", null, new HttpUtil.CallBack() {
//                HttpUtil.doPostAsyn("https://www.baidu.com", null, new HttpUtil.CallBack() {
                    @Override
                    public void onSuccess(JSONObject json) {
//                        ToastUtil.makeText(LoginActivity.this, json.toString());
                        Log.d("LoginActivity", "login success" + json);
                    }

                    @Override
                    public void onError(int responseCode, String msg) {
//                        ToastUtil.makeText(LoginActivity.this, responseCode + msg);
                        Log.d("LoginActivity", "login error, responseCode=" + responseCode + ", msg=" + msg);
                    }
                });
            }
        });
    }

}
