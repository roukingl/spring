create database if not exists java_gobang;

use java_gobang;

drop table if exists userinfo;

create table userinfo(
    id int primary key auto_increment,
    username varchar(50) not null unique,
    password varchar(65) not null,
    score int not null default 1000,
    total_count int not null default 0,
    win_count int not null default 0,
    create_time decimal not null default now(),
    update_time decimal not null default now()
);

insert into userinfo(username, password) values ('张三', '123');
insert into userinfo(username, password) values ('李四', '123');
insert into userinfo(username, password) values ('王五', '123');