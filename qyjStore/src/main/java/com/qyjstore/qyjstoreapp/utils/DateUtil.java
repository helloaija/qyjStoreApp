package com.qyjstore.qyjstoreapp.utils;

import android.text.TextUtils;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author shitl
 * @Description
 * @date 2019-05-20
 */
public class DateUtil {

    public static String DATE_FORMAT_DATETIME = "yyyy-MM-dd HH:mm:ss";
    public static String DATE_FORMAT_DATE = "yyyy-MM-dd";
    public static String DATE_FORMAT_DATE_CH = "yyyy年MM月dd日";

    /**
     * 时间转字符串，默认格式yyyy-MM-dd HH:mm:ss
     * @param date
     * @return
     */
    public static String parseDateToString(Date date) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_DATETIME);
        return sdf.format(date);
    }

    /**
     * 时间换字符串
     * @param date 时间
     * @param dateFormat 格式
     * @return
     */
    public static String parseDateToString(Date date, String dateFormat) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        return sdf.format(date);
    }

    /**
     * 时间字符串转时间k，默认格式yyyy-MM-dd HH:mm:ss
     * @param dateStr 字符串
     * @return
     */
    public static Date parseStringToDate(String dateStr) {
        if (TextUtils.isEmpty(dateStr)) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_DATETIME);
        try {
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            Log.d("DateUtil", "parseStringToDate occur Exception:"  + e.getMessage());
            return null;
        }
    }

    /**
     * 时间字符串转时间
     * @param dateStr 字符串
     * @param dateFormat 格式
     * @return
     */
    public static Date parseStringToDate(String dateStr, String dateFormat) {
        if (TextUtils.isEmpty(dateStr)) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        try {
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            Log.d("DateUtil", "parseStringToDate occur Exception:"  + e.getMessage());
            return null;
        }
    }
}
