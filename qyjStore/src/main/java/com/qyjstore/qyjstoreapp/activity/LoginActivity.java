package com.qyjstore.qyjstoreapp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.alibaba.fastjson.JSONObject;
import com.qyjstore.qyjstoreapp.R;
import com.qyjstore.qyjstoreapp.base.BaseActivity;
import com.qyjstore.qyjstoreapp.base.UserManager;
import com.qyjstore.qyjstoreapp.utils.AppUtil;
import com.qyjstore.qyjstoreapp.utils.ConfigUtil;
import com.qyjstore.qyjstoreapp.utils.ConstantUtil;
import com.qyjstore.qyjstoreapp.utils.HttpUtil;
import com.qyjstore.qyjstoreapp.utils.ToastUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author shitl
 * @Description 登录界面
 * @date 2019-05-22
 */
public class LoginActivity extends BaseActivity {
    private EditText userNameEt;
    private EditText passowrdEt;
    private EditText verifyCodeEt;
    private Button btnLogin;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_login);

        this.userNameEt = findViewById(R.id.activity_login_et_username);
        this.passowrdEt = findViewById(R.id.activity_login_et_password);
        this.verifyCodeEt = findViewById(R.id.activity_login_et_verify_code);
        this.btnLogin = findViewById(R.id.activity_login_bt_login);

        this.setLoginBtnOnClickListener();
    }

    private void setLoginBtnOnClickListener() {
        this.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = userNameEt.getText().toString().trim();
                String password = passowrdEt.getText().toString().trim();
                String verifyCode = verifyCodeEt.getText().toString().trim();

                if (TextUtils.isEmpty(username)) {
                    ToastUtil.makeText(LoginActivity.this, "请输入用户名");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    ToastUtil.makeText(LoginActivity.this, "请输入密码");
                    return;
                }
                if (TextUtils.isEmpty(verifyCode)) {
                    ToastUtil.makeText(LoginActivity.this, "请输入验证码");
                    return;
                }

                Map<String, String> paramMap = new HashMap<>();
                paramMap.put("username", username);
                paramMap.put("password", password);
                paramMap.put("validCode", verifyCode);
                HttpUtil.doPostAsyn(ConfigUtil.SYS_SERVICE_LOGIN, paramMap, new HttpUtil.CallBack() {
                    @Override
                    public void onSuccess(JSONObject json) {
                        Looper.prepare();
                        if ("0000".equals(getResultCode(json))) {
                            Log.d("LoginActivity", "login success" + json);
                            // 登录成功
                            SharedPreferences sp = getSharedPreferences(ConstantUtil.PRO_NAME_USER_INFO, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString("authentication", json.getString("result"));
                            editor.apply();

                            // 加载用户数据
                            loadUserInfo();
                        } else {
                            ToastUtil.makeText(LoginActivity.this, getResultMessage(json));
                        }
                        Looper.loop();
                    }

                    @Override
                    public void onError(int responseCode, String msg) {
                        Looper.prepare();
                        ToastUtil.makeText(LoginActivity.this, "系统异常");
                        Log.d("LoginActivity", "login error, responseCode=" + responseCode + ", msg=" + msg);
                        Looper.loop();
                    }
                });
            }
        });
    }

    /**
     * 加载用户数据
     */
    private void loadUserInfo() {
        // 根据authentication加载用户数据
        UserManager.getInstance().loadUserInfo(new HttpUtil.CallBack() {
            @Override
            public void onSuccess(JSONObject json) {
                Looper.prepare();
                if (!"0000".equals(getResultCode(json))) {
                    ToastUtil.makeText(LoginActivity.this, getResultMessage(json));
                } else {
                    // 判断跳转到哪个页面
                    toPage();
                }
                Looper.loop();
            }

            @Override
            public void onError(int responseCode, String msg) {
                Looper.prepare();
                if (AppUtil.handleLoginExpire(LoginActivity.this, responseCode)) {
                    Looper.loop();
                    return;
                }

                ToastUtil.makeText(LoginActivity.this, "系统异常");
                Looper.loop();
            }
        });
    }

    /**
     * 跳转页面
     */
    private void toPage() {
        SharedPreferences sp = getSharedPreferences(ConstantUtil.PRO_NAME_USER_INFO, Context.MODE_PRIVATE);
        long oldUserId = sp.getLong("userId", 0);
        String gesturePassword = sp.getString("gesturePassword", null);

        long userId = UserManager.getInstance().getUser().getUserId();

        SharedPreferences.Editor editor = sp.edit();
        if (oldUserId == 0 || oldUserId != userId) {
            editor.putLong("userId", userId);
            editor.putString("gesturePassword", "");
            // 设置手势密码
            Intent intent = new Intent(LoginActivity.this, GestureEditActivity.class);
            startActivity(intent);
        } else if (TextUtils.isEmpty(gesturePassword)) {
            // 设置手势密码
            Intent intent = new Intent(LoginActivity.this, GestureEditActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            // 主页面
            startActivity(intent);
            // 校验手势密码
            intent = new Intent(LoginActivity.this, GestureVerifyActivity.class);
            startActivity(intent);
        }
        editor.putBoolean("isLoginState", true);
        editor.apply();
        finish();
    }

}
