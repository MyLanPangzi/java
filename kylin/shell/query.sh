#!/usr/bin/env bash
#!/bin/bash
cube_name=order_cube
do_date=`date -d '-1 day' +%F`

#获取00:00时间戳
start_date_unix=`date -d "$do_date 08:00:00" +%s`
start_date=$(($start_date_unix*1000))

#获取24:00的时间戳
stop_date=$(($start_date+86400000))

curl -X POST  -H "Authorization: Basic QURNSU46S1lMSU4=" \
-H 'Content-Type: application/json' \
-d '{
   "sql":"select sum(total_amount), gender from dwd_fact_order_detail o left join dwd_dim_user_info_his_view uv on uv.id = o.user_id group by gender",
   "offset":0,
   "limit":50000,
   "acceptPartial":false,
   "project":"order"
}' \
http://hadoop102:7070/kylin/api/query

