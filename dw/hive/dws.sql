use gmall;
drop table if exists dws_uv_detail_daycount;
create external table if not exists dws_uv_detail_daycount
(
    mid_id       string comment '设备ID',
    user_id      string comment '用户ID',
    version_code string comment '版本号',
    version_name string comment '版本名称',
    lang         string comment '语言',
    source       string comment '渠道',
    os           string comment '系统版本',
    area         string comment '区域',
    model        string comment '手机型号',
    brand        string comment '手机品牌',
    sdk_version  string comment 'sdk版本',
    gmail        string comment 'gmail',
    height_width string comment '宽高',
    app_time     string comment '客户端时间',
    network      string comment '网络模式',
    lng          string comment '经度',
    lat          string comment '纬度',
    login_count  bigint comment '活跃次数'
)
    partitioned by (dt string)
    stored as parquet
    location '/warehouse/gmall/dws/dws_uv_detail_daycount';
drop table if exists dws_user_action_daycount;
create external table dws_user_action_daycount
(
    user_id        string comment '用户ID',
    login_count    bigint comment '登录次数',
    cart_count     bigint comment '加入购物车次数',
    cart_amount    double comment '加入购物车金额',
    order_count    bigint comment '下单次数',
    order_amount   double comment '下单金额',
    payment_count  bigint comment '支付次数',
    payment_amount double comment '支付金额'
)
    partitioned by (dt string)
    stored as parquet
    location '/warehouse/gmall/dws/dws_user_action_daycount'
    tblproperties ('parquet.compression' = 'lzo');
drop table if exists dws_sku_action_daycount;
create external table if not exists dws_sku_action_daycount
(
    sku_id                 string comment 'skuID',
    order_count            bigint comment '被下单次数',
    order_num              bigint comment '被下单件数',
    order_amount           decimal(16, 2) comment '被下单金额',
    payment_count          bigint comment '被支付次数',
    payment_num            bigint comment '被支付件数',
    payment_amount         decimal(16, 2) comment '被支付金额',
    refund_count           bigint comment '被退款次数',
    refund_num             bigint comment '被退款件数',
    refund_amount          decimal(16, 2) comment '被退款金额',
    cart_count             bigint comment '被加入购物车次数',
    cart_num               bigint comment '被加入购物车件数',
    favor_count            bigint comment '被收藏次数',
    appraise_good_count    bigint comment '好评数',
    appraise_mid_count     bigint comment '中评数',
    appraise_bad_count     bigint comment '差评数',
    appraise_default_count bigint comment '默认评价数'
) comment '每日商品行为'
    partitioned by (dt string)
    stored as parquet
    location '/warehouse/gmall/dws/dws_sku_action_daycount';
drop table if exists dws_sale_detail_daycount;
create external table if not exists dws_sale_detail_daycount
(
    user_id            string comment '用户ID',
    sku_id             string comment '商品 id',
    user_gender        string comment '用户性别',
    user_age           string comment '用户年龄',
    user_level         string comment '用户等级',
    order_price        decimal(16, 2) comment '商品价格',
    sku_name           string comment '商品名称',
    sku_tm_id          string comment '品牌id',
    sku_category3_id   string comment '商品三级品类id',
    sku_category2_id   string comment '商品二级品类id',
    sku_category1_id   string comment '商品一级品类id',
    sku_category3_name string comment '商品三级品类名称',
    sku_category2_name string comment '商品二级品类名称',
    sku_category1_name string comment '商品一级品类名称',
    spu_id             string comment '商品 spu',
    sku_num            bigint comment '购买个数',
    order_count        bigint comment '当日下单单数',
    order_amount       decimal(16, 2) comment '当日下单金额'
) comment '每日购买行为'
    partitioned by (dt string)
    stored as parquet
    location '/warehouse/gmall/dws/dws_sale_detail_daycount';
drop table if exists dws_coupon_use_daycount;
create external table dws_coupon_use_daycount
(
    `coupon_id`        string COMMENT '优惠券ID',
    `coupon_name`      string COMMENT '购物券名称',
    `coupon_type`      string COMMENT '购物券类型 1 现金券 2 折扣券 3 满减券 4 满件打折券',
    `condition_amount` string COMMENT '满额数',
    `condition_num`    string COMMENT '满件数',
    `activity_id`      string COMMENT '活动编号',
    `benefit_amount`   string COMMENT '减金额',
    `benefit_discount` string COMMENT '折扣',
    `create_time`      string COMMENT '创建时间',
    `range_type`       string COMMENT '范围类型 1、商品 2、品类 3、品牌',
    `spu_id`           string COMMENT '商品id',
    `tm_id`            string COMMENT '品牌id',
    `category3_id`     string COMMENT '品类id',
    `limit_num`        string COMMENT '最多领用次数',
    `get_count`        bigint COMMENT '领用次数',
    `using_count`      bigint COMMENT '使用(下单)次数',
    `used_count`       bigint COMMENT '使用(支付)次数'
) COMMENT '每日优惠券统计'
    PARTITIONED BY (`dt` string)
    stored as parquet
    location '/warehouse/gmall/dws/dws_coupon_use_daycount/'
    tblproperties ("parquet.compression" = "lzo");
drop table if exists dws_activity_info_daycount;
create external table dws_activity_info_daycount
(
    `id`            string COMMENT '编号',
    `activity_name` string COMMENT '活动名称',
    `activity_type` string COMMENT '活动类型',
    `start_time`    string COMMENT '开始时间',
    `end_time`      string COMMENT '结束时间',
    `create_time`   string COMMENT '创建时间',
    `order_count`   bigint COMMENT '下单次数',
    `payment_count` bigint COMMENT '支付次数'
) COMMENT '购物车信息表'
    PARTITIONED BY (`dt` string)
    row format delimited fields terminated by '\t'
    location '/warehouse/gmall/dws/dws_activity_info_daycount/'
    tblproperties ("parquet.compression" = "lzo");
drop table if exists dws_uv_topic;
create external table if not exists dws_uv_topic
(
    mid_id           string comment '设备ID',
    user_id          string comment '用户ID',
    version_code     string comment '版本号',
    version_name     string comment '版本名称',
    lang             string comment '语言',
    source           string comment '渠道',
    os               string comment '系统版本',
    area             string comment '区域',
    model            string comment '手机型号',
    brand            string comment '手机品牌',
    sdk_version      string comment 'sdk版本',
    gmail            string comment 'gmail',
    height_width     string comment '宽高',
    app_time         string comment '客户端时间',
    network          string comment '网络模式',
    lng              string comment '经度',
    lat              string comment '纬度',
    login_date_first string comment '首次活跃时间',
    login_date_last  string comment '最后活跃时间',
    login_day_count  bigint comment '当日活跃次数',
    login_count      bigint comment '累计活跃次数'
)
    stored as parquet
    location '/warehouse/gmall/dws/dws_uv_detail_daycount';
drop table if exists dwt_user_topic;
create external table dwt_user_topic
(
    user_id                 string comment '用户id',
    login_date_first        string comment '首次登录时间',
    login_date_last         string comment '末次登录时间',
    login_count             bigint comment '累积登录天数',
    login_last_30d_count    bigint comment '最近30日登录天数',
    order_date_first        string comment '首次下单时间',
    order_date_last         string comment '末次下单时间',
    order_count             bigint comment '累积下单次数',
    order_amount            decimal(16, 2) comment '累积下单金额',
    order_last_30d_count    bigint comment '最近30日下单次数',
    order_last_30d_amount   bigint comment '最近30日下单金额',
    payment_date_first      string comment '首次支付时间',
    payment_date_last       string comment '末次支付时间',
    payment_count           decimal(16, 2) comment '累积支付次数',
    payment_amount          decimal(16, 2) comment '累积支付金额',
    payment_last_30d_count  decimal(16, 2) comment '最近30日支付次数',
    payment_last_30d_amount decimal(16, 2) comment '最近30日支付金额'
) COMMENT '用户主题宽表'
    stored as parquet
    location '/warehouse/gmall/dwt/dwt_user_topic/'
    tblproperties ("parquet.compression" = "lzo");
drop table if exists dwt_sku_topic;
create external table dwt_sku_topic
(
    sku_id                          string comment 'sku_id',
    spu_id                          string comment 'spu_id',
    order_last_30d_count            bigint comment '最近30日被下单次数',
    order_last_30d_num              bigint comment '最近30日被下单件数',
    order_last_30d_amount           decimal(16, 2) comment '最近30日被下单金额',
    order_count                     bigint comment '累积被下单次数',
    order_num                       bigint comment '累积被下单件数',
    order_amount                    decimal(16, 2) comment '累积被下单金额',
    payment_last_30d_count          bigint comment '最近30日被支付次数',
    payment_last_30d_num            bigint comment '最近30日被支付件数',
    payment_last_30d_amount         decimal(16, 2) comment '最近30日被支付金额',
    payment_count                   bigint comment '累积被支付次数',
    payment_num                     bigint comment '累积被支付件数',
    payment_amount                  decimal(16, 2) comment '累积被支付金额',
    refund_last_30d_count           bigint comment '最近三十日退款次数',
    refund_last_30d_num             bigint comment '最近三十日退款件数',
    refund_last_30d_amount          decimal(10, 2) comment '最近三十日退款金额',
    refund_count                    bigint comment '累积退款次数',
    refund_num                      bigint comment '累积退款件数',
    refund_amount                   decimal(10, 2) comment '累积退款金额',
    cart_last_30d_count             bigint comment '最近30日被加入购物车次数',
    cart_last_30d_num               bigint comment '最近30日被加入购物车件数',
    cart_count                      bigint comment '累积被加入购物车次数',
    cart_num                        bigint comment '累积被加入购物车件数',
    favor_last_30d_count            bigint comment '最近30日被收藏次数',
    favor_count                     bigint comment '累积被收藏次数',
    appraise_last_30d_good_count    bigint comment '最近30日好评数',
    appraise_last_30d_mid_count     bigint comment '最近30日中评数',
    appraise_last_30d_bad_count     bigint comment '最近30日差评数',
    appraise_last_30d_default_count bigint comment '最近30日默认评价数',
    appraise_good_count             bigint comment '累积好评数',
    appraise_mid_count              bigint comment '累积中评数',
    appraise_bad_count              bigint comment '累积差评数',
    appraise_default_count          bigint comment '累积默认评价数'
) COMMENT '商品主题宽表'
    stored as parquet
    location '/warehouse/gmall/dwt/dwt_sku_topic/'
    tblproperties ("parquet.compression" = "lzo");
drop table if exists dwt_coupon_topic;
create external table dwt_coupon_topic
(
    `coupon_id`       string COMMENT '优惠券ID',
    `get_day_count`   bigint COMMENT '当日领用次数',
    `using_day_count` bigint COMMENT '当日使用(下单)次数',
    `used_day_count`  bigint COMMENT '当日使用(支付)次数',
    `get_count`       bigint COMMENT '累积领用次数',
    `using_count`     bigint COMMENT '累积使用(下单)次数',
    `used_count`      bigint COMMENT '累积使用(支付)次数'
) COMMENT '购物券主题宽表'
    stored as parquet
    location '/warehouse/gmall/dwt/dwt_coupon_topic/'
    tblproperties ("parquet.compression" = "lzo");
drop table if exists dwt_activity_topic;
create external table dwt_activity_topic
(
    `id`                string COMMENT '活动id',
    `activity_name`     string COMMENT '活动名称',
    `order_day_count`   bigint COMMENT '当日日下单次数',
    `payment_day_count` bigint COMMENT '当日支付次数',
    `order_count`       bigint COMMENT '累积下单次数',
    `payment_count`     bigint COMMENT '累积支付次数'
) COMMENT '活动主题宽表'
    row format delimited fields terminated by '\t'
    location '/warehouse/gmall/dwt/dwt_activity_topic/'
    tblproperties ("parquet.compression" = "lzo");
