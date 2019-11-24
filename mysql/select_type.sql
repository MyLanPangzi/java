explain
select *
from (select t1.content
      from t1) a;

explain
select t1.id
from t1
where t1.id = (select t2.id from t2 where t2.id = 2);

explain
select t1.id
from t1
where id in (select t3.id from t3 where t3.content = 'aaaa');

explain
select t1.id
from t1
where id = (select t3.id from t3 where t3.id = @@sort_buffer_size);

explain
select *
from t1
union all
select *
from t2;

explain
select *
from (select *
      from t1
      union all
      select *
      from t2) a;


