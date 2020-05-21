create table gmall2020_dau
(
              mid varchar,
              uid varchar,
              appid varchar,
              area varchar,
              os varchar,
              ch varchar,
              type varchar,
              vs varchar,
              logDate varchar,
              logHour varchar,
              ts bigint
              CONSTRAINT dau_pk PRIMARY KEY (mid, logDate));

select count(*) from gmall2020_dau where logDate = '2020-05-21';
select * from gmall2020_dau;