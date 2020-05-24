create table gmall2020_dau
(
              mid varchar,
              uid varchar,
              appid varchar,
              area varchar,
              os varchar,
              ch varchar,
              type varchar,
              vs varchar,
              logDate varchar,
              logHour varchar,
              ts bigint
              CONSTRAINT dau_pk PRIMARY KEY (mid, logDate));

select count(*) from gmall2020_dau where logDate = '2020-05-21';
select * from gmall2020_dau;
create table gmall2020_order_info
(          id varchar primary key,
           province_id varchar,
           consignee varchar,
           order_comment varchar,
           consignee_tel varchar,
           order_status varchar,
           payment_way varchar,
           user_id varchar,
           img_url varchar,
           total_amount double,
           expire_time varchar,
           delivery_address varchar,
           create_time varchar,
           operate_time varchar,
           tracking_no varchar,
           parent_order_id varchar,
           out_trade_no varchar,
           trade_body varchar,
           create_date varchar,
           create_hour varchar);

select * from gmall2020_order_info;
select count(*) from gmall2020_order_info;
select sum(total_amount) from gmall2020_order_info where create_date='2020-05-23';