package com.qyjstore.qyjstoreapp.utils;

/**
 * @Author shitl
 * @Description 配置属性
 * @date 2019-05-27
 */
public class ConfigUtil {
    // public static String SYS_SERVICE_BASE_URL = "https://www.qyjstore.cn";
//    public static String SYS_SERVICE_BASE_URL = "http://192.168.30.19:8080";
    public static String SYS_SERVICE_BASE_URL = "http://192.168.1.105:8080";


    /** 登录接口 */
    public static String SYS_SERVICE_LOGIN = SYS_SERVICE_BASE_URL + "/admin/login";

    /** 获取用户信息接口 */
    public static String SYS_SERVICE_GET_USER_INFO = SYS_SERVICE_BASE_URL + "/admin/app/user/getUserInfo";
}
