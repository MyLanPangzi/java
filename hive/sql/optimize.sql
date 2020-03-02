CREATE TABLE IF NOT EXISTS  `emp`
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
create table if not exists bigtable
(
    id        bigint,
    `time`      bigint,
    uid       string,
    keyword   string,
    url_rank  int,
    click_num int,
    click_url string
) row format delimited fields terminated by '\t';
create table if not exists smalltable
(
    id        bigint,
    `time`      bigint,
    uid       string,
    keyword   string,
    url_rank  int,
    click_num int,
    click_url string
) row format delimited fields terminated by '\t';
create table if not exists jointable
(
    id        bigint,
    `time`      bigint,
    uid       string,
    keyword   string,
    url_rank  int,
    click_num int,
    click_url string
) row format delimited fields terminated by '\t';
create table if not exists ori
(
    id        bigint,
    `time`      bigint,
    uid       string,
    keyword   string,
    url_rank  int,
    click_num int,
    click_url string
) row format delimited fields terminated by '\t';
create table if not exists nullidtable
(
    id        bigint,
    `time`      bigint,
    uid       string,
    keyword   string,
    url_rank  int,
    click_num int,
    click_url string
) row format delimited fields terminated by '\t';
create table if not exists jointable
(
    id        bigint,
    `time`    bigint,
    uid       string,
    keyword   string,
    url_rank  int,
    click_num int,
    click_url string
) row format delimited fields terminated by '\t';
create table if not exists ori_partitioned
(
    id        bigint,
    `time`      bigint,
    uid       string,
    keyword   string,
    url_rank  int,
    click_num int,
    click_url string
)
    partitioned by (p_time bigint)
    row format delimited fields terminated by '\t';
create table if not exists ori_partitioned_target
(
    id        bigint,
    `time`      bigint,
    uid       string,
    keyword   string,
    url_rank  int,
    click_num int,
    click_url string
) PARTITIONED BY (p_time STRING) row format delimited fields terminated by '\t';
load data local inpath '/opt/module/data/bigtable' into table bigtable;
load data local inpath '/opt/module/data/smalltable' into table smalltable;
load data local inpath '/opt/module/data/bigtable' into table ori;
load data local inpath '/opt/module/data/nullid' into table nullidtable;
-- noinspection SqlResolve @ column/"p_time"
load data local inpath '/opt/module/data/smalltable' into table ori_partitioned partition (p_time = '20111230000010');
-- noinspection SqlResolve @ column/"p_time"
load data local inpath '/opt/module/data/smalltable' into table ori_partitioned partition (p_time = '20111230000011');

-- Fetch抓取
set hive.fetch.task.conversion=none;
select *
from emp;
select ename
from emp;
select ename
from emp
limit 3;

set hive.fetch.task.conversion=more;
select *
from emp;
select ename
from emp;
select ename
from emp
limit 3;

-- 本地模式
set hive.exec.mode.local.auto=true;
-- //开启本地mr
-- //设置local mr的最大输入数据量，当输入数据量小于这个值时采用local  mr的方式，默认为134217728，即128M
set hive.exec.mode.local.auto.inputbytes.max=50000000;
-- //设置local mr的最大输入文件个数，当输入文件个数小于这个值时采用local mr的方式，默认为4
set hive.exec.mode.local.auto.input.files.max=10;
set hive.exec.mode.local.auto=true;
select *
from emp cluster by deptno;

set hive.exec.mode.local.auto=false;
select *
from emp cluster by deptno;

-- 小表、大表Join
set hive.auto.convert.join = false;
insert overwrite table jointable
select b.id, b.time, b.uid, b.keyword, b.url_rank, b.click_num, b.click_url
from smalltable s
         left join bigtable b on b.id = s.id;
insert overwrite table jointable
select b.id, b.time, b.uid, b.keyword, b.url_rank, b.click_num, b.click_url
from bigtable b
         left join smalltable s on s.id = b.id;

-- 大表Join大表
set mapreduce.job.reduces = 5;
insert overwrite table jointable
select n.*
from nullidtable n
         left join ori o on n.id = o.id;
insert overwrite table jointable
select n.*
from (select * from nullidtable where id is not null) n
         left join ori o on n.id = o.id;
set mapreduce.job.reduces = 5;
insert overwrite table jointable
select n.*
from nullidtable n
         left join ori b on n.id = b.id;
insert overwrite table jointable
select n.*
from nullidtable n
         full join ori o on if(n.id is null, concat('hive', rand()), n.id) = o.id;

-- MapJoin
set hive.auto.convert.join = true;
insert overwrite table jointable
select b.id, b.time, b.uid, b.keyword, b.url_rank, b.click_num, b.click_url
from smalltable s
         join bigtable b on s.id = b.id;
insert overwrite table jointable
select b.id, b.time, b.uid, b.keyword, b.url_rank, b.click_num, b.click_url
from bigtable b
         join smalltable s on s.id = b.id;

set hive.groupby.skewindata = true;

--  Count(Distinct) 去重统计
set mapreduce.job.reduces = 5;
select count(distinct id)
from bigtable;
select count(id)
from (select id from bigtable group by id) a;

-- 行列过滤
select o.id
from bigtable b
         join ori o on o.id = b.id
where o.id <= 10;
select b.id
from bigtable b
         join (select id from ori where id <= 10) o on b.id = o.id;

-- 动态分区调整
set hive.exec.dynamic.partition=true;
set hive.exec.dynamic.partition.mode=nonstrict;
set hive.exec.max.dynamic.partitions=1000;
set hive.error.on.empty.partition=false;
insert overwrite table ori_partitioned_target partition (p_time)
select id,
       `time`,
       uid,
       keyword,
       url_rank,
       click_num,
       click_url,
       p_time
from ori_partitioned;
show partitions ori_partitioned_target;