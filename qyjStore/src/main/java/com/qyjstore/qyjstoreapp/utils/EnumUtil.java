package com.qyjstore.qyjstoreapp.utils;

/**
 * @Author shitl
 * @Description
 * @date 2019-05-31
 */
public class EnumUtil {
    /**
     * 订单状态枚举
     */
    public enum OrderStatusEnum {
        UNPAY("UNPAY", "未支付"), UNPAYALL("UNPAYALL", "未支付完"), HASPAYALL("HASPAYALL", "支付完");;

        private String value;
        private String text;

        private OrderStatusEnum(String value, String text) {
            this.value = value;
            this.text = text;
        }

        public static String getTextByValue(String value) {
            for (OrderStatusEnum ose : OrderStatusEnum.values()) {
                if (ose.getValue().equals(value)) {
                    return ose.getText();
                }
            }
            return "";
        }

        public String getValue() {
            return value;
        }

        public String getText() {
            return text;
        }
    }

}
