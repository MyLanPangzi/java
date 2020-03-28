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
insert overwrite table dws_uv_detail_daycount partition (dt = '2020-03-10')
select mid_id,
       concat_ws('|', collect_set(mid_id))       user_id,
       concat_ws('|', collect_set(user_id))      version_code,
       concat_ws('|', collect_set(version_code)) version_name,
       concat_ws('|', collect_set(version_name)) lang,
       concat_ws('|', collect_set(lang))         source,
       concat_ws('|', collect_set(source))       os,
       concat_ws('|', collect_set(os))           area,
       concat_ws('|', collect_set(area))         model,
       concat_ws('|', collect_set(model))        brand,
       concat_ws('|', collect_set(brand))        sdk_version,
       concat_ws('|', collect_set(sdk_version))  gmail,
       concat_ws('|', collect_set(gmail))        height_width,
       concat_ws('|', collect_set(height_width)) app_time,
       concat_ws('|', collect_set(app_time))     network,
       concat_ws('|', collect_set(network))      lng,
       concat_ws('|', collect_set(lng))          lat,
       count(*)
from dwd_start_log log
where dt = '2020-03-10'
group by mid_id;
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
insert overwrite table dws_user_action_daycount partition (dt = '2020-03-10')
select user_id,
       sum(login_count)    login_count,
       sum(cart_count)     cart_count,
       sum(cart_amount)    cart_amount,
       sum(order_count)    order_count,
       sum(order_amount)   order_amount,
       sum(payment_count)  payment_count,
       sum(payment_amount) payment_amount
from (select log.user_id,
             count(*) login_count,
             0        cart_count,
             0        cart_amount,
             0        order_count,
             0        order_amount,
             0        payment_count,
             0        payment_amount
      from dwd_start_log log
      where dt = '2020-03-10'
      group by user_id
      union all
      select user_id,
             0 login_count,
             count(*),
             sum(cart.cart_price * cart.sku_num),
             0 order_count,
             0 order_amount,
             0 payment_count,
             0 payment_amount
      from dwd_fact_cart_info cart
      where dt = '2020-03-10'
      group by user_id
      union all
      select user_id,
             0 login_count,
             0 cart_count,
             0 cart_amount,
             count(*),
             sum(o.final_total_amount),
             0 payment_count,
             0 payment_amount
      from dwd_fact_order_info o
      where dt = '2020-03-10'
      group by user_id
      union all
      select user_id,
             0 login_count,
             0 cart_count,
             0 cart_amount,
             0 order_count,
             0 order_amount,
             count(*),
             sum(payment.payment_amount)
      from dwd_fact_payment_info payment
      where dt = '2020-03-10'
      group by user_id
     ) u
group by u.user_id;
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
with tmp_order as (
    select sku_id,
           count(*)          order_count,
           sum(sku_num)      order_num,
           sum(total_amount) order_amount
    from dwd_fact_order_detail o
    where dt = '2020-03-10'
    group by sku_id
),
     tmp_payment as (
         select sku_id,
                count(*)          payment_count,
                sum(sku_num)      payment_num,
                sum(total_amount) payment_amount
         from dwd_fact_order_detail o
         where o.dt = '2020-03-10'
           and order_id in (
             select id
             from dwd_fact_order_info
             where dt in ('2020-03-10', date_sub('2020-03-10', -1))
               and date_format(payment_time, 'yyyy-MM-dd') = '2020-03-10'
         )
         group by sku_id
     ),
     tmp_refund as (
         select sku_id,
                count(*)           refund_count,
                sum(refund_num)    refund_num,
                sum(refund_amount) refund_amount
         from dwd_fact_order_refund_info o
         where o.dt = '2020-03-10'
         group by sku_id
     ),
     tmp_cart as (
         select sku_id,
                count(*)     cart_count,
                sum(sku_num) cart_num
         from dwd_fact_cart_info o
         where o.dt = '2020-03-10'
         group by sku_id
     ),
     tmp_favor as (
         select sku_id,
                count(*) favor_count
         from dwd_fact_favor_info o
         where o.dt = '2020-03-10'
         group by sku_id
     ),
     tmp_praise as (
         select sku_id,
                sum(if(appraise = '1201', 1, 0)) appraise_good_count,
                sum(if(appraise = '1202', 1, 0)) appraise_mid_count,
                sum(if(appraise = '1203', 1, 0)) appraise_bad_count,
                sum(if(appraise = '1204', 1, 0)) appraise_default_count
         from dwd_fact_comment_info o
         where o.dt = '2020-03-10'
         group by sku_id
     )
insert
overwrite
table
dws_sku_action_daycount
partition
(
dt = '2020-03-10'
)
select sku_id,
       sum(order_count),
       sum(order_num),
       sum(order_amount),
       sum(payment_count),
       sum(payment_num),
       sum(payment_amount),
       sum(refund_count),
       sum(refund_num),
       sum(refund_amount),
       sum(cart_count),
       sum(cart_num),
       sum(favor_count),
       sum(appraise_good_count),
       sum(appraise_mid_count),
       sum(appraise_bad_count),
       sum(appraise_default_count)
from (
         select sku_id,
                order_count,
                order_num,
                order_amount,
                0 payment_count,
                0 payment_num,
                0 payment_amount,
                0 refund_count,
                0 refund_num,
                0 refund_amount,
                0 cart_count,
                0 cart_num,
                0 favor_count,
                0 appraise_good_count,
                0 appraise_mid_count,
                0 appraise_bad_count,
                0 appraise_default_count
         from tmp_order
         union all
         select sku_id,
                0 order_count,
                0 order_num,
                0 order_amount,
                payment_count,
                payment_num,
                payment_amount,
                0 refund_count,
                0 refund_num,
                0 refund_amount,
                0 cart_count,
                0 cart_num,
                0 favor_count,
                0 appraise_good_count,
                0 appraise_mid_count,
                0 appraise_bad_count,
                0 appraise_default_count
         from tmp_payment
         union all
         select sku_id,
                0 order_count,
                0 order_num,
                0 order_amount,
                0 payment_count,
                0 payment_num,
                0 payment_amount,
                refund_count,
                refund_num,
                refund_amount,
                0 cart_count,
                0 cart_num,
                0 favor_count,
                0 appraise_good_count,
                0 appraise_mid_count,
                0 appraise_bad_count,
                0 appraise_default_count
         from tmp_refund
         union all
         select sku_id,
                0 order_count,
                0 order_num,
                0 order_amount,
                0 payment_count,
                0 payment_num,
                0 payment_amount,
                0 refund_count,
                0 refund_num,
                0 refund_amount,
                cart_count,
                cart_num,
                0 favor_count,
                0 appraise_good_count,
                0 appraise_mid_count,
                0 appraise_bad_count,
                0 appraise_default_count
         from tmp_cart
         union all
         select sku_id,
                0 order_count,
                0 order_num,
                0 order_amount,
                0 payment_count,
                0 payment_num,
                0 payment_amount,
                0 refund_count,
                0 refund_num,
                0 refund_amount,
                0 cart_count,
                0 cart_num,
                favor_count,
                0 appraise_good_count,
                0 appraise_mid_count,
                0 appraise_bad_count,
                0 appraise_default_count
         from tmp_favor
         union all
         select sku_id,
                0 order_count,
                0 order_num,
                0 order_amount,
                0 payment_count,
                0 payment_num,
                0 payment_amount,
                0 refund_count,
                0 refund_num,
                0 refund_amount,
                0 cart_count,
                0 cart_num,
                0 favor_count,
                appraise_good_count,
                appraise_mid_count,
                appraise_bad_count,
                appraise_default_count
         from tmp_praise
     ) r
group by sku_id;
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
insert overwrite table dws_sale_detail_daycount partition (dt = '2020-03-10')
select op.user_id,
       op.sku_id,
       ui.gender,
       months_between('2020-03-10', ui.birthday) / 12 age,
       ui.user_level,
       si.price,
       si.sku_name,
       si.tm_id,
       si.category3_id,
       si.category2_id,
       si.category1_id,
       si.category3_name,
       si.category2_name,
       si.category1_name,
       si.spu_id,
       op.sku_num,
       op.order_count,
       op.order_amount
from (
         select user_id,
                sku_id,
                sum(sku_num)      sku_num,
                count(*)          order_count,
                sum(total_amount) order_amount
         from dwd_fact_order_detail
         where dt = '2020-03-10'
         group by user_id, sku_id
     ) op
         join
     (
         select *
         from dwd_dim_user_info_his
         where end_date = '9999-99-99'
     ) ui on op.user_id = ui.id
         join
     (
         select *
         from dwd_dim_sku_info
         where dt = '2020-03-10'
     ) si on op.sku_id = si.id;

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
insert overwrite table dws_uv_topic
select nvl(new.mid_id, old.mid_id)             mid_id,
       nvl(new.user_id, old.user_id)           user_id,
       nvl(new.version_code, old.version_code) version_code,
       nvl(new.version_name, old.version_name) version_name,
       nvl(new.lang, old.lang)                 lang,
       nvl(new.source, old.source)             source,
       nvl(new.os, old.os)                     os,
       nvl(new.area, old.area)                 area,
       nvl(new.model, old.model)               model,
       nvl(new.brand, old.brand)               brand,
       nvl(new.sdk_version, old.sdk_version)   sdk_version,
       nvl(new.gmail, old.gmail)               gmail,
       nvl(new.height_width, old.height_width) height_width,
       nvl(new.app_time, old.app_time)         app_time,
       nvl(new.network, old.network)           network,
       nvl(new.lng, old.lng)                   lng,
       nvl(new.lat, old.lat)                   lat,
       if(old.mid_id is not null, old.login_date_first, '2020-03-10'),
       if(new.mid_id is not null, '2020-03-10', old.login_date_last),
       if(new.login_count is not null, new.login_count, 0),
       nvl(old.login_count, 0) + if(new.login_count > 0, 1, 0)
from (select old.mid_id,
             old.user_id,
             old.version_code,
             old.version_name,
             old.lang,
             old.source,
             old.os,
             old.area,
             old.model,
             old.brand,
             old.sdk_version,
             old.gmail,
             old.height_width,
             old.app_time,
             old.network,
             old.lng,
             old.lat,
             login_date_first,
             login_date_last,
             login_day_count,
             login_count
      from dws_uv_topic old) old
         full outer join (select new.mid_id,
                                 new.user_id,
                                 new.version_code,
                                 new.version_name,
                                 new.lang,
                                 new.source,
                                 new.os,
                                 new.area,
                                 new.model,
                                 new.brand,
                                 new.sdk_version,
                                 new.gmail,
                                 new.height_width,
                                 new.app_time,
                                 new.network,
                                 new.lng,
                                 new.lat,
                                 new.login_count
                          from dws_uv_detail_daycount new
                          where dt = '2020-03-10') new on new.mid_id = old.mid_id;
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
insert overwrite table dwt_user_topic
select nvl(new.user_id, old.user_id),
       if(old.login_date_first is null and new.login_count > 0, '2020-03-10', old.login_date_first),
       if(new.login_count > 0, '2020-03-10', old.login_date_last),
       nvl(old.login_count, 0) + if(new.login_count > 0, 1, 0),
       nvl(new.login_last_30d_count, 0),
       if(old.order_date_first is null and new.order_count > 0, '2020-03-10', old.order_date_first),
       if(new.order_count > 0, '2020-03-10', old.order_date_last),
       nvl(old.order_count, 0) + nvl(new.order_count, 0),
       nvl(old.order_amount, 0) + nvl(new.order_amount, 0),
       nvl(new.order_last_30d_count, 0),
       nvl(new.order_last_30d_amount, 0),
       if(old.payment_date_first is null and new.payment_count > 0, '2020-03-10', old.payment_date_first),
       if(new.payment_count > 0, '2020-03-10', old.payment_date_last),
       nvl(old.payment_count, 0) + nvl(new.payment_count, 0),
       nvl(old.payment_amount, 0) + nvl(new.payment_amount, 0),
       nvl(new.payment_last_30d_count, 0),
       nvl(new.payment_last_30d_amount, 0)
from dwt_user_topic old
         full outer join
     (
         select user_id,
                sum(if(dt = '2020-03-10', login_count, 0))    login_count,
                sum(if(dt = '2020-03-10', order_count, 0))    order_count,
                sum(if(dt = '2020-03-10', order_amount, 0))   order_amount,
                sum(if(dt = '2020-03-10', payment_count, 0))  payment_count,
                sum(if(dt = '2020-03-10', payment_amount, 0)) payment_amount,
                sum(if(order_count > 0, 1, 0))                login_last_30d_count,
                sum(order_count)                              order_last_30d_count,
                sum(order_amount)                             order_last_30d_amount,
                sum(payment_count)                            payment_last_30d_count,
                sum(payment_amount)                           payment_last_30d_amount
         from dws_user_action_daycount
         where dt >= date_add('2020-03-10', -30)
         group by user_id
     ) new
     on old.user_id = new.user_id;
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
insert overwrite table dwt_sku_topic
select nvl(new.sku_id, old.sku_id),
       dwd_dim_sku_info.spu_id,
       nvl(new.order_count30, 0),
       nvl(new.order_num30, 0),
       nvl(new.order_amount30, 0),
       nvl(old.order_count, 0) + nvl(new.order_count, 0),
       nvl(old.order_num, 0) + nvl(new.order_num, 0),
       nvl(old.order_amount, 0) + nvl(new.order_amount, 0),
       nvl(new.payment_count30, 0),
       nvl(new.payment_num30, 0),
       nvl(new.payment_amount30, 0),
       nvl(old.payment_count, 0) + nvl(new.payment_count, 0),
       nvl(old.payment_num, 0) + nvl(new.payment_count, 0),
       nvl(old.payment_amount, 0) + nvl(new.payment_count, 0),
       nvl(new.refund_count30, 0),
       nvl(new.refund_num30, 0),
       nvl(new.refund_amount30, 0),
       nvl(old.refund_count, 0) + nvl(new.refund_count, 0),
       nvl(old.refund_num, 0) + nvl(new.refund_num, 0),
       nvl(old.refund_amount, 0) + nvl(new.refund_amount, 0),
       nvl(new.cart_count30, 0),
       nvl(new.cart_num30, 0),
       nvl(old.cart_count, 0) + nvl(new.cart_count, 0),
       nvl(old.cart_num, 0) + nvl(new.cart_num, 0),
       nvl(new.favor_count30, 0),
       nvl(old.favor_count, 0) + nvl(new.favor_count, 0),
       nvl(new.appraise_good_count30, 0),
       nvl(new.appraise_mid_count30, 0),
       nvl(new.appraise_bad_count30, 0),
       nvl(new.appraise_default_count30, 0),
       nvl(old.appraise_good_count, 0) + nvl(new.appraise_good_count, 0),
       nvl(old.appraise_mid_count, 0) + nvl(new.appraise_mid_count, 0),
       nvl(old.appraise_bad_count, 0) + nvl(new.appraise_bad_count, 0),
       nvl(old.appraise_default_count, 0) + nvl(new.appraise_default_count, 0)
from (
         select sku_id,
                spu_id,
                order_last_30d_count,
                order_last_30d_num,
                order_last_30d_amount,
                order_count,
                order_num,
                order_amount,
                payment_last_30d_count,
                payment_last_30d_num,
                payment_last_30d_amount,
                payment_count,
                payment_num,
                payment_amount,
                refund_last_30d_count,
                refund_last_30d_num,
                refund_last_30d_amount,
                refund_count,
                refund_num,
                refund_amount,
                cart_last_30d_count,
                cart_last_30d_num,
                cart_count,
                cart_num,
                favor_last_30d_count,
                favor_count,
                appraise_last_30d_good_count,
                appraise_last_30d_mid_count,
                appraise_last_30d_bad_count,
                appraise_last_30d_default_count,
                appraise_good_count,
                appraise_mid_count,
                appraise_bad_count,
                appraise_default_count
         from dwt_sku_topic
     ) old
         full outer join
     (
         select sku_id,
                sum(if(dt = '2020-03-10', order_count, 0))            order_count,
                sum(if(dt = '2020-03-10', order_num, 0))              order_num,
                sum(if(dt = '2020-03-10', order_amount, 0))           order_amount,
                sum(if(dt = '2020-03-10', payment_count, 0))          payment_count,
                sum(if(dt = '2020-03-10', payment_num, 0))            payment_num,
                sum(if(dt = '2020-03-10', payment_amount, 0))         payment_amount,
                sum(if(dt = '2020-03-10', refund_count, 0))           refund_count,
                sum(if(dt = '2020-03-10', refund_num, 0))             refund_num,
                sum(if(dt = '2020-03-10', refund_amount, 0))          refund_amount,
                sum(if(dt = '2020-03-10', cart_count, 0))             cart_count,
                sum(if(dt = '2020-03-10', cart_num, 0))               cart_num,
                sum(if(dt = '2020-03-10', favor_count, 0))            favor_count,
                sum(if(dt = '2020-03-10', appraise_good_count, 0))    appraise_good_count,
                sum(if(dt = '2020-03-10', appraise_mid_count, 0))     appraise_mid_count,
                sum(if(dt = '2020-03-10', appraise_bad_count, 0))     appraise_bad_count,
                sum(if(dt = '2020-03-10', appraise_default_count, 0)) appraise_default_count,
                sum(order_count)                                      order_count30,
                sum(order_num)                                        order_num30,
                sum(order_amount)                                     order_amount30,
                sum(payment_count)                                    payment_count30,
                sum(payment_num)                                      payment_num30,
                sum(payment_amount)                                   payment_amount30,
                sum(refund_count)                                     refund_count30,
                sum(refund_num)                                       refund_num30,
                sum(refund_amount)                                    refund_amount30,
                sum(cart_count)                                       cart_count30,
                sum(cart_num)                                         cart_num30,
                sum(favor_count)                                      favor_count30,
                sum(appraise_good_count)                              appraise_good_count30,
                sum(appraise_mid_count)                               appraise_mid_count30,
                sum(appraise_bad_count)                               appraise_bad_count30,
                sum(appraise_default_count)                           appraise_default_count30
         from dws_sku_action_daycount
         where dt >= date_add('2020-03-10', -30)
         group by sku_id
     ) new
     on new.sku_id = old.sku_id
         left join dwd_dim_sku_info
                   on new.sku_id = dwd_dim_sku_info.id;