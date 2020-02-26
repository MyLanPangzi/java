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
show databases ;
show databases like '%hello';
desc database hello ;
alter database hello SET DBPROPERTIES ('hello'='world');
desc database extended hello;
