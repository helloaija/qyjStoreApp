package com.qyjstore.qyjstoreapp.base;

import android.support.v7.app.AppCompatActivity;
import org.json.JSONException;
import org.json.JSONObject;

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
        try {
            if (json == null) {
                return null;
            }
            return json.getString("resultCode");
        } catch (JSONException e) {
            return "";
        }
    }

    /**
     * resultMessage
     * @param json
     * @return
     */
    public String getResultMessage(JSONObject json) {
        try {
            if (json == null) {
                return null;
            }
            return json.getString("resultMessage");
        } catch (JSONException e) {
            return "";
        }
    }
}
