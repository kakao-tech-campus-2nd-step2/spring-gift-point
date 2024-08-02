insert into member(email, password, point, deleted)
values ('member@naver.com', 'password', 0, 0);
insert into member(email, password, point, deleted)
values ('admin@naver.com', 'password', 0, 0);

insert into category(name, description, color, image_url, deleted)
values ('디지털/가전', '가전설명', '#888888',
        'https://prs.ohou.se/apne2/any/uploads/productions/v1-262152097570816.jpg?w=256&h=256&c=c&q=50', 0);
insert into category(name, description, color, image_url, deleted)
values ('상품권', '상품권설명', '#123456', 'https://sitem.ssgcdn.com/01/88/00/item/1000010008801_i1_750.jpg', 0);
insert into category(name, description, color, image_url, deleted)
values ('뷰티', '뷰티설명', '#777777',
        'https://i0.wp.com/blog.opensurvey.co.kr/wp-content/uploads/2020/01/2020_beauty_blog.jpg?resize=700%2C350&ssl=1',
        0);
insert into category(name, description, color, image_url, deleted)
values ('식품', '식품설명', '#222222',
        'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS97vAEobij-ygYW3Zk4c_N5GJz9CxBb7fLNA&s', 0);

insert into product(name, price, image_url, category_id, deleted)
values ('Apple 정품 아이폰 15', 1700000,
        'https://lh5.googleusercontent.com/proxy/M33I-cZvIHdtsY_uyd5R-4KXJ8uZBBAgVw4bmZagF1T5krxkC6AHpxPUvU_02yDsRljgOHwa-cUTlhgYG_bSNJbbmnf6k9OOPRQyvPf5m4nD',
        1, 0);
insert into product(name, price, image_url, category_id, deleted)
values ('Apple 정품 2024 아이패드 에어 11 M2칩', 900000,
        'https://encrypted-tbn0.gstatic.com/shopping?q=tbn:ANd9GcThcspVP4EUYTEiUD0udG3dzUZDZOQH9eopFO7_7zZmIafSouktNeyQn8jzKwYTMxcQwaWN_iglo8LAus6DJTG_ogEaU_tHSOtNL3wiYJhYqisdTuMRT2o97h503C6gWd9BxV8_ow&usqp=CAc',
        1, 0);
insert into product(name, price, image_url, category_id, deleted)
values ('50000원 상품권', 50000,
        'https://lh5.googleusercontent.com/proxy/M33I-cZvIHdtsY_uyd5R-4KXJ8uZBBAgVw4bmZagF1T5krxkC6AHpxPUvU_02yDsRljgOHwa-cUTlhgYG_bSNJbbmnf6k9OOPRQyvPf5m4nD',
        2, 0);

insert into option(product_id, name, quantity, deleted)
values (3, '상품옵션', 10000, 0);
insert into option(product_id, name, quantity, deleted)
values (2, '상품옵션2', 100000, 0);
