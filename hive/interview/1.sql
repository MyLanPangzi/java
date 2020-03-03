create table if not exists user_visit
(
    userId     string,
    visitDate  string,
    visitCount int
)
    row format delimited
        fields terminated by ',';
load data local inpath '/opt/module/data/1.txt' OVERWRITE into table user_visit;
-- 要求使用SQL统计出每个用户的累积访问次数，如下表所示：
-- 用户id 月份 小计 累积
-- +-----------+----------+------+---------------+--+
-- | t.userid  | t.vdate  | _c2  | sum_window_1  |
-- +-----------+----------+------+---------------+--+
-- | u01       | 2017-1   | 11   | 23            |
-- | u01       | 2017-2   | 12   | 23            |
-- | u02       | 2017-1   | 12   | 12            |
-- | u03       | 2017-1   | 8    | 8             |
-- | u04       | 2017-1   | 3    | 3             |
-- +-----------+----------+------+---------------+--+
select *
from (select userId,
             substr(visitDate, 1, 6)                                            vdate,
             sum(visitCount) over (partition by userId,substr(visitDate, 1, 6)) sum_month,
             sum(visitCount) over (partition by userId)                         sum_all
      from user_visit
     ) t
group by userId, vdate, sum_month, sum_all;