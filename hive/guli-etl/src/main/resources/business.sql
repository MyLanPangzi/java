create table if not exists gulivideo_user_ori
(
    uploader string,
    videos   int,
    friends  int
)
    row format delimited
        fields terminated by "\t"
    stored as textfile;
create table if not exists gulivideo_user_orc
(
    uploader string,
    videos   int,
    friends  int
)
    row format delimited
        fields terminated by "\t"
    stored as orc TBLPROPERTIES ('orc.compress' = 'SNAPPY');
load data inpath '/user/atguigu/user/' into table gulivideo_user_ori;
insert into gulivideo_user_orc
select *
from gulivideo_user_ori;
select count(*)
from gulivideo_user_ori;
select count(*)
from gulivideo_user_orc;

create table if not exists gulivideo_ori
(
    videoId   string,
    uploader  string,
    age       int,
    category  array<string>,
    length    int,
    `views`   int,
    rate      float,
    ratings   int,
    comments  int,
    relatedId array<string>
)
    row format delimited
        fields terminated by "\t"
        collection items terminated by "&"
    stored as textfile;
create table if not exists gulivideo_orc
(
    videoId   string,
    uploader  string,
    age       int,
    category  array<string>,
    length    int,
    `views`   int,
    rate      float,
    ratings   int,
    comments  int,
    relatedId array<string>
)
    row format delimited fields terminated by "\t"
        collection items terminated by "&"
    stored as orc TBLPROPERTIES ('orc.compress' = 'SNAPPY');
load data inpath '/user/atguigu/video_output' into table gulivideo_ori;
select count(*)
from gulivideo_ori;
insert overwrite table gulivideo_orc
select *
from gulivideo_ori;
select count(*)
from gulivideo_orc;

-- 统计硅谷影音视频网站的常规指标，各种TopN指标：
-- --统计视频观看数Top10
select *
from gulivideo_orc
order by `views` desc
limit 10;
-- --统计视频类别热度Top10 sum count
select lcategory, sum(`views`) sum_views
from gulivideo_orc video lateral view explode(video.category) t as lcategory
group by lcategory
order by sum_views desc
limit 10;
-- 3 统计出视频观看数最高的20个视频的所属类别以及类别包含Top20视频的个数
select lcategory, count(*)
from (select `views`, category
      from gulivideo_orc
      order by `views` desc
      limit 20) t lateral view explode(t.category) lt as lcategory
group by lcategory;
-- --统计视频观看数Top50所关联视频的所属类别Rank
select *, rank() over (order by t.cnt desc)
from (select categories, count(*) cnt
      from (select category
            from gulivideo_orc v
                     join (select c
                           from (select relatedId, `views`
                                 from gulivideo_orc
                                 order by `views` desc
                                 limit 50) t lateral view explode(t.relatedId) v as c) t
                          on v.videoId = t.c) t lateral view explode(t.category) lvc as categories
      group by categories
      order by cnt desc
     ) t;
-- --统计每个类别中的视频热度Top10
select *
from (select *, dense_rank() over (partition by c order by `views` desc) rank
      from (select `views`, videoId, c
            from gulivideo_orc lateral view explode(category) category as c) t
     ) t
where t.rank <= 10;
-- --统计每个类别中视频流量Top10
select *
from (select *, dense_rank() over (partition by c order by `ratings` desc) rank
      from (select `ratings`, videoId, c
            from gulivideo_orc lateral view explode(category) category as c) t
     ) t
where t.rank <= 10;
-- 7 统计上传视频最多的用户Top10以及他们上传的观看次数在前20的视频
select v.uploader, v.`views`, v.videoId
from gulivideo_orc v
         join
     (select u.uploader, u.videos
      from gulivideo_user_orc u
      order by u.videos desc
      limit 10
     ) t on v.uploader = t.uploader
order by `views` desc
limit 20;
-- 统计每个类别视频观看数Top10