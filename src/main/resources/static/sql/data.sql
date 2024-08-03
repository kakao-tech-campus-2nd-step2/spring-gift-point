-- 더미 데이터를 삽입할 sql파일

insert into categories (category_id, name, image_url)
values (default, '식료품', 'food.png');
insert into categories (category_id, name, image_url)
values (default, '의류', 'clothes.png');
insert into categories (category_id, name, image_url)
values (default, '가전', 'gajun.png');

insert into options (option_id, name, quantity, product_id)
values (default, 'dummy1_option', 10000, 1);
insert into options (option_id, name, quantity, product_id)
values (default, 'dummy2_option', 1000, 2);
insert into options (option_id, name, quantity, product_id)
values (default, 'dummy3_option', 100, 3);

-- 예외적으로 option이 없는 더미 데이터
insert into products (product_id, name, price, image_url, category_id)
values (default, 'dummy1', 10000, 'dummy1.png', 1);
insert into products (product_id, name, price, image_url, category_id)
values (default, 'dummy2', 20000, 'dummy2.png', 2);
insert into products (product_id, name, price, image_url, category_id)
values (default, 'dummy3', 30000, 'dummy3.png', 1);