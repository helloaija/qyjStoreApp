package com.qyjstore.qyjstoreapp.base;

import android.support.v7.app.AppCompatActivity;
import com.alibaba.fastjson.JSONObject;
import org.json.JSONException;

/**
 * @Author shitl
 * @Description
 * @date 2019-05-27
 */
public class BaseActivity extends AppCompatActivity {
    /**
     * 获取系统后台返回的resultCode
     * @param json
     * @return
     */
    public String getResultCode(JSONObject json) {
        if (json == null) {
            return null;
        }
        return json.getString("resultCode");
    }

    /**
     * 获取系统后台返回的resultMessage
     * @param json
     * @return
     */
    public String getResultMessage(JSONObject json) {
        if (json == null) {
            return null;
        }
        return json.getString("resultMessage");
    }
}
