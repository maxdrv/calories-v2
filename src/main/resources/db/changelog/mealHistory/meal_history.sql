--liquibase formatted sql

--changeset maxdrv:create_meal_history_table

create table if not exists meal_history
(
    id          bigint primary key,
    created_at  timestamp with time zone not null,
    updated_at  timestamp with time zone not null,
    dish_id     bigint not null,
    consumed_at timestamp with time zone not null,
    constraint meal_history_fk_dish foreign key (dish_id) REFERENCES dish (id)
);

create sequence if not exists meal_history_seq start 10000 increment 1;