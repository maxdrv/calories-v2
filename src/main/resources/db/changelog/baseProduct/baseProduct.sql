--liquibase formatted sql

--changeset maxdrv:create_base_product_table

create table if not exists base_product
(
    id       bigint primary key,
    name     text             not null,
    kcal     double precision not null,
    proteins double precision not null,
    fats     double precision not null,
    carbs    double precision not null
);

create sequence if not exists base_product_seq start 10000 increment 1;

alter table base_product
    add constraint kcal_is_zero_or_positive check ( proteins >= 0),
    add constraint proteins_is_zero_or_positive check ( proteins >= 0),
    add constraint fats_is_zero_or_positive check ( fats >= 0),
    add constraint carbs_is_zero_or_positive check ( carbs >= 0);
