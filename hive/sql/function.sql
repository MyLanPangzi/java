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
create table if not exists t(
                                id int,
                                ti tinyint,
                                si smallint,
                                bi bigint,
                                f float,
                                d double,
                                dec decimal(9,1),
                                s string,
                                v varchar(20),
                                cchar char(2),
                                cbool boolean,
                                t timestamp,
                                cdate date,
--     cinterval interval,
                                carr array<string>,
                                cmap map<string, string>,
                                cstruct struct<name: string, gender: string>,
                                cunion uniontype<string, int>
)
    row format delimited
        collection items terminated by ','
        map keys terminated by ':';
load data local inpath "/opt/module/data/business.txt" into table business;
load data local inpath '/opt/module/data/human.txt' into table human;
load data local inpath '/opt/module/data/movies.txt' into table movie;
truncate table human;
select concat(blood, '|', xingzuo), concat_ws('|', collect_set(name))
from human
group by concat(blood, '|', xingzuo);
insert into t(id, ti, si, bi, f, d, dec, s, v, cchar, cbool, t, cdate, carr, cmap, cstruct, cunion)
select  1,2,3,4,5,6.0,7.0,'s','varchar','char',true,unix_timestamp(),
        '2020-03-01',array('a','b'),map('a','b'), named_struct('name','hello','gender', 'f'), create_union(0, '1', 1)
from business limit 1;
select name, c
from movie LATERAL VIEW explode(split(category, ',')) m as c;

-- （1） 查询在2017年4月份购买过的顾客及总人数
select name,count(*) over() c
from business
where month(orderdate) = 4
group by name
WINDOW w AS ();
--     （2） 查询顾客的购买明细及月购买总额
select *,sum(cost) over w
from business
window w as (distribute by name);
--     （3） 上述的场景,要将cost按照日期进行累加
select *,sum(cost) over w
from business
window w as (distribute by name sort by orderdate);

-- 3. 窗口函数:
--    扩展需求三:
--     当前行和上一行
select *,sum(cost) over w
from business
window w as (distribute by name sort by orderdate rows between 1 preceding and current row);
--     当前行和下一行
select *,sum(cost) over w
from business
window w as (distribute by name sort by orderdate rows between current row and 1 following);
--     当前行和上一行及下一行
select *,sum(cost) over w
from business
window w as (distribute by name sort by orderdate rows between 1 preceding and 1 following);
--     当前行和网上第2行 及  往下第2行
select *,
       cost +
       lag(cost, 2, 0) over w  +
       lead(cost, 2, 0) over w
from business
    window w as (order by orderdate);
select *,datediff(orderdate, lag(orderdate, 1,'1900-01-01') over w)
from business
    window w as (order by orderdate);

--     （4） 查询顾客上次的购买时间
select *,
       first_value(orderdate) over w first,
       last_value(orderdate) over w last,
       lag(orderdate,1) over w pre,
       lead(orderdate,1) over w next
from business
window w as (distribute by name sort by orderdate);
--     （5） 查询前20%时间的订单信息
select *
from (select *, ntile(5) over w line
      from business
          window w as (order by orderdate)
) t
where t.line = 1;

