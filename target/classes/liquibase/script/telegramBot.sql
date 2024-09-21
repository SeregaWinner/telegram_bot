-- liquibase formatted sql
create table notification(
 id bigint primary key,
 chatId bigint  not null,
 textMessage character varying(255) not null ,
 timeDate timestamp not null
 );