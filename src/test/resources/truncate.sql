truncate table dish_portion_mapping, dish, portion, base_product;

alter sequence if exists dish_seq restart 10000;
alter sequence if exists portion_seq restart 10000;
alter sequence if exists base_product_seq restart 10000;

