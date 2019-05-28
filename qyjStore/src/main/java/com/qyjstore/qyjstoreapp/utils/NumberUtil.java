package com.qyjstore.qyjstoreapp.utils;

import com.alibaba.fastjson.JSON;

import java.math.BigDecimal;

/**
 * @Author shitl
 * @Description
 * @date 2019-05-20
 */
public class NumberUtil {
    public static String getScale(BigDecimal bigDecimal, int scale) {
        BigDecimal scaleDecimal = bigDecimal.subtract(BigDecimal.valueOf(bigDecimal.longValue()));
        return String.valueOf(scaleDecimal.multiply(new BigDecimal(10).pow(scale)).longValue());
    }

    public static void main(String[] args) {
    }


}

