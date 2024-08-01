-- 사용자 데이터 삽입
INSERT INTO users (role, name, email, password, profile_image_url, kakao_id, login_type)
VALUES ('USER', '테스트유저', 'test@test.com', '$2a$10$SGtj9v1PCwZNmTGxwAHi3.YwwHW1xjeHLp13FEUfMAnaA8a/jJsdy', NULL, NULL,
        'NORMAL');

-- 카테고리 데이터 삽입
INSERT INTO category (name, description, image_url, color)
VALUES ('도서', '다양한 책과 문학 작품', 'https://newsimg.hankookilbo.com/2020/04/21/202004211422083541_3.jpg', '#0000ff');

INSERT INTO category (name, description, image_url, color)
VALUES ('전자 제품', '최신 전자 제품 및 가전 기기',
        'https://cphoto.asiae.co.kr/listimglink/1/2014082807172651648_1.jpg',
        '#ff5733');

-- 상품 데이터 삽입
INSERT INTO product (name, price, image_url, category_id)
VALUES ('참을 수 없는 존재의 가벼움', 15300, 'https://contents.kyobobook.co.kr/sih/fit-in/400x0/pdt/9788937437564.jpg',
        (SELECT category_id FROM category WHERE name = '도서'));

INSERT INTO product (name, price, image_url, category_id)
VALUES ('육각형 개발자', 19800, 'https://contents.kyobobook.co.kr/sih/fit-in/458x0/pdt/9791169211239.jpg',
        (SELECT category_id FROM category WHERE name = '도서'));

INSERT INTO product (name, price, image_url, category_id)
VALUES ('드라이브', 13500, 'https://contents.kyobobook.co.kr/sih/fit-in/458x0/pdt/9788935208951.jpg',
        (SELECT category_id FROM category WHERE name = '도서'));

INSERT INTO product (name, price, image_url, category_id)
VALUES ('함께 자라기', 11700, 'https://contents.kyobobook.co.kr/sih/fit-in/458x0/pdt/9788966262335.jpg',
        (SELECT category_id FROM category WHERE name = '도서'));

INSERT INTO product (name, price, image_url, category_id)
VALUES ('Apple 정품 아이폰 15 Pro', 1555700,
        'https://thumbnail10.coupangcdn.com/thumbnails/remote/230x230ex/image/retail/images/4434414951782396-0046b204-9f46-48d4-9483-c50be7ba1cbf.jpg',
        (SELECT category_id FROM category WHERE name = '전자 제품'));

INSERT INTO product (name, price, image_url, category_id)
VALUES ('Apple 2023 맥북 프로 14 M3', 2570190,
        'https://thumbnail9.coupangcdn.com/thumbnails/remote/230x230ex/image/retail/images/2119768668797193-b109eb1a-fd30-4420-850f-04faf0aea2f9.jpg',
        (SELECT category_id FROM category WHERE name = '전자 제품'));

-- 상품 옵션 데이터 삽입
INSERT INTO product_option (name, product_id, quantity)
VALUES ('하드커버', (SELECT product_id FROM product WHERE name = '참을 수 없는 존재의 가벼움'), 50);

INSERT INTO product_option (name, product_id, quantity)
VALUES ('페이퍼백', (SELECT product_id FROM product WHERE name = '참을 수 없는 존재의 가벼움'), 100);

INSERT INTO product_option (name, product_id, quantity)
VALUES ('양장본', (SELECT product_id FROM product WHERE name = '육각형 개발자'), 30);

INSERT INTO product_option (name, product_id, quantity)
VALUES ('소프트커버', (SELECT product_id FROM product WHERE name = '육각형 개발자'), 70);

INSERT INTO product_option (name, product_id, quantity)
VALUES ('일반판', (SELECT product_id FROM product WHERE name = '드라이브'), 60);

INSERT INTO product_option (name, product_id, quantity)
VALUES ('특별판', (SELECT product_id FROM product WHERE name = '드라이브'), 20);

INSERT INTO product_option (name, product_id, quantity)
VALUES ('일반판', (SELECT product_id FROM product WHERE name = '함께 자라기'), 60);

INSERT INTO product_option (name, product_id, quantity)
VALUES ('블랙', (SELECT product_id FROM product WHERE name = 'Apple 정품 아이폰 15 Pro'), 100);

INSERT INTO product_option (name, product_id, quantity)
VALUES ('화이트', (SELECT product_id FROM product WHERE name = 'Apple 정품 아이폰 15 Pro'), 50);

INSERT INTO product_option (name, product_id, quantity)
VALUES ('까리한 블랙', (SELECT product_id FROM product WHERE name = 'Apple 2023 맥북 프로 14 M3'), 30);

INSERT INTO product_option (name, product_id, quantity)
VALUES ('세련된 실버', (SELECT product_id FROM product WHERE name = 'Apple 2023 맥북 프로 14 M3'), 20);
