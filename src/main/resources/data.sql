insert into member(name, email, password, role, deleted)
values ('멤버', 'member@naver.com', 'password', 'MEMBER', 0);
insert into member(name, email, password, role, deleted)
values ('관리자', 'admin@naver.com', 'password', 'ADMIN', 0);

insert into category(name, description, color, image_url, deleted)
values ('디지털/가전', '가전설명', '#888888', '가전이미지', 0);
insert into category(name, description, color, image_url, deleted)
values ('상품권', '상품권설명', '#123456', '상품권이미지', 0);
insert into category(name, description, color, image_url, deleted)
values ('뷰티', '뷰티설명', '#777777', '뷰티이미지', 0);
insert into category(name, description, color, image_url, deleted)
values ('식품', '식품설명', '#222222', '식품이미지', 0);

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
