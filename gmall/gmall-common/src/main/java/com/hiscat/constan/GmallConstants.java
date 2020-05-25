package com.hiscat.constan;

/**
 * @author hiscat
 */
public class GmallConstants {
    public static final Integer REDIS_KEY_EXPIRE = 120;
    public static final String KAFKA_TOPIC_STARTUP = "GMALL_STARTUP";
    public static final String KAFKA_TOPIC_EVENT = "GMALL_EVENT";

    public static final String KAFKA_TOPIC_NEW_ORDER = "GMALL_NEW_ORDER";
    public static final String KAFKA_TOPIC_ORDER_DETAIL = "GMALL_ORDER_DETAIL";
    public static final String KAFKA_TOPIC_ORDER_INFO = "GMALL_ORDER_INFO";
    public static final String KAFKA_TOPIC_USER_INFO = "GMALL_USER_INFO";

    public static final String ES_INDEX_DAU = "gmall2020_dau";
    public static final String ES_INDEX_NEW_MID = "gmall2020_new_mid";
    public static final String ES_INDEX_NEW_ORDER = "gmall2020_new_order";
    public static final String ES_INDEX_SALE_DETAIL = "gmall2020_sale_detail";
    public static final String ES_ALERT_INDEX = "gmall2020_coupon_alert_";

    public static final String ORDER_INFO_TABLE = "order_info";
    public static final String ORDER_DETAIL_TABLE = "order_detail";
    public static final String USER_INFO_TABLE = "user_info";
}
