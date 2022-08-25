--liquibase formatted sql

--changeset maxdrv:create_portion_table

create table if not exists portion
(
    id              bigint primary key,
    grams           int4   not null,
    base_product_id bigint not null,
    constraint portion_fk_base_product foreign key (base_product_id) REFERENCES base_product (id)
);

create sequence if not exists portion_seq start 10000 increment 1;

alter table portion
    add constraint grams_is_zero_or_positive check ( grams >= 0);
