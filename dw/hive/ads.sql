use gmall;
drop table if exists ads_uv_count;
create external table ads_uv_count
(
    `dt`          string COMMENT '统计日期',
    `day_count`   bigint COMMENT '当日用户数量',
    `wk_count`    bigint COMMENT '当周用户数量',
    `mn_count`    bigint COMMENT '当月用户数量',
    `is_weekend`  string COMMENT 'Y,N是否是周末,用于得到本周最终结果',
    `is_monthend` string COMMENT 'Y,N是否是月末,用于得到本月最终结果'
) COMMENT '活跃设备数'
    row format delimited fields terminated by '\t'
    location '/warehouse/gmall/ads/ads_uv_count/';
drop table if exists ads_new_mid_count;
create external table ads_new_mid_count
(
    `create_date`   string comment '创建时间',
    `new_mid_count` BIGINT comment '新增设备数量'
) COMMENT '每日新增设备信息数量'
    row format delimited fields terminated by '\t'
    location '/warehouse/gmall/ads/ads_new_mid_count/';
drop table if exists ads_silent_count;
create external table ads_silent_count
(
    `dt`           string COMMENT '统计日期',
    `silent_count` bigint COMMENT '沉默设备数'
)
    row format delimited fields terminated by '\t'
    location '/warehouse/gmall/ads/ads_silent_count';
drop table if exists ads_back_count;
create external table ads_back_count
(
    `dt`            string COMMENT '统计日期',
    `wk_dt`         string COMMENT '统计日期所在周',
    `wastage_count` bigint COMMENT '回流设备数'
)
    row format delimited fields terminated by '\t'
    location '/warehouse/gmall/ads/ads_back_count';
drop table if exists ads_wastage_count;
create external table ads_wastage_count
(
    `dt`            string COMMENT '统计日期',
    `wastage_count` bigint COMMENT '流失设备数'
)
    row format delimited fields terminated by '\t'
    location '/warehouse/gmall/ads/ads_wastage_count';
drop table if exists ads_wastage_count;
create external table ads_wastage_count
(
    `dt`            string COMMENT '统计日期',
    `wastage_count` bigint COMMENT '流失设备数'
)
    row format delimited fields terminated by '\t'
    location '/warehouse/gmall/ads/ads_wastage_count';
drop table if exists ads_user_retention_day_rate;
create external table ads_user_retention_day_rate
(
    `stat_date`       string comment '统计日期',
    `create_date`     string comment '设备新增日期',
    `retention_day`   int comment '截止当前日期留存天数',
    `retention_count` bigint comment '留存数量',
    `new_mid_count`   bigint comment '设备新增数量',
    `retention_ratio` decimal(10, 2) comment '留存率'
) COMMENT '每日用户留存情况'
    row format delimited fields terminated by '\t'
    location '/warehouse/gmall/ads/ads_user_retention_day_rate/';
drop table if exists ads_continuity_wk_count;
create external table ads_continuity_wk_count
(
    `dt`               string COMMENT '统计日期,一般用结束周周日日期,如果每天计算一次,可用当天日期',
    `wk_dt`            string COMMENT '持续时间',
    `continuity_count` bigint COMMENT '活跃次数'
)
    row format delimited fields terminated by '\t'
    location '/warehouse/gmall/ads/ads_continuity_wk_count';
drop table if exists ads_continuity_uv_count;
create external table ads_continuity_uv_count
(
    `dt`               string COMMENT '统计日期',
    `wk_dt`            string COMMENT '最近7天日期',
    `continuity_count` bigint
) COMMENT '连续活跃设备数'
    row format delimited fields terminated by '\t'
    location '/warehouse/gmall/ads/ads_continuity_uv_count';
drop table if exists ads_user_topic;
create external table ads_user_topic
(
    `dt`                    string COMMENT '统计日期',
    `day_users`             string COMMENT '活跃会员数',
    `day_new_users`         string COMMENT '新增会员数',
    `day_new_payment_users` string COMMENT '新增消费会员数',
    `payment_users`         string COMMENT '总付费会员数',
    `users`                 string COMMENT '总会员数',
    `day_users2users`       decimal(10, 2) COMMENT '会员活跃率',
    `payment_users2users`   decimal(10, 2) COMMENT '会员付费率',
    `day_new_users2users`   decimal(10, 2) COMMENT '会员新鲜度'
) COMMENT '会员主题信息表'
    row format delimited fields terminated by '\t'
    location '/warehouse/gmall/ads/ads_user_topic';
drop table if exists ads_user_action_convert_day;
create external table ads_user_action_convert_day
(
    `dt`                          string COMMENT '统计日期',
    `total_visitor_m_count`       bigint COMMENT '总访问人数',
    `cart_u_count`                bigint COMMENT '加入购物车的人数',
    `visitor2cart_convert_ratio`  decimal(10, 2) COMMENT '访问到加入购物车转化率',
    `order_u_count`               bigint COMMENT '下单人数',
    `cart2order_convert_ratio`    decimal(10, 2) COMMENT '加入购物车到下单转化率',
    `payment_u_count`             bigint COMMENT '支付人数',
    `order2payment_convert_ratio` decimal(10, 2) COMMENT '下单到支付的转化率'
) COMMENT '用户行为漏斗分析'
    row format delimited fields terminated by '\t'
    location '/warehouse/gmall/ads/ads_user_action_convert_day/';
drop table if exists ads_product_info;
create external table ads_product_info
(
    `dt`      string COMMENT '统计日期',
    `sku_num` string COMMENT 'sku个数',
    `spu_num` string COMMENT 'spu个数'
) COMMENT '商品个数信息'
    row format delimited fields terminated by '\t'
    location '/warehouse/gmall/ads/ads_product_info';
drop table if exists ads_product_sale_topN;
create external table ads_product_sale_topN
(
    `dt`             string COMMENT '统计日期',
    `sku_id`         string COMMENT '商品ID',
    `payment_amount` bigint COMMENT '销量'
) COMMENT '商品个数信息'
    row format delimited fields terminated by '\t'
    location '/warehouse/gmall/ads/ads_product_sale_topN';
drop table if exists ads_product_favor_topN;
create external table ads_product_favor_topN
(
    `dt`          string COMMENT '统计日期',
    `sku_id`      string COMMENT '商品ID',
    `favor_count` bigint COMMENT '收藏量'
) COMMENT '商品收藏TopN'
    row format delimited fields terminated by '\t'
    location '/warehouse/gmall/ads/ads_product_favor_topN';
drop table if exists ads_product_cart_topN;
create external table ads_product_cart_topN
(
    `dt`       string COMMENT '统计日期',
    `sku_id`   string COMMENT '商品ID',
    `cart_num` bigint COMMENT '加入购物车数量'
) COMMENT '商品加入购物车TopN'
    row format delimited fields terminated by '\t'
    location '/warehouse/gmall/ads/ads_product_cart_topN';
drop table if exists ads_product_refund_topN;
create external table ads_product_refund_topN
(
    `dt`           string COMMENT '统计日期',
    `sku_id`       string COMMENT '商品ID',
    `refund_ratio` decimal(10, 2) COMMENT '退款率'
) COMMENT '商品退款率TopN'
    row format delimited fields terminated by '\t'
    location '/warehouse/gmall/ads/ads_product_refund_topN';
drop table if exists ads_appraise_bad_topN;
create external table ads_appraise_bad_topN
(
    `dt`                 string COMMENT '统计日期',
    `sku_id`             string COMMENT '商品ID',
    `appraise_bad_ratio` decimal(10, 2) COMMENT '差评率'
) COMMENT '商品差评率TopN'
    row format delimited fields terminated by '\t'
    location '/warehouse/gmall/ads/ads_appraise_bad_topN';
drop table if exists ads_order_daycount;
create external table ads_order_daycount
(
    dt           string comment '统计日期',
    order_count  bigint comment '单日下单笔数',
    order_amount bigint comment '单日下单金额',
    order_users  bigint comment '单日下单用户数'
) comment '每日订单总计表'
    row format delimited fields terminated by '\t'
    location '/warehouse/gmall/ads/ads_order_daycount';
drop table if exists ads_payment_daycount;
create external table ads_payment_daycount
(
    dt                 string comment '统计日期',
    order_count        bigint comment '单日支付笔数',
    order_amount       bigint comment '单日支付金额',
    payment_user_count bigint comment '单日支付人数',
    payment_sku_count  bigint comment '单日支付商品数',
    payment_avg_time   double comment '下单到支付的平均时长，取分钟数'
) comment '每日订单总计表'
    row format delimited fields terminated by '\t'
    location '/warehouse/gmall/ads/ads_payment_daycount';
drop table ads_sale_tm_category1_stat_mn;
create external table ads_sale_tm_category1_stat_mn
(
    tm_id                 string comment '品牌id',
    category1_id          string comment '1级品类id ',
    category1_name        string comment '1级品类名称 ',
    buycount              bigint comment '购买人数',
    buy_twice_last        bigint comment '两次以上购买人数',
    buy_twice_last_ratio  decimal(10, 2) comment '单次复购率',
    buy_3times_last       bigint comment '三次以上购买人数',
    buy_3times_last_ratio decimal(10, 2) comment '多次复购率',
    stat_mn               string comment '统计月份',
    stat_date             string comment '统计日期'
) COMMENT '复购率统计'
    row format delimited fields terminated by '\t'
    location '/warehouse/gmall/ads/ads_sale_tm_category1_stat_mn/';

insert into table ads_uv_count
select daycount.dt,
       day_count,
       wk_count,
       mn_count,
       if(`dayofweek`(daycount.dt) = 1, 'Y', 'N')        is_weekend,
       if(last_day(daycount.dt) = daycount.dt, 'Y', 'N') is_monthend
from (select '2020-03-10' dt, count(*) day_count
      from dwt_uv_topic
      where login_date_last = '2020-03-10') daycount
         inner join
     (select '2020-03-10' dt, count(*) mn_count
      from dwt_uv_topic
      where date_format(login_date_last, 'yyyy-MM') = date_format('2020-03-10', 'yyyy-MM')) weekcount
     on daycount.dt = weekcount.dt
         inner join (select '2020-03-10' dt, count(*) wk_count
                     from dwt_uv_topic
                     where login_date_last between date_sub(next_day('2020-03-10', 'MO'), 7) and date_sub(next_day('2020-03-10', 'MO'), 1)) monthcount
                    on monthcount.dt = daycount.dt;
insert into table ads_new_mid_count
select '2020-03-10', count(*)
from dwt_uv_topic
where login_date_last = '2020-03-10';
insert into table ads_silent_count
select '2020-03-10', count(*)
from dwt_uv_topic
where login_date_last < date_sub('2020-03-10', 7)
  and login_date_last = login_date_first;
insert into ads_back_count
select '2020-03-10', '2020-03-10', count(*)
from (select mid_id
      from dwt_uv_topic
      where login_date_first < date_sub(next_day('2020-03-10', 'MO'), 14)
        and login_date_last > date_sub(next_day('2020-03-10', 'SU'), 14)
     ) cur
         left join (select mid_id
                    from dws_uv_detail_daycount
                    where dt between date_sub(next_day('2020-03-10', 'MO'), 14) and date_sub(next_day('2020-03-10', 'SU'), 14)) pre
                   on cur.mid_id = pre.mid_id
where pre.mid_id is null;
insert into ads_wastage_count
select '2020-03-10', count(*)
from dwt_uv_topic
where login_date_last < date_sub('2020-03-10', 7);
select '2020-03-10',--统计日期
       date_add('2020-03-10', -1),--新增日期
       1,--留存天数
       sum(if(login_date_first = date_add('2020-03-10', -1) and login_date_last = '2020-03-10', 1, 0)),--2020-03-09的1日留存数
       sum(if(login_date_first = date_add('2020-03-10', -1), 1, 0)),--2020-03-09新增
       sum(if(login_date_first = date_add('2020-03-10', -1) and login_date_last = '2020-03-10', 1, 0)) /
       sum(if(login_date_first = date_add('2020-03-10', -1), 1, 0)) * 100
from dwt_uv_topic;
insert into table ads_continuity_wk_count
select '2020-03-10',
       concat(date_sub(next_day('2020-03-10', 'MO'), 3 * 7), '_', date_sub(next_day('2020-03-10', 'MO'), 1)),
       week3.week3 + week2.week2 + week1.week1
from (select mid_id, count(*) week3
      from dws_uv_detail_daycount
      where dt between date_sub(next_day('2020-03-10', 'MO'), 3 * 7) and date_sub(next_day('2020-03-10', 'SU'), 3 * 7)
      group by mid_id
     ) week3
         inner join (
    select mid_id, count(*) week2
    from dws_uv_detail_daycount
    where dt between date_sub(next_day('2020-03-10', 'MO'), 2 * 7) and date_sub(next_day('2020-03-10', 'SU'), 2 * 7)
    group by mid_id
) week2 on week2.mid_id = week3.mid_id

         inner join (
    select mid_id, count(*) week1
    from dws_uv_detail_daycount
    where dt between date_sub(next_day('2020-03-10', 'MO'), 7) and date_sub(next_day('2020-03-10', 'SU'), 7)
    group by mid_id
) week1 on week2.mid_id = week1.mid_id;
insert into table ads_continuity_uv_count
select '2020-03-10',
       concat(date_sub(next_day('2020-03-10', 'MO'), 7), '_', date_sub(next_day('2020-03-10', 'MO'), 1)),
       count(*)
from (
         select mid_id
         from (
                  select tmp.mid_id, date_sub(tmp.dt, num), count(*) dtcount
                  from (
                           select mid_id, dt, rank() over (PARTITION BY mid_id order by dt) num
                           from dws_uv_detail_daycount
                           where dt >= date_sub('2020-03-10', 6)
                       ) tmp
                  group by tmp.mid_id, date_sub(tmp.dt, num)
                  having count(*) >= 3
              ) tmp
         group by tmp.mid_id
     ) tmp;
insert into table ads_user_topic
select '2020-03-10',
       sum(if(login_date_last = '2020-03-10', 1, 0))            day_users,
       sum(if(login_date_first = '2020-03-10', 1, 0))           day_new_users,
       sum(if(order_date_first = '2020-03-10', 1, 0))           day_new_payment_users,
       sum(if(order_count > 0, 1, 0))                           payment_users,
       count(*)                                                 users,
       sum(if(login_date_last = '2020-03-10', 1, 0)) / count(*) day_users2users,
       sum(if(order_count > 0, 1, 0)) / count(*)                payment_users2users,
       sum(if(login_date_first = '2020-03-10', 1, 0)) /
       sum(if(login_date_last = '2020-03-10', 1, 0))            day_new_users2users
from dwt_user_topic;
insert into ads_user_action_convert_day
select uv.dt,
       uv.day_count                                                total_visitor_m_count,
       cart_u_count,
       cast(cart_u_count / uv.day_count as decimal(10, 2)) * 100   visitor2cart_convert_ratio,
       order_u_count,
       cast(order_u_count / cart_u_count as decimal(10, 2)) * 100  cart2order_convert_ratio,
       payment_u_count,
       cast(order_u_count / order_u_count as decimal(10, 2)) * 100 order2payment_convert_ratio
from (select dt,
             sum(if(cart_count > 0, 1, 0))    cart_u_count,
             sum(if(order_count > 0, 1, 0))   order_u_count,
             sum(if(payment_count > 0, 1, 0)) payment_u_count
      from dws_user_action_daycount
      where dt = '2020-03-10'
      group by dt) u
         join ads_uv_count uv on uv.dt = u.dt;
insert into table ads_product_info
select sku.dt,
       sku.sku_num,
       spu.spu_num
from (select '2020-03-10' dt,
             count(*)     sku_num
      from dwt_sku_topic sku) sku
         join (
    select '2020-03-10' dt,
           count(*)     spu_num
    from (
             select spu_id
             from dwt_sku_topic
             group by spu_id
         ) spu
) spu on spu.dt = sku.dt;
insert into table ads_product_sale_topN
select dt,
       sku_id,
       payment_amount
from dws_sku_action_daycount
where dt = '2020-03-10'
order by payment_amount desc
limit 10;
insert into table ads_product_favor_topN
select dt,
       sku_id,
       favor_count
from dws_sku_action_daycount
where dt = '2020-03-10'
order by favor_count desc
limit 10;
insert into table ads_product_cart_topN
select dt,
       sku_id,
       cart_num
from dws_sku_action_daycount
where dt = '2020-03-10'
order by cart_num desc
limit 10;
insert into table ads_product_refund_topN
select '2020-03-10'                                           dt,
       sku_id,
       sku.payment_last_30d_count / sku.refund_last_30d_count refund_ratio
from dwt_sku_topic sku
order by refund_ratio desc
limit 10;
insert into table ads_appraise_bad_topN
select '2020-03-10'                                          dt,
       sku_id,
       sku.appraise_bad_count / (sku.appraise_bad_count + sku.appraise_mid_count + sku.appraise_good_count +
                                 sku.appraise_default_count) appraise_bad_ratio
from dwt_sku_topic sku
order by appraise_bad_ratio desc
limit 10;
insert into table ads_order_daycount
select sku.dt,
       order_count,
       order_amount,
       order_users
from (select dt,
             order_count,
             order_amount
      from dws_sku_action_daycount
      where dt = '2020-03-10'
     ) sku
         inner join (
    select '2020-03-10' dt,
           count(*)     order_users
    from dws_user_action_daycount
    where dt = '2020-03-10'
      and order_count > 0
) ua on ua.dt = sku.dt;
insert into table ads_payment_daycount
select '2020-03-10' dt,
       order_count,
       order_amount,
       payment_user_count,
       payment_sku_count,
       payment_avg_time
from (
         select dt,
                sum(order_count)                 order_count,
                sum(order_amount)                order_amount,
                sum(if(payment_count > 0, 1, 0)) payment_user_count
         from dws_user_action_daycount
         where dt = '2020-03-10'
         group by dt
     ) ua
         join (
    select '2020-03-10' dt,
           count(*)     payment_sku_count
    from dws_sku_action_daycount
    where dt = '2020-03-10'
      and payment_count > 0
) sku on ua.dt = sku.dt
         join (
    select dt,
           sum(to_unix_timestamp(payment_time) - to_unix_timestamp(o.create_time)) / 60 / count(*) payment_avg_time
    from dwd_fact_order_info o
    where dt = '2020-03-10'
      and payment_time is not null
    group by dt
) tmp_order on tmp_order.dt = sku.dt;
insert into table ads_sale_tm_category1_stat_mn
select sku_tm_id,
       sku_category1_id,
       sku_category1_name,
       sum(if(order_count > 0, 1, 0))                                  buycount,
       sum(if(order_count > 1, 1, 0))                                  buy_twice_last,
       sum(if(order_count > 1, 1, 0)) / sum(if(order_count > 0, 1, 0)) buy_twice_last_ratio,
       sum(if(order_count > 2, 1, 0))                                  buy_3times_last,
       sum(if(order_count > 2, 1, 0)) / sum(if(order_count > 0, 1, 0)) buy_3times_last_ratio,
       date_format('2020-03-10', 'yyyy-MM')                            stat_mn,
       '2020-03-10'                                                    stat_date
from (
         select user_id,
                sd.sku_tm_id,
                sd.sku_category1_id,
                sd.sku_category1_name,
                sum(order_count) order_count
         from dws_sale_detail_daycount sd
         where date_format(dt, 'yyyy-MM') = date_format('2020-02-10', 'yyyy-MM')
         group by user_id, sd.sku_tm_id, sd.sku_category1_id, sd.sku_category1_name
     ) tmp
group by sku_tm_id, sku_category1_id, sku_category1_name;
