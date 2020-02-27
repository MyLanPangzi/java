create table test
(
    name     string,
    friends  array<string>,
    children map<string, int>,
    address  struct<street:string,city:string>
)
    row format delimited fields terminated by ','
        collection items terminated by '_'
        map keys terminated by ':'
        lines terminated by '\n';

load data local inpath '/opt/module/data/test' into table test;
create database if not exists hello;
drop database if exists hello cascade;
create database if not exists hello location '/hello';
use hello;
show databases;
show databases like '%hello';
desc database hello;
alter database hello SET DBPROPERTIES ('hello' = 'world');
desc database extended hello;
create table if not exists dept_partition2(
    deptno int,
    dname string,
    loc string
)
partitioned by (month string, day string)
row format delimited
fields terminated by '\t';

load data local inpath '/opt/module/data/dept.txt' into table dept_partition2 partition (month='202002',day='26');
select * from dept_partition2 where month='202002'and day='27';
msck REPAIR TABLE dept_partition2;
alter table dept_partition2 add partition (month='202002',day='29');