CREATE TABLE `emp`(
                               `empno` int,
                               `ename` string,
                               `job` string,
                               `mgr` int,
                               `birthday` string,
                               `sal` double,
                               `comm` double,
                               `deptno` int)
    ROW FORMAT DELIMITED
        FIELDS TERMINATED BY '\t'
    STORED AS INPUTFORMAT
        'org.apache.hadoop.mapred.TextInputFormat'
        OUTPUTFORMAT
            'org.apache.hadoop.hive.ql.io.HiveIgnoreKeyTextOutputFormat'
    LOCATION
        'hdfs://mycluster/world/emp'
    TBLPROPERTIES (
        'COLUMN_STATS_ACCURATE'='true',
        'transient_lastDdlTime'='1582856319');
create table dept
(
    col    int not null,
    deptno int not null,
    dname  int not null,
    loc    int not null
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

insert overwrite local directory '/opt/module/data/student'
select * from student;
load data local inpath '/opt/module/data/student.txt' into table student partition (month='202002');
insert overwrite local directory '/opt/module/data/student'
row format delimited
fields terminated by '\t'
select * from student;
insert overwrite directory '/student'
row format delimited
fields terminated by '\t'
select * from student;
truncate table student;
msck repair table student;
load data local inpath '/opt/module/data/emp.txt' into table emp;

select e.deptno,avg(e.sal) from emp e group by e.deptno;
select e.deptno,max(e.sal) from emp e group by e.deptno;
select e.deptno,avg(e.sal) avg_sal from emp e group by e.deptno having avg_sal > 2000;
select e.ename,d.dname from emp e join dept d on e.deptno = d.deptno;
select e.ename,d.dname from emp e left join dept d on e.deptno = d.deptno;
select e.ename,d.dname from emp e right join dept d on e.deptno = d.deptno;
select e.ename,d.dname from emp e full join  dept d on e.deptno = d.deptno;
create table if not exists location(
    loc int,
    loc_name string
)
row format delimited
fields terminated by '\t';
load data local inpath '/opt/module/data/location.txt' into table location;
select e.ename,d.dname,l.loc_name
from emp e
join dept d on e.deptno = d.deptno
join location l on l.loc = d.loc;
select * from emp, dept;
select *  from emp order by sal;
select *  from emp order by sal desc ;
select ename, sal*2 s2 from emp order by s2 desc ;
select ename, deptno,sal from emp order by deptno,sal;
select * from emp sort by emp.empno desc;
insert overwrite local directory '/opt/module/data/sortby-result'
select * from emp sort by emp.empno desc;
insert overwrite local directory '/opt/module/data/distributeby-result'
select * from emp distribute by emp.deptno sort by emp.empno desc;
insert overwrite local directory '/opt/module/data/clusterby-result'
select * from emp cluster by deptno;

create table if not exists student(
    id int, name string
)
clustered by(id)
into 4 buckets
row format delimited
fields terminated by '\t';
load data local inpath '/opt/module/data/stu.txt' into table student;
desc formatted student;
create table if not exists stu(id int, name string)
row format delimited
fields terminated by '\t';
insert overwrite table stu
select id,name
from student;
truncate table student;
insert overwrite table student
select id,name from stu;
select * from student tablesample ( bucket 1 out of 4 on id);
load data local inpath '/opt/module/data/emp.txt' into table emp;
select nvl(comm, 0) from emp;
select nvl(comm, mgr) from emp;
create table if not exists emp_sex(
    name string,dept_id string, sex string
)
row format delimited
fields terminated by '\t';
select dept_id,
       sum(case sex when '男' then 1 else 0 end ) m,
       sum(case sex when '女' then 1 else 0 end ) f
from emp_sex
group by dept_id;