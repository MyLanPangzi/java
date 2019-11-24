# 显示可能应用在这张表中的索引， 一个或多个。 查询涉及到的字段上若存在索引， 则该索引将被列出，
# 但不一定被查询实际使用
create index idx_emp_age_name on t_emp(age,name);
explain select SQL_NO_CACHE * from t_emp e where e.age = 10 and e.name like 'ab%';
explain select SQL_NO_CACHE * from t_emp e where e.age = 10 and e.name like '%ab%';
desc t_emp;
# ①先看索引上字段的类型+长度比如 int=4 ; varchar(20) =20 ; char(20) =20
# ②如果是 varchar 或者 char 这种字符串字段， 视字符集要乘不同的值， 比如 utf-8 要乘 3,GBK 要乘 2，
# ③varchar 这种动态字符串要加 2 个字节
# ④允许为空的字段要加 1 个字节
# 第一组： key_len=age 的字节长度+name 的字节长度=4+1 + ( 20*3+2)=5+62=67
# 第二组： key_len=age 的字节长度=4+1=5

# 显示索引的哪一列被使用了， 如果可能的话， 是一个常数。 哪些列或常量被用于查找索引列上的值。
drop table if exists emp;
create table emp(id int primary key , name varchar(20), dep_no int );
drop table if exists dep;
create table dep(id int primary key , name varchar(20));
create index idx_emp_dep_no_name on emp(dep_no,name);
create index idx_emp_name_1 on emp(name);
explain select * from emp, dep where emp.name = 'a' and emp.dep_no=dep.id;
# rows 列显示 MySQL 认为它执行查询时必须检查的行数。 越少越好！