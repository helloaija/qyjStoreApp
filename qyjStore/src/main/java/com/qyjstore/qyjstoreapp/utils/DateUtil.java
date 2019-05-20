package com.qyjstore.qyjstoreapp.utils;

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

    public static String parseDateToString(Date date) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_DATETIME);
        return sdf.format(date);
    }

    public static String parseDateToString(Date date, String dateFormat) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        return sdf.format(date);
    }
}
