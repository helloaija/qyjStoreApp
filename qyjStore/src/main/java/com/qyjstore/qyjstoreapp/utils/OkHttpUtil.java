package com.qyjstore.qyjstoreapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Looper;
import com.qyjstore.qyjstoreapp.base.BaseApplication;
import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.util.Map;

/**
 * @Author shitl
 * @Description okHttpClient工具类，单例模式
 * @date 2019-06-10
 */
public class OkHttpUtil {
    private static OkHttpClient okHttpClient;

    /**
     * 构造函数私有化
     */
    private OkHttpUtil() {
    }

    public static OkHttpClient getInstance() {
        if (okHttpClient == null) {
            synchronized (OkHttpUtil.class) {
                if (okHttpClient == null) {
                    okHttpClient = new OkHttpClient.Builder().addInterceptor(new tokenInterceptor()).build();
                }
            }
        }
        return okHttpClient;
    }

    /**
     * 异步get请求
     * @param requestUrl
     * @param callback
     */
    public static void doGet(String requestUrl, Callback callback) {
        doGet(requestUrl, null, callback);
    }

    /**
     * 异步get请求
     * @param requestUrl
     * @param paramMap
     * @param callback
     */
    public static void doGet(String requestUrl, Map<String, String> paramMap, Callback callback) {
        OkHttpClient okHttpClient = getInstance();
        final Request request = new Request.Builder().url(concatParam(requestUrl, paramMap)).get().build();

        okHttpClient.newCall(request).enqueue(callback);
    }

    /**
     * 异步post请求
     * @param requestUrl
     * @param paramMap
     * @param callback
     */
    public static void doPost(String requestUrl, Map<String, String> paramMap, Callback callback) {
        RequestBody requestBody = null;

        if (paramMap != null || !paramMap.isEmpty()) {
            FormBody.Builder builder = new FormBody.Builder();
            for (Map.Entry<String, String> entry : paramMap.entrySet()) {
                builder.add(entry.getKey(), entry.getValue());
            }

            requestBody = builder.build();
        }


        OkHttpClient okHttpClient = getInstance();
        final Request request = new Request.Builder().url(concatParam(requestUrl, paramMap))
                .post(requestBody).build();

        okHttpClient.newCall(request).enqueue(callback);
    }

    /**
     * 拼接参数
     * @param map
     * @return
     */
    private static String concatParam(String requestUrl, Map<String, String> map) {
        if (map == null || map.isEmpty()) {
            return requestUrl;
        }

        StringBuilder paramSb = new StringBuilder();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (paramSb.length() != 0) {
                paramSb.append("&");
            }
            paramSb.append(entry.getKey()).append("=").append(entry.getValue());
        }

        return requestUrl + "?" + paramSb.toString();
    }

    static class tokenInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            SharedPreferences sp = BaseApplication.getInstance().getApplicationContext()
                    .getSharedPreferences(ConstantUtil.PRO_NAME_USER_INFO, Context.MODE_PRIVATE);
            String authentication = sp.getString("authentication", "");

            Request request = chain.request().newBuilder().addHeader("Authorization", "Bearer " + authentication)
                    .build();
            return chain.proceed(request);
        }
    }

    /**
     * 异步get请求
     * @param requestUrl
     * @param callback
     */
    public static void doGet(String requestUrl, final HttpCallBack callback) {
        doGet(requestUrl, null, callback);
    }

    /**
     * 异步get请求
     * @param requestUrl
     * @param paramMap
     * @param callback
     */
    public static void doGet(String requestUrl, Map<String, String> paramMap, final HttpCallBack callback) {
        OkHttpClient okHttpClient = getInstance();
        final Request request = new Request.Builder().url(concatParam(requestUrl, paramMap)).get().build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Looper.prepare();
                AppUtil.handleHttpException(callback.context, e);
                Looper.loop();

                callback.onFailure(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (AppUtil.handleHttpResponseCode(callback.context, response.code())) {
                    return;
                }

                callback.onResponse(call, response, response.body().string());
            }
        });
    }

    /**
     * 异步post请求
     * @param requestUrl
     * @param paramMap
     * @param callback
     */
    public static void doPost(String requestUrl, Map<String, String> paramMap, final HttpCallBack callback) {
        RequestBody requestBody = null;

        if (paramMap != null || !paramMap.isEmpty()) {
            FormBody.Builder builder = new FormBody.Builder();
            for (Map.Entry<String, String> entry : paramMap.entrySet()) {
                builder.add(entry.getKey(), entry.getValue());
            }

            requestBody = builder.build();
        }


        OkHttpClient okHttpClient = getInstance();
        final Request request = new Request.Builder().url(concatParam(requestUrl, paramMap))
                .post(requestBody).build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Looper.prepare();
                AppUtil.handleHttpException(callback.context, e);
                Looper.loop();

                callback.onFailure(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (AppUtil.handleHttpResponseCode(callback.context, response.code())) {
                    return;
                }

                callback.onResponse(call, response, response.body().string());
            }
        });
    }

    /**
     * 业务回调，需要传入Content对象做统一业务处理，比如登录校验，请求超时处理等
     */
    public abstract static class HttpCallBack {
        Context context;

        public HttpCallBack(Context context) {
            this.context = context;
        }

        public abstract void onFailure(Call call, IOException e);

        public abstract void onResponse(Call call, Response response, String responeText);

        public Context getContext() {
            return context;
        }
    }
}


