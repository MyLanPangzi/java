CREATE TABLE `emp`
(
    `empno`    int,
    `ename`    string,
    `job`      string,
    `mgr`      int,
    `birthday` string,
    `sal`      double,
    `comm`     double,
    `deptno`   int
)
    ROW FORMAT DELIMITED
        FIELDS TERMINATED BY '\t'
    STORED AS INPUTFORMAT
        'org.apache.hadoop.mapred.TextInputFormat'
        OUTPUTFORMAT
            'org.apache.hadoop.hive.ql.io.HiveIgnoreKeyTextOutputFormat'
    LOCATION
        'hdfs://mycluster/world/emp'
    TBLPROPERTIES (
        'COLUMN_STATS_ACCURATE' = 'true',
        'numFiles' = '0',
        'totalSize' = '0',
        'transient_lastDdlTime' = '1582896530');
CREATE TABLE `emp_sex`
(
    `name`    string,
    `dept_id` string,
    `sex`     string
)
    ROW FORMAT DELIMITED
        FIELDS TERMINATED BY '\t'
    STORED AS INPUTFORMAT
        'org.apache.hadoop.mapred.TextInputFormat'
        OUTPUTFORMAT
            'org.apache.hadoop.hive.ql.io.HiveIgnoreKeyTextOutputFormat'
    LOCATION
        'hdfs://mycluster/user/hive/warehouse/emp_sex'
    TBLPROPERTIES (
        'transient_lastDdlTime' = '1582897144');
create table if not exists human
(
    name    string,
    xingzuo string,
    blood   string
)
    row format delimited
        fields terminated by '\t';
select dept_id, sum(if(sex == '男', 1, 0)), sum(if(sex == '女', 1, 0))
from emp_sex
group by dept_id;
create table if not exists movie
(
    name     string,
    category string
)
    row format delimited
        fields terminated by '\t';
create table business
(
    name      string,
    orderdate string,
    cost      int
) ROW FORMAT DELIMITED FIELDS TERMINATED BY ',';

load data local inpath "/opt/module/data/business.txt" into table business;

load data local inpath '/opt/module/data/human.txt' into table human;
load data local inpath '/opt/module/data/movies.txt' into table movie;
truncate table human;
select concat(blood, '|', xingzuo), concat_ws('|', collect_set(name))
from human
group by concat(blood, '|', xingzuo);
select name, c
from movie LATERAL VIEW explode(split(category, ',')) m as c;

-- （1） 查询在2017年4月份购买过的顾客及总人数
select name,count(*) over() from business where month(orderdate) = 4 group by name;
-- select name,count(*)  from business where month(orderdate) = 4 group by name;
--     （2） 查询顾客的购买明细及月购买总额
select *,sum(cost) over(partition by month(orderdate),name) from business;
--     查询每个客户的购买明细以及月购买额
select *,sum(cost) over(partition by month(orderdate)) from business;
--     （3） 上述的场景,要将cost按照日期进行累加
select *,sum(cost) over(order by orderdate) from business;
--     （4） 查询顾客上次的购买时间
select *,lag(orderdate,1,'1900-01-01') over(partition by name order by orderdate ) as time1 from business;

select *,
       sum(cost) over() as overall,--所有行相加
       sum(cost) over(partition by name) as myname,--按name分组，组内数据相加
       sum(cost) over(partition by name order by orderdate) as acc_by_date,--按name分组，组内数据累加
       sum(cost) over(partition by name order by orderdate rows between UNBOUNDED PRECEDING and current row ) as start_cur ,--和sample3一样,由起点到当前行的聚合
       sum(cost) over(partition by name order by orderdate rows between 1 PRECEDING and current row) as pre_cur, --当前行和前面一行做聚合
       sum(cost) over(partition by name order by orderdate rows between 1 PRECEDING AND 1 FOLLOWING ) as pre_cur_next,--当前行和前边一行及后面一行
       sum(cost) over(partition by name order by orderdate rows between current row and UNBOUNDED FOLLOWING ) as cur_end --当前行及后面所有行
from business;

