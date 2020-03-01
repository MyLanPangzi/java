create table if not exists test_rank
(
    a string,
    b int
) row format delimited fields terminated by ',' stored as textfile;
create table score
(
    name    string,
    subject string,
    score   int
)
    row format delimited
        fields terminated by '\t';
load data local inpath '/opt/module/data/rank.txt' into table test_rank;
load data local inpath '/opt/module/data/score.txt' into table score;

select *,
       row_number() over w,
       rank() over w,
       dense_rank() over w
from score
    window w as (partition by subject order by score desc );
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
