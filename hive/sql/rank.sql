create table if not exists test_rank
(
    a string,
    b int
) row format delimited fields terminated by ',' stored as textfile;
load data local inpath '/opt/module/data/rank.txt' into table test_rank;
-- ROW_NUMBER
-- RANK
-- DENSE_RANK
-- CUME_DIST
-- PERCENT_RANK
-- NTILE
select *,
       cume_dist() over w, --组内小于等于当前行 / 总行数
       percent_rank() over w, -- 组内rank值 - 1  / 总rank -1
       ntile(3) over w, -- 组内数据切片，并编号
       row_number() over w, --组内编号
       rank() over w, -- 组内编号，同值留下空号
       dense_rank() over w --组内编号，同值不留空号
from test_rank
    window w as (order by b);
select *,
       lag(a,1) over(order by b),
       sum(b) over (range between 1 preceding and 1 following)
from test_rank;
