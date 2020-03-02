create database if not exists world
    comment 'hello world'
    location '/world'
    with dbproperties ('hello' = 'world');
drop database if exists world;
drop database if exists world cascade;
use world;

