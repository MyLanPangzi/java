add jar /opt/module/hive/lib/udf.jar;
create temporary function mylower as 'com.hiscat.udf.Lower';
drop temporary function mylower;

add jar /opt/module/hive/lib/udf.jar;
create temporary function mysplit as 'com.hiscat.udf.MySplit';
select mysplit('a,b',',');
drop temporary function mysplit;
