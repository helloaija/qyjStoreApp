package com.qyjstore.qyjstoreapp.utils;

/**
 * @Author shitl
 * @Description 配置属性
 * @date 2019-05-27
 */
public class ConfigUtil {
    // public static String SYS_SERVICE_BASE_URL = "https://www.qyjstore.cn";
    public static String SYS_SERVICE_BASE_URL = "http://192.168.30.22:8080";
    // public static String SYS_SERVICE_BASE_URL = "http://192.168.1.105:8080";

    /** 接口-登录 */
    public static String SYS_SERVICE_LOGIN = SYS_SERVICE_BASE_URL + "/admin/login";

    /** 接口-获取用户信息 */
    public static String SYS_SERVICE_GET_USER_INFO = SYS_SERVICE_BASE_URL + "/admin/app/user/getUserInfo";

    /** 接口-获取销售单列表 */
    public static String SYS_SERVICE_LIST_SELL_ORDER = SYS_SERVICE_BASE_URL + "/admin/app/sellOrder/listSellOrderPage";

    /** 接口-获取单个销售单 */
    public static String SYS_SERVICE_GET_SELL_ORDER = SYS_SERVICE_BASE_URL + "/admin/app/sellOrder/getSellOrderInfo";

    /** 接口-获取购买用户列表 */
    public static String SYS_SERVICE_LIST_USER = SYS_SERVICE_BASE_URL + "/admin/app/buyer/listUserPage";

    /** 接口-新增购买用户 */
    public static String SYS_SERVICE_INSERT_USER = SYS_SERVICE_BASE_URL + "/admin/app/buyer/insertUserInfo";

    /** 接口-获取进货产品列表 */
    public static String SYS_SERVICE_LIST_STOCK_PRODUCT = SYS_SERVICE_BASE_URL + "/admin/app/stockOrder/listProductStockInfo";

    /** 接口-获取产品列表 */
    public static String SYS_SERVICE_LIST_PRODUCT = SYS_SERVICE_BASE_URL + "/admin/app/product/listProductPage";

    /** 接口-保存产品信息 */
    public static String SYS_SERVICE_SAVE_PRODUCT = SYS_SERVICE_BASE_URL + "/admin/app/product/saveProductInfo";

    /** 接口-添加销售单 */
    public static String SYS_SERVICE_ADD_SELL_ORDER = SYS_SERVICE_BASE_URL + "/admin/app/sellOrder/addSellOrder";

    /** 接口-更新销售单 */
    public static String SYS_SERVICE_UPDATE_SELL_ORDER = SYS_SERVICE_BASE_URL + "/admin/app/sellOrder/updateSellOrder";

    /** 接口-删除销售单 */
    public static String SYS_SERVICE_DELETE_SELL_ORDER = SYS_SERVICE_BASE_URL + "/admin/app/sellOrder/deleteSellOrder";

    /** 接口-获取进货订单列表 */
    public static String SYS_SERVICE_LIST_STOCK_ORDER = SYS_SERVICE_BASE_URL + "/admin/app/stockOrder/listStockOrderPage";

    /** 接口-获取单个进货单 */
    public static String SYS_SERVICE_GET_STOCK_ORDER = SYS_SERVICE_BASE_URL + "/admin/app/stockOrder/getStockOrderInfo";

    /** 接口-添加进货单 */
    public static String SYS_SERVICE_ADD_STOCK_ORDER = SYS_SERVICE_BASE_URL + "/admin/app/stockOrder/addStockOrder";

    /** 接口-更新进货单 */
    public static String SYS_SERVICE_UPDATE_STOCK_ORDER = SYS_SERVICE_BASE_URL + "/admin/app/stockOrder/updateStockOrder";

    /** 接口-删除进货单 */
    public static String SYS_SERVICE_DELETE_STOCK_ORDER = SYS_SERVICE_BASE_URL + "/admin/app/stockOrder/deleteStockOrder";

    /** 接口-获取库存列表 */
    public static String SYS_SERVICE_LIST_STORE = SYS_SERVICE_BASE_URL + "/admin/app/product/listStoreDetail";

    /** 接口-获取产品销售列表 */
    public static String SYS_SERVICE_LIST_SELL_PRODUCT = SYS_SERVICE_BASE_URL + "/admin/app/sellOrder/getSellProductPage";
}
