package com.qyjstore.qyjstoreapp.utils;

import java.math.BigDecimal;

/**
 * @Author shitl
 * @Description
 * @date 2019-05-20
 */
public class NumberUtil {

    /**
     * 按位数获取小数点（截取），不够位数补0
     * @param bigDecimal
     * @param scale 小数位数
     * @return
     */
    public static String getScale(BigDecimal bigDecimal, int scale) {
        return getScale(bigDecimal, scale, BigDecimal.ROUND_DOWN);
    }

    /**
     * 按位数获取小数点，不够位数补0
     * @param bigDecimal
     * @param scale 小数位数
     * @param round 获取方式，截取、四舍五入等按BigDecimal约定的方式
     * @return
     */
    public static String getScale(BigDecimal bigDecimal, int scale, int round) {
        if (bigDecimal == null || scale <= 0) {
            return "";
        }

        return bigDecimal.setScale(scale, round).toString().split("\\.")[1];

    }

    /**
     * 按位数获取小数点（截取），不够位数补0
     * @param d
     * @param scale 小数位数
     * @return
     */
    public static String getDoubleString(Double d, int scale) {
        if (d == null) {
            return "";
        }
        return getDoubleString(BigDecimal.valueOf(d), scale, BigDecimal.ROUND_DOWN);
    }

    /**
     * 按位数获取小数点（截取），不够位数补0
     * @param d
     * @param scale 小数位数
     * @return
     */
    public static String getDoubleString(BigDecimal d, int scale) {
        return getDoubleString(d, scale, BigDecimal.ROUND_DOWN);
    }

    /**
     * 按位数获取小数点，不够位数补0
     * @param d
     * @param scale 小数位数
     * @param round 获取方式，截取、四舍五入等按BigDecimal约定的方式
     * @return
     */
    public static String getDoubleString(BigDecimal d, int scale, int round) {
        if (d == null) {
            return "";
        }
        if (scale < 0) {
            return d.toString();
        }

        return d.setScale(scale, BigDecimal.ROUND_DOWN).toString();
    }

    public static void main(String[] args) {
        BigDecimal num = new BigDecimal("16.1111113");
        num.setScale(2, BigDecimal.ROUND_DOWN);
        System.out.println(NumberUtil.getScale(num, 20, BigDecimal.ROUND_DOWN));
        System.out.println(num.toString());
    }


}

