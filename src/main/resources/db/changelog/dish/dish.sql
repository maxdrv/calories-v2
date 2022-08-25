--liquibase formatted sql

--changeset maxdrv:create_dish_table

create table if not exists dish
(
    id   bigint primary key,
    name text   not null
);

create sequence if not exists dish_seq start 10000 increment 1;