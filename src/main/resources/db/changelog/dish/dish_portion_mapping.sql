--liquibase formatted sql

--changeset maxdrv:create_dish_portion_mapping_table

create table if not exists dish_portion_mapping
(
    dish_id    bigint not null references dish(id),
    portion_id bigint not null references portion(id),
    primary key (dish_id, portion_id)
);