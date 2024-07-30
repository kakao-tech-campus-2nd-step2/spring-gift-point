-- 사용자 데이터 삽입
INSERT INTO users (role, name, email, password, profile_image_url, kakao_id, login_type)
VALUES ('USER', '테스트유저', 'test@test.com', 'test', NULL, NULL, 'NORMAL');

-- 카테고리 데이터 삽입
INSERT INTO category (name, description, image_url, color)
VALUES ('도서', '다양한 책과 문학 작품', 'http://example.com/books.jpg', '#0000ff');


-- 상품 데이터 삽입
INSERT INTO product (name, price, image_url, category_id)
VALUES ('참을 수 없는 존재의 가벼움', 15000, 'http://example.com/unbearable_lightness.jpg',
        (SELECT category_id FROM category WHERE name = '도서'));

INSERT INTO product (name, price, image_url, category_id)
VALUES ('육각형 개발자', 20000, 'http://example.com/hexagonal_developer.jpg',
        (SELECT category_id FROM category WHERE name = '도서'));

INSERT INTO product (name, price, image_url, category_id)
VALUES ('드라이브', 18000, 'http://example.com/drive.jpg',
        (SELECT category_id FROM category WHERE name = '도서'));

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
