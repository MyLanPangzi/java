drop table if exists dwd_start_log;
CREATE EXTERNAL TABLE dwd_start_log
(
    `mid_id`       string,
    `user_id`      string,
    `version_code` string,
    `version_name` string,
    `lang`         string,
    `source`       string,
    `os`           string,
    `area`         string,
    `model`        string,
    `brand`        string,
    `sdk_version`  string,
    `gmail`        string,
    `height_width` string,
    `app_time`     string,
    `network`      string,
    `lng`          string,
    `lat`          string,
    `entry`        string,
    `open_ad_type` string,
    `action`       string,
    `loading_time` string,
    `detail`       string,
    `extend1`      string
)
    PARTITIONED BY (dt string)
    stored as parquet
    location '/warehouse/gmall/dwd/dwd_start_log/'
    TBLPROPERTIES ('parquet.compression' = 'lzo');
insert overwrite table dwd_start_log
    PARTITION (dt = '2020-03-10')
select get_json_object(line, '$.mid')          mid_id,
       get_json_object(line, '$.uid')          user_id,
       get_json_object(line, '$.vc')           version_code,
       get_json_object(line, '$.vn')           version_name,
       get_json_object(line, '$.l')            lang,
       get_json_object(line, '$.sr')           source,
       get_json_object(line, '$.os')           os,
       get_json_object(line, '$.ar')           area,
       get_json_object(line, '$.md')           model,
       get_json_object(line, '$.ba')           brand,
       get_json_object(line, '$.sv')           sdk_version,
       get_json_object(line, '$.g')            gmail,
       get_json_object(line, '$.hw')           height_width,
       get_json_object(line, '$.t')            app_time,
       get_json_object(line, '$.nw')           network,
       get_json_object(line, '$.ln')           lng,
       get_json_object(line, '$.la')           lat,
       get_json_object(line, '$.entry')        entry,
       get_json_object(line, '$.open_ad_type') open_ad_type,
       get_json_object(line, '$.action')       action,
       get_json_object(line, '$.loading_time') loading_time,
       get_json_object(line, '$.detail')       detail,
       get_json_object(line, '$.extend1')      extend1
from ods_start_log
where dt = '2020-03-10';
drop table if exists dwd_base_event_log;
CREATE EXTERNAL TABLE dwd_base_event_log
(
    `mid_id`       string,
    `user_id`      string,
    `version_code` string,
    `version_name` string,
    `lang`         string,
    `source`       string,
    `os`           string,
    `area`         string,
    `model`        string,
    `brand`        string,
    `sdk_version`  string,
    `gmail`        string,
    `height_width` string,
    `app_time`     string,
    `network`      string,
    `lng`          string,
    `lat`          string,
    `event_name`   string,
    `event_json`   string,
    `server_time`  string
)
    PARTITIONED BY (`dt` string)
    stored as parquet
    location '/warehouse/gmall/dwd/dwd_base_event_log/'
    TBLPROPERTIES ('parquet.compression' = 'lzo');
create function base_analizer as 'com.hiscat.hive.BaseFieldUDF'
    using jar 'hdfs://hadoop102:9000/user/hive/jars/udtf-1.0-SNAPSHOT.jar';
create function flat_analizer as 'com.hiscat.hive.EventJsonUDTF'
    using jar 'hdfs://hadoop102:9000/user/hive/jars/udtf-1.0-SNAPSHOT.jar';
drop function base_analizer;
drop function flat_analizer;
insert overwrite table dwd_base_event_log partition (dt = '2020-03-11')
select base_analizer(line, 'mid') as mid_id,
       base_analizer(line, 'uid') as user_id,
       base_analizer(line, 'vc')  as version_code,
       base_analizer(line, 'vn')  as version_name,
       base_analizer(line, 'l')   as lang,
       base_analizer(line, 'sr')  as source,
       base_analizer(line, 'os')  as os,
       base_analizer(line, 'ar')  as area,
       base_analizer(line, 'md')  as model,
       base_analizer(line, 'ba')  as brand,
       base_analizer(line, 'sv')  as sdk_version,
       base_analizer(line, 'g')   as gmail,
       base_analizer(line, 'hw')  as height_width,
       base_analizer(line, 't')   as app_time,
       base_analizer(line, 'nw')  as network,
       base_analizer(line, 'ln')  as lng,
       base_analizer(line, 'la')  as lat,
       event_name,
       event_json,
       base_analizer(line, 'st')  as server_time
from ods_event_log lateral view flat_analizer(base_analizer(line, 'et')) tmp_flat as event_name, event_json
where dt = '2020-03-11'
  and base_analizer(line, 'et') <> '';
drop table if exists dwd_display_log;
CREATE EXTERNAL TABLE dwd_display_log
(
    `mid_id`       string,
    `user_id`      string,
    `version_code` string,
    `version_name` string,
    `lang`         string,
    `source`       string,
    `os`           string,
    `area`         string,
    `model`        string,
    `brand`        string,
    `sdk_version`  string,
    `gmail`        string,
    `height_width` string,
    `app_time`     string,
    `network`      string,
    `lng`          string,
    `lat`          string,
    `action`       string,
    `goodsid`      string,
    `place`        string,
    `extend1`      string,
    `category`     string,
    `server_time`  string
)
    PARTITIONED BY (dt string)
    stored as parquet
    location '/warehouse/gmall/dwd/dwd_display_log/'
    TBLPROPERTIES ('parquet.compression' = 'lzo');
insert overwrite table dwd_display_log PARTITION (dt = '2020-03-10')
select mid_id,
       user_id,
       version_code,
       version_name,
       lang,
       source,
       os,
       area,
       model,
       brand,
       sdk_version,
       gmail,
       height_width,
       app_time,
       network,
       lng,
       lat,
       get_json_object(event_json, '$.kv.action')   action,
       get_json_object(event_json, '$.kv.goodsid')  goodsid,
       get_json_object(event_json, '$.kv.place')    place,
       get_json_object(event_json, '$.kv.extend1')  extend1,
       get_json_object(event_json, '$.kv.category') category,
       server_time
from dwd_base_event_log
where dt = '2020-03-10'
  and event_name = 'display';
drop table if exists dwd_newsdetail_log;
CREATE EXTERNAL TABLE dwd_newsdetail_log
(
    `mid_id`        string,
    `user_id`       string,
    `version_code`  string,
    `version_name`  string,
    `lang`          string,
    `source`        string,
    `os`            string,
    `area`          string,
    `model`         string,
    `brand`         string,
    `sdk_version`   string,
    `gmail`         string,
    `height_width`  string,
    `app_time`      string,
    `network`       string,
    `lng`           string,
    `lat`           string,
    `entry`         string,
    `action`        string,
    `goodsid`       string,
    `showtype`      string,
    `news_staytime` string,
    `loading_time`  string,
    `type1`         string,
    `category`      string,
    `server_time`   string
)
    PARTITIONED BY (dt string)
    stored as parquet
    location '/warehouse/gmall/dwd/dwd_newsdetail_log/'
    TBLPROPERTIES ('parquet.compression' = 'lzo');
insert overwrite table dwd_newsdetail_log PARTITION (dt = '2020-03-10')
select mid_id,
       user_id,
       version_code,
       version_name,
       lang,
       source,
       os,
       area,
       model,
       brand,
       sdk_version,
       gmail,
       height_width,
       app_time,
       network,
       lng,
       lat,
       get_json_object(event_json, '$.kv.entry')         entry,
       get_json_object(event_json, '$.kv.action')        action,
       get_json_object(event_json, '$.kv.goodsid')       goodsid,
       get_json_object(event_json, '$.kv.showtype')      showtype,
       get_json_object(event_json, '$.kv.news_staytime') news_staytime,
       get_json_object(event_json, '$.kv.loading_time')  loading_time,
       get_json_object(event_json, '$.kv.type1')         type1,
       get_json_object(event_json, '$.kv.category')      category,
       server_time
from dwd_base_event_log
where dt = '2020-03-10'
  and event_name = 'newsdetail';
drop table if exists dwd_loading_log;
CREATE EXTERNAL TABLE dwd_loading_log
(
    `mid_id`       string,
    `user_id`      string,
    `version_code` string,
    `version_name` string,
    `lang`         string,
    `source`       string,
    `os`           string,
    `area`         string,
    `model`        string,
    `brand`        string,
    `sdk_version`  string,
    `gmail`        string,
    `height_width` string,
    `app_time`     string,
    `network`      string,
    `lng`          string,
    `lat`          string,
    `action`       string,
    `loading_time` string,
    `loading_way`  string,
    `extend1`      string,
    `extend2`      string,
    `type`         string,
    `type1`        string,
    `server_time`  string
)
    PARTITIONED BY (dt string)
    stored as parquet
    location '/warehouse/gmall/dwd/dwd_loading_log/'
    TBLPROPERTIES ('parquet.compression' = 'lzo');
insert overwrite table dwd_loading_log PARTITION (dt = '2020-03-10')
select mid_id,
       user_id,
       version_code,
       version_name,
       lang,
       source,
       os,
       area,
       model,
       brand,
       sdk_version,
       gmail,
       height_width,
       app_time,
       network,
       lng,
       lat,
       get_json_object(event_json, '$.kv.action')       action,
       get_json_object(event_json, '$.kv.loading_time') loading_time,
       get_json_object(event_json, '$.kv.loading_way')  loading_way,
       get_json_object(event_json, '$.kv.extend1')      extend1,
       get_json_object(event_json, '$.kv.extend2')      extend2,
       get_json_object(event_json, '$.kv.type')         type,
       get_json_object(event_json, '$.kv.type1')        type1,
       server_time
from dwd_base_event_log
where dt = '2020-03-10'
  and event_name = 'loading';
drop table if exists dwd_ad_log;
CREATE EXTERNAL TABLE dwd_ad_log
(
    `mid_id`       string,
    `user_id`      string,
    `version_code` string,
    `version_name` string,
    `lang`         string,
    `source`       string,
    `os`           string,
    `area`         string,
    `model`        string,
    `brand`        string,
    `sdk_version`  string,
    `gmail`        string,
    `height_width` string,
    `app_time`     string,
    `network`      string,
    `lng`          string,
    `lat`          string,
    `entry`        string,
    `action`       string,
    `contentType`  string,
    `displayMills` string,
    `itemId`       string,
    `activityId`   string,
    `server_time`  string
)
    PARTITIONED BY (dt string)
    stored as parquet
    location '/warehouse/gmall/dwd/dwd_ad_log/'
    TBLPROPERTIES ('parquet.compression' = 'lzo');
insert overwrite table dwd_ad_log PARTITION (dt = '2020-03-10')
select mid_id,
       user_id,
       version_code,
       version_name,
       lang,
       source,
       os,
       area,
       model,
       brand,
       sdk_version,
       gmail,
       height_width,
       app_time,
       network,
       lng,
       lat,
       get_json_object(event_json, '$.kv.entry')        entry,
       get_json_object(event_json, '$.kv.action')       action,
       get_json_object(event_json, '$.kv.contentType')  contentType,
       get_json_object(event_json, '$.kv.displayMills') displayMills,
       get_json_object(event_json, '$.kv.itemId')       itemId,
       get_json_object(event_json, '$.kv.activityId')   activityId,
       server_time
from dwd_base_event_log
where dt = '2020-03-10'
  and event_name = 'ad';
drop table if exists dwd_notification_log;
CREATE EXTERNAL TABLE dwd_notification_log
(
    `mid_id`       string,
    `user_id`      string,
    `version_code` string,
    `version_name` string,
    `lang`         string,
    `source`       string,
    `os`           string,
    `area`         string,
    `model`        string,
    `brand`        string,
    `sdk_version`  string,
    `gmail`        string,
    `height_width` string,
    `app_time`     string,
    `network`      string,
    `lng`          string,
    `lat`          string,
    `action`       string,
    `noti_type`    string,
    `ap_time`      string,
    `content`      string,
    `server_time`  string
)
    PARTITIONED BY (dt string)
    stored as parquet
    location '/warehouse/gmall/dwd/dwd_notification_log/'
    TBLPROPERTIES ('parquet.compression' = 'lzo');
insert overwrite table dwd_notification_log PARTITION (dt = '2020-03-10')
select mid_id,
       user_id,
       version_code,
       version_name,
       lang,
       source,
       os,
       area,
       model,
       brand,
       sdk_version,
       gmail,
       height_width,
       app_time,
       network,
       lng,
       lat,
       get_json_object(event_json, '$.kv.action')    action,
       get_json_object(event_json, '$.kv.noti_type') noti_type,
       get_json_object(event_json, '$.kv.ap_time')   ap_time,
       get_json_object(event_json, '$.kv.content')   content,
       server_time
from dwd_base_event_log
where dt = '2020-03-10'
  and event_name = 'notification';
drop table if exists dwd_active_background_log;
CREATE EXTERNAL TABLE dwd_active_background_log
(
    `mid_id`        string,
    `user_id`       string,
    `version_code`  string,
    `version_name`  string,
    `lang`          string,
    `source`        string,
    `os`            string,
    `area`          string,
    `model`         string,
    `brand`         string,
    `sdk_version`   string,
    `gmail`         string,
    `height_width`  string,
    `app_time`      string,
    `network`       string,
    `lng`           string,
    `lat`           string,
    `active_source` string,
    `server_time`   string
)
    PARTITIONED BY (dt string)
    stored as parquet
    location '/warehouse/gmall/dwd/dwd_background_log/'
    TBLPROPERTIES ('parquet.compression' = 'lzo');
insert overwrite table dwd_active_background_log PARTITION (dt = '2020-03-10')
select mid_id,
       user_id,
       version_code,
       version_name,
       lang,
       source,
       os,
       area,
       model,
       brand,
       sdk_version,
       gmail,
       height_width,
       app_time,
       network,
       lng,
       lat,
       get_json_object(event_json, '$.kv.active_source') active_source,
       server_time
from dwd_base_event_log
where dt = '2020-03-10'
  and event_name = 'active_background';
drop table if exists dwd_comment_log;
CREATE EXTERNAL TABLE dwd_comment_log
(
    `mid_id`       string,
    `user_id`      string,
    `version_code` string,
    `version_name` string,
    `lang`         string,
    `source`       string,
    `os`           string,
    `area`         string,
    `model`        string,
    `brand`        string,
    `sdk_version`  string,
    `gmail`        string,
    `height_width` string,
    `app_time`     string,
    `network`      string,
    `lng`          string,
    `lat`          string,
    `comment_id`   int,
    `userid`       int,
    `p_comment_id` int,
    `content`      string,
    `addtime`      string,
    `other_id`     int,
    `praise_count` int,
    `reply_count`  int,
    `server_time`  string
)
    PARTITIONED BY (dt string)
    stored as parquet
    location '/warehouse/gmall/dwd/dwd_comment_log/'
    TBLPROPERTIES ('parquet.compression' = 'lzo');
insert overwrite table dwd_comment_log PARTITION (dt = '2020-03-10')
select mid_id,
       user_id,
       version_code,
       version_name,
       lang,
       source,
       os,
       area,
       model,
       brand,
       sdk_version,
       gmail,
       height_width,
       app_time,
       network,
       lng,
       lat,
       get_json_object(event_json, '$.kv.comment_id')   comment_id,
       get_json_object(event_json, '$.kv.userid')       userid,
       get_json_object(event_json, '$.kv.p_comment_id') p_comment_id,
       get_json_object(event_json, '$.kv.content')      content,
       get_json_object(event_json, '$.kv.addtime')      addtime,
       get_json_object(event_json, '$.kv.other_id')     other_id,
       get_json_object(event_json, '$.kv.praise_count') praise_count,
       get_json_object(event_json, '$.kv.reply_count')  reply_count,
       server_time
from dwd_base_event_log
where dt = '2020-03-10'
  and event_name = 'comment';
drop table if exists dwd_favorites_log;
CREATE EXTERNAL TABLE dwd_favorites_log
(
    `mid_id`       string,
    `user_id`      string,
    `version_code` string,
    `version_name` string,
    `lang`         string,
    `source`       string,
    `os`           string,
    `area`         string,
    `model`        string,
    `brand`        string,
    `sdk_version`  string,
    `gmail`        string,
    `height_width` string,
    `app_time`     string,
    `network`      string,
    `lng`          string,
    `lat`          string,
    `id`           int,
    `course_id`    int,
    `userid`       int,
    `add_time`     string,
    `server_time`  string
)
    PARTITIONED BY (dt string)
    stored as parquet
    location '/warehouse/gmall/dwd/dwd_favorites_log/'
    TBLPROPERTIES ('parquet.compression' = 'lzo');
insert overwrite table dwd_favorites_log PARTITION (dt = '2020-03-10')
select mid_id,
       user_id,
       version_code,
       version_name,
       lang,
       source,
       os,
       area,
       model,
       brand,
       sdk_version,
       gmail,
       height_width,
       app_time,
       network,
       lng,
       lat,
       get_json_object(event_json, '$.kv.id')        id,
       get_json_object(event_json, '$.kv.course_id') course_id,
       get_json_object(event_json, '$.kv.userid')    userid,
       get_json_object(event_json, '$.kv.add_time')  add_time,
       server_time
from dwd_base_event_log
where dt = '2020-03-10'
  and event_name = 'favorites';
drop table if exists dwd_praise_log;
CREATE EXTERNAL TABLE dwd_praise_log
(
    `mid_id`       string,
    `user_id`      string,
    `version_code` string,
    `version_name` string,
    `lang`         string,
    `source`       string,
    `os`           string,
    `area`         string,
    `model`        string,
    `brand`        string,
    `sdk_version`  string,
    `gmail`        string,
    `height_width` string,
    `app_time`     string,
    `network`      string,
    `lng`          string,
    `lat`          string,
    `id`           string,
    `userid`       string,
    `target_id`    string,
    `type`         string,
    `add_time`     string,
    `server_time`  string
)
    PARTITIONED BY (dt string)
    stored as parquet
    location '/warehouse/gmall/dwd/dwd_praise_log/'
    TBLPROPERTIES ('parquet.compression' = 'lzo');
insert overwrite table dwd_praise_log PARTITION (dt = '2020-03-10')
select mid_id,
       user_id,
       version_code,
       version_name,
       lang,
       source,
       os,
       area,
       model,
       brand,
       sdk_version,
       gmail,
       height_width,
       app_time,
       network,
       lng,
       lat,
       get_json_object(event_json, '$.kv.id')        id,
       get_json_object(event_json, '$.kv.userid')    userid,
       get_json_object(event_json, '$.kv.target_id') target_id,
       get_json_object(event_json, '$.kv.type')      type,
       get_json_object(event_json, '$.kv.add_time')  add_time,
       server_time
from dwd_base_event_log
where dt = '2020-03-10'
  and event_name = 'praise';
drop table if exists dwd_error_log;
CREATE EXTERNAL TABLE dwd_error_log
(
    `mid_id`       string,
    `user_id`      string,
    `version_code` string,
    `version_name` string,
    `lang`         string,
    `source`       string,
    `os`           string,
    `area`         string,
    `model`        string,
    `brand`        string,
    `sdk_version`  string,
    `gmail`        string,
    `height_width` string,
    `app_time`     string,
    `network`      string,
    `lng`          string,
    `lat`          string,
    `errorBrief`   string,
    `errorDetail`  string,
    `server_time`  string
)
    PARTITIONED BY (dt string)
    stored as parquet
    location '/warehouse/gmall/dwd/dwd_error_log/'
    TBLPROPERTIES ('parquet.compression' = 'lzo');
insert overwrite table dwd_error_log PARTITION (dt = '2020-03-10')
select mid_id,
       user_id,
       version_code,
       version_name,
       lang,
       source,
       os,
       area,
       model,
       brand,
       sdk_version,
       gmail,
       height_width,
       app_time,
       network,
       lng,
       lat,
       get_json_object(event_json, '$.kv.errorBrief')  errorBrief,
       get_json_object(event_json, '$.kv.errorDetail') errorDetail,
       server_time
from dwd_base_event_log
where dt = '2020-03-10'
  and event_name = 'error';


DROP TABLE IF EXISTS `dwd_dim_sku_info`;
CREATE EXTERNAL TABLE `dwd_dim_sku_info`
(
    `id`             string COMMENT '商品id',
    `spu_id`         string COMMENT 'spuid',
    `price`          double COMMENT '商品价格',
    `sku_name`       string COMMENT '商品名称',
    `sku_desc`       string COMMENT '商品描述',
    `weight`         double COMMENT '重量',
    `tm_id`          string COMMENT '品牌id',
    `tm_name`        string COMMENT '品牌名称',
    `category3_id`   string COMMENT '三级分类id',
    `category2_id`   string COMMENT '二级分类id',
    `category1_id`   string COMMENT '一级分类id',
    `category3_name` string COMMENT '三级分类名称',
    `category2_name` string COMMENT '二级分类名称',
    `category1_name` string COMMENT '一级分类名称',
    `spu_name`       string COMMENT 'spu名称',
    `create_time`    string COMMENT '创建时间'
)
    COMMENT '商品维度表'
    PARTITIONED BY (`dt` string)
    stored as parquet
    location '/warehouse/gmall/dwd/dwd_dim_sku_info/'
    tblproperties ("parquet.compression" = "lzo");
insert overwrite table dwd_dim_sku_info partition (dt = '2020-03-10')
select sku.id,
       sku.spu_id,
       sku.price,
       sku.sku_name,
       sku.sku_desc,
       sku.weight,
       sku.tm_id,
       tm.tm_name,
       sku.`category3_id`,
       c3.`category2_id`,
       c2.`category1_id`,
       c3.name `category3_name`,
       c2.name `category2_name`,
       c1.name `category1_name`,
       `spu_name`,
       sku.`create_time`
from ods_sku_info sku
         join ods_base_trademark tm on sku.tm_id = tm.tm_id
         join ods_base_category3 c3 on c3.id = sku.category3_id
         join ods_base_category2 c2 on c2.id = c3.category2_id
         join ods_base_category1 c1 on c1.id = c2.category1_id
         join ods_spu_info spu on spu.id = sku.spu_id
where sku.dt = '2020-03-10';
drop table if exists dwd_dim_coupon_info;
create external table dwd_dim_coupon_info
(
    `id`               string COMMENT '购物券编号',
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
    `operate_time`     string COMMENT '修改时间',
    `expire_time`      string COMMENT '过期时间'
) COMMENT '优惠券信息表'
    PARTITIONED BY (`dt` string)
    row format delimited fields terminated by '\t'
    location '/warehouse/gmall/dwd/dwd_dim_coupon_info/';
insert overwrite table dwd_dim_coupon_info partition (dt = '2020-03-10')
select
    id,
    coupon_name,
    coupon_type,
    condition_amount,
    condition_num,
    activity_id,
    benefit_amount,
    benefit_discount,
    create_time,
    range_type,
    spu_id,
    tm_id,
    category3_id,
    limit_num,
    operate_time,
    expire_time
from ods_coupon_info
where dt = '2020-03-10';
drop table if exists dwd_dim_activity_info;
create external table dwd_dim_activity_info
(
    `id`               string COMMENT '编号',
    `activity_name`    string COMMENT '活动名称',
    `activity_type`    string COMMENT '活动类型',
    `condition_amount` string COMMENT '满减金额',
    `condition_num`    string COMMENT '满减件数',
    `benefit_amount`   string COMMENT '优惠金额',
    `benefit_discount` string COMMENT '优惠折扣',
    `benefit_level`    string COMMENT '优惠级别',
    `start_time`       string COMMENT '开始时间',
    `end_time`         string COMMENT '结束时间',
    `create_time`      string COMMENT '创建时间'
) COMMENT '活动信息表'
    PARTITIONED BY (`dt` string)
    row format delimited fields terminated by '\t'
    location '/warehouse/gmall/dwd/dwd_dim_activity_info/';
insert overwrite table dwd_dim_activity_info partition (dt = '2020-03-10')
select info.id,
       info.activity_name,
       info.activity_type,
       rule.condition_amount,
       rule.condition_num,
       rule.benefit_amount,
       rule.benefit_discount,
       rule.benefit_level,
       info.start_time,
       info.end_time,
       info.create_time
from (
         select *
         from ods_activity_info
         where dt = '2020-03-10'
     ) info
         left join
     (
         select *
         from ods_activity_rule
         where dt = '2020-03-10'
     ) rule on info.id = rule.activity_id;
DROP TABLE IF EXISTS `dwd_dim_base_province`;
CREATE EXTERNAL TABLE `dwd_dim_base_province`
(
    `id`            string COMMENT 'id',
    `province_name` string COMMENT '省市名称',
    `area_code`     string COMMENT '地区编码',
    `iso_code`      string COMMENT 'ISO编码',
    `region_id`     string COMMENT '地区id',
    `region_name`   string COMMENT '地区名称'
)
    COMMENT '地区省市表'
    location '/warehouse/gmall/dwd/dwd_dim_base_province/';
insert overwrite table dwd_dim_base_province
select bp.id,
       bp.name,
       bp.area_code,
       bp.iso_code,
       bp.region_id,
       br.region_name
from ods_base_province bp
         join ods_base_region br
              on bp.region_id = br.id;
DROP TABLE IF EXISTS `dwd_dim_date_info`;
CREATE EXTERNAL TABLE `dwd_dim_date_info`
(
    `date_id`    string COMMENT '日',
    `week_id`    int COMMENT '周',
    `week_day`   int COMMENT '周的第几天',
    `day`        int COMMENT '每月的第几天',
    `month`      int COMMENT '第几月',
    `quarter`    int COMMENT '第几季度',
    `year`       int COMMENT '年',
    `is_workday` int COMMENT '是否是周末',
    `holiday_id` int COMMENT '是否是节假日'
)
    row format delimited fields terminated by '\t'
    location '/warehouse/gmall/dwd/dwd_dim_date_info/';
load data local inpath '/opt/module/db_log/date_info.txt' into table dwd_dim_date_info;
drop table if exists dwd_fact_order_detail;
create external table dwd_fact_order_detail
(
    `id`           string COMMENT '订单编号',
    `order_id`     string COMMENT '订单号',
    `user_id`      string COMMENT '用户id',
    `sku_id`       string COMMENT 'sku商品id',
    `sku_name`     string COMMENT '商品名称',
    `order_price`  decimal(10, 2) COMMENT '商品价格',
    `sku_num`      bigint COMMENT '商品数量',
    `create_time`  string COMMENT '创建时间',
    `province_id`  string COMMENT '省份ID',
    `total_amount` decimal(20, 2) COMMENT '订单总金额'
)
    PARTITIONED BY (`dt` string)
    stored as parquet
    location '/warehouse/gmall/dwd/dwd_fact_order_detail/'
    tblproperties ("parquet.compression" = "lzo");
insert overwrite table dwd_fact_order_detail partition (dt = '2020-03-10')
select od.id,
       od.order_id,
       od.user_id,
       od.sku_id,
       od.sku_name,
       od.order_price,
       od.sku_num,
       od.create_time,
       oi.province_id,
       od.order_price * od.sku_num
from (
         select *
         from ods_order_detail
         where dt = '2020-03-10'
     ) od
         join
     (
         select *
         from ods_order_info
         where dt = '2020-03-10'
     ) oi
     on od.order_id = oi.id;
drop table if exists dwd_fact_payment_info;
create external table dwd_fact_payment_info
(
    `id`              string COMMENT '',
    `out_trade_no`    string COMMENT '对外业务编号',
    `order_id`        string COMMENT '订单编号',
    `user_id`         string COMMENT '用户编号',
    `alipay_trade_no` string COMMENT '支付宝交易流水编号',
    `payment_amount`  decimal(16, 2) COMMENT '支付金额',
    `subject`         string COMMENT '交易内容',
    `payment_type`    string COMMENT '支付类型',
    `payment_time`    string COMMENT '支付时间',
    `province_id`     string COMMENT '省份ID'
)
    PARTITIONED BY (`dt` string)
    stored as parquet
    location '/warehouse/gmall/dwd/dwd_fact_payment_info/'
    tblproperties ("parquet.compression" = "lzo");
insert overwrite table dwd_fact_payment_info partition (dt = '2020-03-10')
select pi.id,
       pi.out_trade_no,
       pi.order_id,
       pi.user_id,
       pi.alipay_trade_no,
       pi.total_amount,
       pi.subject,
       pi.payment_type,
       pi.payment_time,
       oi.province_id
from (
         select *
         from ods_payment_info
         where dt = '2020-03-10'
     ) pi
         join
     (
         select id, province_id
         from ods_order_info
         where dt = '2020-03-10'
     ) oi
     on pi.order_id = oi.id;
drop table if exists dwd_fact_order_refund_info;
create external table dwd_fact_order_refund_info
(
    `id`                 string COMMENT '编号',
    `user_id`            string COMMENT '用户ID',
    `order_id`           string COMMENT '订单ID',
    `sku_id`             string COMMENT '商品ID',
    `refund_type`        string COMMENT '退款类型',
    `refund_num`         bigint COMMENT '退款件数',
    `refund_amount`      decimal(16, 2) COMMENT '退款金额',
    `refund_reason_type` string COMMENT '退款原因类型',
    `create_time`        string COMMENT '退款时间'
) COMMENT '退款事实表'
    PARTITIONED BY (`dt` string)
    row format delimited fields terminated by '\t'
    location '/warehouse/gmall/dwd/dwd_fact_order_refund_info/';
insert overwrite table dwd_fact_order_refund_info partition (dt = '2020-03-10')
select id,
       user_id,
       order_id,
       sku_id,
       refund_type,
       refund_num,
       refund_amount,
       refund_reason_type,
       create_time
from ods_order_refund_info
where dt = '2020-03-10';
drop table if exists dwd_fact_comment_info;
create external table dwd_fact_comment_info
(
    `id`          string COMMENT '编号',
    `user_id`     string COMMENT '用户ID',
    `sku_id`      string COMMENT '商品sku',
    `spu_id`      string COMMENT '商品spu',
    `order_id`    string COMMENT '订单ID',
    `appraise`    string COMMENT '评价',
    `create_time` string COMMENT '评价时间'
) COMMENT '退款事实表'
    PARTITIONED BY (`dt` string)
    row format delimited fields terminated by '\t'
    location '/warehouse/gmall/dwd/dwd_fact_comment_info/';
insert overwrite table dwd_fact_comment_info partition (dt = '2020-03-10')
select id,
       user_id,
       sku_id,
       spu_id,
       order_id,
       appraise,
       create_time
from ods_comment_info
where dt = '2020-03-10';
drop table if exists dwd_fact_cart_info;
create external table dwd_fact_cart_info
(
    `id`           string COMMENT '编号',
    `user_id`      string COMMENT '用户id',
    `sku_id`       string COMMENT 'skuid',
    `cart_price`   string COMMENT '放入购物车时价格',
    `sku_num`      string COMMENT '数量',
    `sku_name`     string COMMENT 'sku名称 (冗余)',
    `create_time`  string COMMENT '创建时间',
    `operate_time` string COMMENT '修改时间',
    `is_ordered`   string COMMENT '是否已经下单。1为已下单;0为未下单',
    `order_time`   string COMMENT '下单时间'
) COMMENT '加购事实表'
    PARTITIONED BY (`dt` string)
    row format delimited fields terminated by '\t'
    location '/warehouse/gmall/dwd/dwd_fact_cart_info/';
insert overwrite table dwd_fact_cart_info partition (dt = '2020-03-10')
select id,
       user_id,
       sku_id,
       cart_price,
       sku_num,
       sku_name,
       create_time,
       operate_time,
       is_ordered,
       order_time
from ods_cart_info
where dt = '2020-03-10';
drop table if exists dwd_fact_favor_info;
create external table dwd_fact_favor_info
(
    `id`          string COMMENT '编号',
    `user_id`     string COMMENT '用户id',
    `sku_id`      string COMMENT 'skuid',
    `spu_id`      string COMMENT 'spuid',
    `is_cancel`   string COMMENT '是否取消',
    `create_time` string COMMENT '收藏时间',
    `cancel_time` string COMMENT '取消时间'
) COMMENT '收藏事实表'
    PARTITIONED BY (`dt` string)
    row format delimited fields terminated by '\t'
    location '/warehouse/gmall/dwd/dwd_fact_favor_info/';
insert overwrite table dwd_fact_favor_info partition (dt = '2020-03-10')
select id,
       user_id,
       sku_id,
       spu_id,
       is_cancel,
       create_time,
       cancel_time
from ods_favor_info
where dt = '2020-03-10';
drop table if exists dwd_fact_order_info;
create external table dwd_fact_order_info
(
    `id`                    string COMMENT '订单编号',
    `order_status`          string COMMENT '订单状态',
    `user_id`               string COMMENT '用户id',
    `out_trade_no`          string COMMENT '支付流水号',
    `create_time`           string COMMENT '创建时间(未支付状态)',
    `payment_time`          string COMMENT '支付时间(已支付状态)',
    `cancel_time`           string COMMENT '取消时间(已取消状态)',
    `finish_time`           string COMMENT '完成时间(已完成状态)',
    `refund_time`           string COMMENT '退款时间(退款中状态)',
    `refund_finish_time`    string COMMENT '退款完成时间(退款完成状态)',
    `province_id`           string COMMENT '省份ID',
    `activity_id`           string COMMENT '活动ID',
    `original_total_amount` string COMMENT '原价金额',
    `benefit_reduce_amount` string COMMENT '优惠金额',
    `feight_fee`            string COMMENT '运费',
    `final_total_amount`    decimal(10, 2) COMMENT '订单金额'
)
    PARTITIONED BY (`dt` string)
    stored as parquet
    location '/warehouse/gmall/dwd/dwd_fact_order_info/'
    tblproperties ("parquet.compression" = "lzo");
set hive.exec.dynamic.partition.mode=nonstrict;
insert overwrite table dwd_fact_order_info partition (dt)
select if(new.id is null, old.id, new.id),
       if(new.order_status is null, old.order_status, new.order_status),
       if(new.user_id is null, old.user_id, new.user_id),
       if(new.out_trade_no is null, old.out_trade_no, new.out_trade_no),
       if(new.tms['1001'] is null, old.create_time, new.tms['1001']),--1001对应未支付状态
       if(new.tms['1002'] is null, old.payment_time, new.tms['1002']),
       if(new.tms['1003'] is null, old.cancel_time, new.tms['1003']),
       if(new.tms['1004'] is null, old.finish_time, new.tms['1004']),
       if(new.tms['1005'] is null, old.refund_time, new.tms['1005']),
       if(new.tms['1006'] is null, old.refund_finish_time, new.tms['1006']),
       if(new.province_id is null, old.province_id, new.province_id),
       if(new.activity_id is null, old.activity_id, new.activity_id),
       if(new.original_total_amount is null, old.original_total_amount, new.original_total_amount),
       if(new.benefit_reduce_amount is null, old.benefit_reduce_amount, new.benefit_reduce_amount),
       if(new.feight_fee is null, old.feight_fee, new.feight_fee),
       if(new.final_total_amount is null, old.final_total_amount, new.final_total_amount),
       date_format(if(new.tms['1001'] is null, old.create_time, new.tms['1001']), 'yyyy-MM-dd')
from (
         select id,
                order_status,
                user_id,
                out_trade_no,
                create_time,
                payment_time,
                cancel_time,
                finish_time,
                refund_time,
                refund_finish_time,
                province_id,
                activity_id,
                original_total_amount,
                benefit_reduce_amount,
                feight_fee,
                final_total_amount
         from dwd_fact_order_info
         where dt
                   in
               (
                   select date_format(operate_time, '%Y-%m-%d')
                   from ods_order_info
                   where dt = '2020-03-10'
               )
     ) old
         full outer join
     (
         select info.id,
                info.order_status,
                info.user_id,
                info.out_trade_no,
                info.province_id,
                act.activity_id,
                log.tms,
                info.original_total_amount,
                info.benefit_reduce_amount,
                info.feight_fee,
                info.final_total_amount
         from (
                  select order_id,
                         str_to_map(concat_ws(',', collect_set(concat(order_status, '=', operate_time))), ',', '=') tms
                  from ods_order_status_log
                  where dt = '2020-03-10'
                  group by order_id
              ) log
                  join
              (
                  select *
                  from ods_order_info
                  where dt = '2020-03-10'
              ) info
              on log.order_id = info.id
                  left join
              (
                  select *
                  from ods_activity_order
                  where dt = '2020-03-10'
              ) act
              on log.order_id = act.order_id
     ) new
     on old.id = new.id;
drop table if exists dwd_fact_coupon_use;
create external table dwd_fact_coupon_use
(
    `id`            string COMMENT '编号',
    `coupon_id`     string COMMENT '优惠券ID',
    `user_id`       string COMMENT 'skuid',
    `order_id`      string COMMENT 'spuid',
    `coupon_status` string COMMENT '优惠券状态',
    `get_time`      string COMMENT '领取时间',
    `using_time`    string COMMENT '使用时间(下单)',
    `used_time`     string COMMENT '使用时间(支付)'
) COMMENT '优惠券领用事实表'
    PARTITIONED BY (`dt` string)
    row format delimited fields terminated by '\t'
    location '/warehouse/gmall/dwd/dwd_fact_coupon_use/';
set hive.exec.dynamic.partition.mode=nonstrict;
insert overwrite table dwd_fact_coupon_use partition (dt)
select if(new.id is null, old.id, new.id),
       if(new.coupon_id is null, old.coupon_id, new.coupon_id),
       if(new.user_id is null, old.user_id, new.user_id),
       if(new.order_id is null, old.order_id, new.order_id),
       if(new.coupon_status is null, old.coupon_status, new.coupon_status),
       if(new.get_time is null, old.get_time, new.get_time),
       if(new.using_time is null, old.using_time, new.using_time),
       if(new.used_time is null, old.used_time, new.used_time),
       date_format(if(new.get_time is null, old.get_time, new.get_time), 'yyyy-MM-dd')
from (
         select id,
                coupon_id,
                user_id,
                order_id,
                coupon_status,
                get_time,
                using_time,
                used_time
         from dwd_fact_coupon_use
         where dt in
               (
                   select date_format(get_time, 'yyyy-MM-dd')
                   from ods_coupon_use
                   where dt = '2020-03-10'
               )
     ) old
         full outer join
     (
         select id,
                coupon_id,
                user_id,
                order_id,
                coupon_status,
                get_time,
                using_time,
                used_time
         from ods_coupon_use
         where dt = '2020-03-10'
     ) new
     on old.id = new.id;
drop table if exists dwd_dim_user_info_his;
create external table dwd_dim_user_info_his
(
    `id`           string COMMENT '用户id',
    `name`         string COMMENT '姓名',
    `birthday`     string COMMENT '生日',
    `gender`       string COMMENT '性别',
    `email`        string COMMENT '邮箱',
    `user_level`   string COMMENT '用户等级',
    `create_time`  string COMMENT '创建时间',
    `operate_time` string COMMENT '操作时间',
    `start_date`   string COMMENT '有效开始日期',
    `end_date`     string COMMENT '有效结束日期'
) COMMENT '订单拉链表'
    stored as parquet
    location '/warehouse/gmall/dwd/dwd_dim_user_info_his/'
    tblproperties ("parquet.compression" = "lzo");
insert overwrite table dwd_dim_user_info_his
select id,
       name,
       birthday,
       gender,
       email,
       user_level,
       create_time,
       operate_time,
       '2020-03-10',
       '9999-99-99'
from ods_user_info oi
where oi.dt = '2020-03-10';
drop table if exists dwd_dim_user_info_his_tmp;
create external table dwd_dim_user_info_his_tmp
(
    `id`           string COMMENT '用户id',
    `name`         string COMMENT '姓名',
    `birthday`     string COMMENT '生日',
    `gender`       string COMMENT '性别',
    `email`        string COMMENT '邮箱',
    `user_level`   string COMMENT '用户等级',
    `create_time`  string COMMENT '创建时间',
    `operate_time` string COMMENT '操作时间',
    `start_date`   string COMMENT '有效开始日期',
    `end_date`     string COMMENT '有效结束日期'
) COMMENT '订单拉链临时表'
    stored as parquet
    location '/warehouse/gmall/dwd/dwd_dim_user_info_his_tmp/'
    tblproperties ("parquet.compression" = "lzo");
insert overwrite table dwd_dim_user_info_his_tmp
select *
from (
         select id,
                name,
                birthday,
                gender,
                email,
                user_level,
                create_time,
                operate_time,
                '2020-03-11' start_date,
                '9999-99-99' end_date
         from ods_user_info
         where dt = '2020-03-11'

         union all
         select uh.id,
                uh.name,
                uh.birthday,
                uh.gender,
                uh.email,
                uh.user_level,
                uh.create_time,
                uh.operate_time,
                uh.start_date,
                if(ui.id is not null and uh.end_date = '9999-99-99', date_add(ui.dt, -1), uh.end_date) end_date
         from dwd_dim_user_info_his uh
                  left join
              (
                  select *
                  from ods_user_info
                  where dt = '2020-03-11'
              ) ui on uh.id = ui.id
     ) his
order by his.id, start_date;
insert overwrite table dwd_dim_user_info_his
select *
from dwd_dim_user_info_his_tmp;
