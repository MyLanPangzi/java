-- 1. 窗口函数  需求 三 四 五
-- 2. 排名函数  rank  row_number  DENSE_RANK
-- 3. 使用常用的其他函数(日期类,字符串类,集合类)
-- 4. 自定义函数  UDT  UDTF
-- 5. 测试压缩和存储
-- 6. 优化
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
create table log_text
(
    track_time  string,
    url         string,
    session_id  string,
    referer     string,
    ip          string,
    end_user_id string,
    city_id     string
)
    row format delimited fields terminated by '\t'
    stored as textfile;
create table log_orc
(
    track_time  string,
    url         string,
    session_id  string,
    referer     string,
    ip          string,
    end_user_id string,
    city_id     string
)
    row format delimited fields terminated by '\t'
    stored as orc;
create table log_parquet
(
    track_time  string,
    url         string,
    session_id  string,
    referer     string,
    ip          string,
    end_user_id string,
    city_id     string
)
    row format delimited fields terminated by '\t'
    stored as parquet;
create table log_orc_none
(
    track_time  string,
    url         string,
    session_id  string,
    referer     string,
    ip          string,
    end_user_id string,
    city_id     string
)
    row format delimited fields terminated by '\t'
    stored as orc tblproperties ("orc.compress" = "NONE");
create table log_orc_snappy
(
    track_time  string,
    url         string,
    session_id  string,
    referer     string,
    ip          string,
    end_user_id string,
    city_id     string
)
    row format delimited fields terminated by '\t'
    stored as orc tblproperties ("orc.compress" = "SNAPPY");
load data local inpath '/opt/module/data/log.data' into table log_text;

set hive.exec.mode.local.auto=true;
set hive.exec.mode.local.auto=false;
set hive.exec.compress.intermediate=true;
set mapreduce.map.output.compress=true;
set mapreduce.map.output.compress.codec=org.apache.hadoop.io.compress.SnappyCodec;
select count(ename)
from emp;

set hive.exec.compress.output=true;
set mapreduce.output.fileoutputformat.compress=true;
set mapreduce.output.fileoutputformat.compress.codec=org.apache.hadoop.io.compress.SnappyCodec;
set mapreduce.output.fileoutputformat.compress.type=BLOCK;
insert overwrite local directory
    '/opt/module/data/distribute-result'
select *
from emp distribute by deptno sort by empno desc;

dfs -du -h /user/hive/warehouse/log_text;
insert into table log_orc
select *
from log_text;
dfs -du -h /user/hive/warehouse/log_orc/ ;
insert into table log_parquet
select *
from log_text;
dfs -du -h /user/hive/warehouse/log_parquet/;
select count(*)
from log_text;
select count(*)
from log_orc;
select count(*)
from log_parquet;
insert into table log_orc_none
select *
from log_text;
dfs -du -h /user/hive/warehouse/log_orc_none/ ;
insert into table log_orc_snappy
select *
from log_text;
dfs -du -h /user/hive/warehouse/log_orc_snappy/ ;
select count(*)
from log_orc_snappy;