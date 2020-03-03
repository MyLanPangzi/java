create table if not exists visit
(
    user_id string,
    shop    string
) row format delimited fields terminated by '\t';
-- 1）每个店铺的UV（访客数）
-- 2）每个店铺访问次数top3的访客信息。输出店铺名称、访客id、访问次数
load data local inpath '/opt/module/data/2.txt' overwrite into table visit;
select shop, count(*)
from visit
group by shop;
select *
from (select *, rank() over (partition by shop order by visit_count desc ) drank
      from (select shop, user_id, count(*) visit_count
            from visit
            group by shop, user_id) t
     ) t
where t.drank <= 3;