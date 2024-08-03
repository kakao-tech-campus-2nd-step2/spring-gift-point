Insert Into category(name, color, image_url)
values ('교환권', '#000000', '');
Insert Into category(name, color, image_url)
values ('상품권', '#000000', '');
Insert Into category(name, color, image_url)
values ('뷰티', '#000000', '');
Insert Into category(name, color, image_url)
values ('패션', '#000000', '');
Insert Into category(name, color, image_url)
values ('식품', '#000000', '');

INSERT INTO users(email, name, password, role, point)
values ('admin@admin.com', 'admin',
        '{bcrypt}$2a$10$Kd3Tjp96Ox4L6PTNpJ2byuDKRRUdFnJl89a85O8mTFzMQkyTowKFS', 'ADMIN', 10000);

INSERT
INTO product(category_id, created_at, image_url, modified_at, modified_by, name, price, user_id)
VALUES (2, NOW(),
        'https://st.kakaocdn.net/product/gift/product/20230612161606_b6ca12ec22ed483691bbe3dbcd7a7b24.png',
        NOW(), 1, '5만원권', 50000, 1);
INSERT INTO option(name, product_id, quantity)
VALUES ('5만원권', 1, 1000);

INSERT
INTO product(category_id, created_at, image_url, modified_at, modified_by, name, price, user_id)
VALUES (2, NOW(),
        'https://st.kakaocdn.net/product/gift/product/20231228135641_7083a797d09f4aecbc2b0d1c603315d3.png',
        NOW(), 1, '기프트카드 10만원권', 100000, 1);
INSERT INTO option(name, product_id, quantity)
VALUES ('기프트카드 10만원권', 2, 1000);

INSERT
INTO product(category_id, created_at, image_url, modified_at, modified_by, name, price, user_id)
VALUES (2, NOW(),
        'https://st.kakaocdn.net/product/gift/product/20200513102805_4867c1e4a7ae43b5825e9ae14e2830e3.png',
        NOW(), 1, '외식 통합권 10만원권', 100000, 1);
INSERT INTO option(name, product_id, quantity)
VALUES ('외식 통합권 10만원권', 3, 1000);

INSERT
INTO product(category_id, created_at, image_url, modified_at, modified_by, name, price, user_id)
VALUES (1, NOW(),
        'https://st.kakaocdn.net/product/gift/product/20231010111814_9a667f9eccc943648797925498bdd8a3.jpg',
        NOW(), 1, '아이스 카페 아메리카노 T', 4500, 1);
INSERT INTO option(name, product_id, quantity)
VALUES ('아이스 카페 아메리카노 T', 4, 1000);

INSERT
INTO product(category_id, created_at, image_url, modified_at, modified_by, name, price, user_id)
VALUES (1, NOW(),
        'https://st.kakaocdn.net/product/gift/product/20240201161827_7e1676bce5ff497a86f67cc543891ee0.jpg',
        NOW(), 1, '스타벅스 더블 초콜릿 케이크', 30000, 1);
INSERT INTO option(name, product_id, quantity)
VALUES ('스타벅스 더블 초콜릿 케이크', 5, 1000);

INSERT
INTO product(category_id, created_at, image_url, modified_at, modified_by, name, price, user_id)
VALUES (1, NOW(),
        'https://st.kakaocdn.net/product/gift/product/20240108141614_5581ad00f1214ccc80d161d46abae021.jpg',
        NOW(), 1, '생딸기 마스카포네 생크림', 37000, 1);
INSERT INTO option(name, product_id, quantity)
VALUES ('생딸기 마스카포네 생크림', 6, 1000);

INSERT
INTO product(category_id, created_at, image_url, modified_at, modified_by, name, price, user_id)
VALUES (1, NOW(),
        'https://st.kakaocdn.net/product/gift/product/20240105174500_8239a8f780754322861599425cd924ac.png',
        NOW(), 1, '스타벅스 파베 생 초콜릿', 19900, 1);
INSERT INTO option(name, product_id, quantity)
VALUES ('스타벅스 파베 생 초콜릿', 7, 1000);

INSERT
INTO product(category_id, created_at, image_url, modified_at, modified_by, name, price, user_id)
VALUES (1, NOW(),
        'https://st.kakaocdn.net/product/gift/product/20231005100907_5e889796c644475785ab8e3d0a4c03d5.jpg',
        NOW(), 1, '카페라떼 (R) 2잔', 10000, 1);
INSERT INTO option(name, product_id, quantity)
VALUES ('카페라떼 (R) 2잔', 8, 1000);

INSERT
INTO product(category_id, created_at, image_url, modified_at, modified_by, name, price, user_id)
VALUES (1, NOW(),
        'https://st.kakaocdn.net/product/gift/product/20220228135836_41ef8019002b4e488abfb0c14030fa2e.jpg',
        NOW(), 1, '떠먹는 아이스박스', 6500, 1);
INSERT INTO option(name, product_id, quantity)
VALUES ('떠먹는 아이스박스', 9, 1000);

INSERT
INTO product(category_id, created_at, image_url, modified_at, modified_by, name, price, user_id)
VALUES (1, NOW(),
        'https://st.kakaocdn.net/product/gift/product/20240115150652_35d344a9a02d4ab49c405b0f9a0a9627.jpg',
        NOW(), 1, '딸기 퐁당 라떼 (R)', 6500, 1);
INSERT INTO option(name, product_id, quantity)
VALUES ('딸기 퐁당 라떼 (R)', 10, 1000);

INSERT
INTO product(category_id, created_at, image_url, modified_at, modified_by, name, price, user_id)
VALUES (1, NOW(),
        'https://st.kakaocdn.net/product/gift/product/20211217124737_5c87ae38718141e5bbdf66234a307dce.jpg',
        NOW(), 1, '애플망고치즈설빙', 13900, 1);
INSERT INTO option(name, product_id, quantity)
VALUES ('애플망고치즈설빙', 11, 1000);

INSERT
INTO product(category_id, created_at, image_url, modified_at, modified_by, name, price, user_id)
VALUES (1, NOW(),
        'https://st.kakaocdn.net/product/gift/product/20220222115915_1a158a760f0d4e6f8ec27db5a0a31a0f.jpg',
        NOW(), 1, '리얼 생크림 2호', 33000, 1);
INSERT INTO option(name, product_id, quantity)
VALUES ('리얼 생크림 2호', 12, 1000);

INSERT
INTO product(category_id, created_at, image_url, modified_at, modified_by, name, price, user_id)
VALUES (1, NOW(),
        'https://st.kakaocdn.net/product/gift/product/20240215161244_9eb1b790df4f4348b21958b80354cfd9.jpg',
        NOW(), 1, '핸드크림 50ML', 23000, 1);
INSERT INTO option(name, product_id, quantity)
VALUES ('핸드크림 50ML', 13, 1000);

INSERT
INTO product(category_id, created_at, image_url, modified_at, modified_by, name, price, user_id)
VALUES (1, NOW(),
        'https://st.kakaocdn.net/product/gift/product/20230419124409_6ed038d200dc4905bfd8e6474446e357.jpg',
        NOW(), 1, '뿌링클+콜라1.25L', 23500, 1);
INSERT INTO option(name, product_id, quantity)
VALUES ('뿌링클+콜라1.25L', 14, 1000);



