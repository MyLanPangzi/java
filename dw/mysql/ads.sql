use gmall_report;
show tables;
create table ads_appraise_bad_topN
(
    dt                 varchar(255),
    sku_id             varchar(255),
    appraise_bad_ratio decimal(10, 2)
);
create table ads_product_cart_topN
(
    dt       varchar(255) comment '????',
    sku_id   varchar(255) comment '??ID',
    cart_num bigint comment '???????'
);
create table ads_product_favor_topN
(
    dt          varchar(255) comment '????',
    sku_id      varchar(255) comment '??ID',
    favor_count bigint comment '???'
);
create table ads_product_info
(
    dt      varchar(255) comment '????',
    sku_num varchar(255) comment 'sku??',
    spu_num varchar(255) comment 'spu??'
);
create table ads_product_refund_topN
(
    dt           varchar(255) comment '????',
    sku_id       varchar(255) comment '??ID',
    refund_ratio decimal(10, 2) comment '???'
);
create table ads_product_sale_topN
(
    dt             varchar(255) comment '????',
    sku_id         varchar(255) comment '??ID',
    payment_amount bigint comment '??'
);
select * from ads_product_sale_topN
where dt in ('2020-09-18','2020-09-19')
order by dt, payment_amount desc

select * from ads_product_info
where dt in ('2020-09-18','2020-09-19')

select * from ads_product_cart_topN
where dt in ('2020-09-18','2020-09-19')
order by dt, cart_num desc

select distinct sku_id, refund_ratio, dt from ads_product_refund_topN
where dt in ('2020-09-18','2020-09-19')
order by dt, refund_ratio desc

select distinct dt, sku_id, appraise_bad_ratio from ads_appraise_bad_topN
where dt in ('2020-09-18','2020-09-19')
order by dt, appraise_bad_ratio desc