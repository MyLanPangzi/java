create table if not exists student(
    id int,
    name string
)
row format delimited
fields terminated by '\t';
create table if not exists student2 as select id, name from student;
create table if not exists student3 like student;
desc formatted student;
load data local inpath '/opt/module/data/student.txt' into table student;
create external table if not exists dept(
    deptno int,
    dname string,
    loc int
)
row format delimited
fields terminated by '\t';

create external table if not exists emp(
    empno int,
    ename string,
    job string,
    mgr int,
    birthday string,
    sal double,
    comm double,
    deptno int
)
row format delimited
fields terminated by '\t';
desc formatted emp;
desc formatted dept;
load data local inpath '/opt/module/data/emp.txt' into table emp;
load data local inpath '/opt/module/data/dept.txt' into table dept;
select * from dept;
select * from emp;
drop table dept;
drop table emp;
alter table emp set TBLPROPERTIES ('EXTERNAL'='FALSE');
desc formatted emp;
drop table emp;
alter table dept set TBLPROPERTIES ('EXTERNAL'='FALSE');
desc formatted dept;

create table if not exists dept_partition(
    deptno int,
    dename string,
    loc string
)
partitioned by (month string)
row format delimited
fields terminated by '\t';
load data local inpath '/opt/module/data/dept.txt' into table dept_partition
    PARTITION (month='202001');
load data local inpath '/opt/module/data/dept.txt' into table dept_partition
    PARTITION (month='202002');
load data local inpath '/opt/module/data/dept.txt' into table dept_partition
    PARTITION (month='202003');
select * from dept_partition where month = '202001'
union
select * from dept_partition where month = '202003';
drop table dept_partition;

alter table dept_partition add partition (month='202004');
alter table dept_partition drop partition (month='202004');
alter table dept_partition add partition (month='202005') partition (month='202006');
alter table dept_partition drop partition (month='202005'), partition (month='202006');
show  partitions dept_partition;
desc formatted dept_partition;

alter table student rename to stu;
alter table stu add columns (desc string);
alter table stu replace columns (hello string, world string);
drop table stu;
drop table student2;
drop table student3;
