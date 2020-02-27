create table dept
(
    col int not null
);
create table dept_partition
(
    col int not null
);

truncate table dept;
truncate table dept_partition;

load data inpath '/dept.txt' into table dept;
select * from dept;
load data local inpath '/opt/module/data/dept.txt' into table dept;
select * from dept;
load data inpath '/dept.txt' OVERWRITE into table dept;
select * from dept;

create table student(id int, name string)
partitioned by (month string)
row format delimited
fields terminated by '\t';
insert into student (id, name, month) values (1,'hello','world');
insert into student partition (month='202002')values (1, 'hello') ;

insert into table student partition (month='202003')
select id,name from student where month = '202002';
insert overwrite table student partition (month='202003')
select id,name from student where month = '202002';
from student
insert into student partition (month='202004')
select id,name where month='202002'
insert into student partition (month='202005')
select id,name where month='202003';

create table if not exists student2
as select id,name from student;
create table if not exists student3(
    id int, name string
)
row format delimited
fields terminated by '\t'
location '/user/hive/warehouse/student3';