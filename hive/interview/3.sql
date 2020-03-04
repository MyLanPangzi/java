create table if not exists user_low_carbon
(
    user_id    String,
    data_dt    String,
    low_carbon int
) row format delimited fields terminated by '\t';
create table if not exists plant_carbon
(
    plant_id   string,
    plant_name String,
    low_carbon int
) row format delimited fields terminated by '\t';
load data local inpath "/opt/module/data/user_low_carbon.txt" into table user_low_carbon;
load data local inpath "/opt/module/data/plant_carbon.txt" into table plant_carbon;
select *
from plant_carbon;
select *
from user_low_carbon;
-- 1.蚂蚁森林植物申领统计
-- 问题：假设2017年1月1日开始记录低碳数据（user_low_carbon），假设2017年10月1日之前满足申领条件的用户都申领了一颗p004-胡杨，
-- 剩余的能量全部用来领取“p002-沙柳” 。
-- 统计在10月1日累计申领“p002-沙柳” 排名前10的用户信息；以及他比后一名多领了几颗沙柳。
-- 得到的统计结果如下表样式：
-- user_id  plant_count less_count(比后一名多领了几颗沙柳)
-- u_101    1000         100
-- u_088    900          400
-- u_103    500          …
-- 累计1-10的能量
-- 减去胡杨的能量
-- 剩余能量/沙柳能量
-- 然后倒序排序
-- 开窗，计算下一名的差距
select *, t.plant_count - lead(t.plant_count, 1) over (order by plant_count desc )
from (select u.user_id, floor((u.total_low_carbon - p004.low_carbon) / p002.low_carbon) plant_count
      from (select user_id, sum(low_carbon) total_low_carbon
            from user_low_carbon
            where to_date(regexp_replace(data_dt, '/', '-')) between to_date('2017-01-01') and to_date('2017-10-01')
            group by user_id ) u
               join (select low_carbon from plant_carbon where plant_id = 'p004') p004
               join (select low_carbon from plant_carbon where plant_id = 'p002') p002
      order by plant_count desc
      limit 10) t;

-- 2、蚂蚁森林低碳用户排名分析
-- 问题：查询user_low_carbon表中每日流水记录，条件为：
-- 用户在2017年，连续三天（或以上）的天数里，
-- 每天减少碳排放（low_carbon）都超过100g的用户低碳流水。
-- 需要查询返回满足以上条件的user_low_carbon表中的记录流水。
-- 例如用户u_002符合条件的记录如下，因为2017/1/2~2017/1/5连续四天的碳排放量之和都大于等于100g：
-- seq（key） user_id data_dt  low_carbon
-- xxxxx10    u_002  2017/1/2  150
-- xxxxx11    u_002  2017/1/2  70
-- xxxxx12    u_002  2017/1/3  30
-- xxxxx13    u_002  2017/1/3  80
-- xxxxx14    u_002  2017/1/4  150
-- xxxxx14    u_002  2017/1/5  101
-- 先合计每天的能量,过滤掉低于100的天数
-- 显示昨天,今天,明天
-- 过滤掉没有昨天+1不等于今天,明天减一不等于今天的数据
select t.user_id, t.data_dt, u.low_carbon
from (select u.user_id,
             to_date(regexp_replace(u.data_dt, '/', '-')) data_dt,
             u.low_carbon
      from user_low_carbon u) u
         join (select *
               from (select t.user_id,
                            t.data_dt,
                            lag(t.data_dt, 1) over (partition by user_id order by t.data_dt)  pre,
                            lag(t.data_dt, 2) over (partition by user_id order by t.data_dt)  pre2,
                            lead(t.data_dt, 1) over (partition by user_id order by t.data_dt) next,
                            lead(t.data_dt, 2) over (partition by user_id order by t.data_dt) next2,
                            sum(low_carbon)                                                   low_carbon_sum
                     from (select u.user_id,
                                  to_date(regexp_replace(u.data_dt, '/', '-')) data_dt,
                                  u.low_carbon
                           from user_low_carbon u) t
                     where year(to_date(regexp_replace(data_dt, '/', '-'))) = 2017
                     group by t.user_id, t.data_dt
                     having low_carbon_sum > 100) t
               where (date_add(t.pre, 1) = t.data_dt and date_add(t.data_dt, 1) = t.next)
                  or (date_add(t.data_dt, -1) = t.pre and date_add(t.data_dt, -2) = t.pre2)
                  or (date_add(t.data_dt, 1) = t.next and date_add(t.data_dt, 2) = t.next2)) t
              on u.user_id = t.user_id and u.data_dt = t.data_dt;
;

