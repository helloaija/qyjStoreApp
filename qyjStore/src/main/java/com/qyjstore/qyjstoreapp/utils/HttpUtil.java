package com.qyjstore.qyjstoreapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qyjstore.qyjstoreapp.base.BaseApplication;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class HttpUtil {
    public static JSONObject doGet(String requestUrl, Map<String, String> paramMap) throws Exception {
        return doGet(requestUrl, paramMap, null);
    }

    public static JSONObject doGet(String requestUrl, Map<String, String> paramMap, CallBack callBack) throws Exception {
        URL url = new URL(requestUrl + concatParam(paramMap));
        HttpURLConnection conn = null;
        try {
            SharedPreferences sp = BaseApplication.getInstance().getApplicationContext()
                    .getSharedPreferences(ConstantUtil.PRO_NAME_USER_INFO, Context.MODE_PRIVATE);
            String authentication = sp.getString("authentication", "");
            conn = (HttpURLConnection) url.openConnection();

            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("Authorization", "Bearer " + authentication);

            if (conn.getResponseCode() != 200) {
                if (callBack != null) {
                    callBack.onError(conn.getResponseCode(), null);
                }
                return null;
            }

            String result = convertStreamToString(conn.getInputStream());

            JSONObject json = JSON.parseObject(result);
            if (callBack != null) {
                callBack.onSuccess(json);
            }
            return json;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    public static JSONObject doPost(String requestUrl, Map<String, String> paramMap) throws Exception {
        return doPost(requestUrl, paramMap);
    }

    public static JSONObject doPost(String requestUrl, Map<String, String> paramMap, CallBack callBack) throws Exception {

        URL url = new URL(requestUrl + concatParam(paramMap));
        HttpURLConnection conn = null;
        try {
            SharedPreferences sp = BaseApplication.getInstance().getApplicationContext()
                    .getSharedPreferences(ConstantUtil.PRO_NAME_USER_INFO, Context.MODE_PRIVATE);
            String authentication = sp.getString("authentication", "");
            conn = (HttpURLConnection) url.openConnection();

            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("charset", "utf-8");
            conn.setRequestProperty("Authorization", "Bearer " + authentication);
            conn.setUseCaches(false);
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);

            if (conn.getResponseCode() != 200) {
                if (callBack != null) {
                    callBack.onError(conn.getResponseCode(), null);
                }
                return null;
            }

            String result = convertStreamToString(conn.getInputStream());

            JSONObject json = JSON.parseObject(result);
            if (callBack != null) {
                callBack.onSuccess(json);
            }
            return json;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    public static void doGetAsyn(final String requestUrl, final Map<String, String> paramMap, final CallBack callBack) {
        new Thread() {
            @Override
            public void run() {
                try {
                    doGet(requestUrl, paramMap, callBack);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("HttpUtil", "doGetAsyn error:" + e.getStackTrace());
                }

            }
        }.start();
    }

    public static void doPostAsyn(final String requestUrl, final Map<String, String> paramMap, final CallBack callBack) {
        new Thread() {
            public void run() {
                try {
                    doPost(requestUrl, paramMap, callBack);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("HttpUtil", "doPostAsyn error:" + e.getStackTrace());
                }

            }
        }.start();
    }

    public interface CallBack {
        void onSuccess(JSONObject json);

        void onError(int responseCode, String msg);
    }

    private static String convertStreamToString(InputStream conIs) throws Exception {
        try (InputStream is = conIs;
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            int len = -1;
            byte[] b = new byte[128];

            while ((len = is.read(b)) != -1) {
                baos.write(b, 0, len);
            }
            baos.flush();
            return baos.toString();
        }
    }

    /**
     * 拼接参数
     * @param map
     * @return
     */
    private static String concatParam(Map<String, String> map) {
        StringBuilder paramSb = new StringBuilder();
        if (map != null && !map.isEmpty()) {
            paramSb.append("?");
            for (Map.Entry<String, String> entry : map.entrySet()) {
                if (!paramSb.toString().endsWith("?")) {
                    paramSb.append("&");
                }
                paramSb.append(entry.getKey()).append("=").append(entry.getValue());
            }
        }

        return paramSb.toString();
    }
}
