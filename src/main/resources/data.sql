-- 카테고리 지정
INSERT INTO category (name, color, description, image_url) VALUES
('음료', '#FFFFFF', '음료 카테고리', 'https://example.com/image.jpg');

INSERT INTO products (name, price, image_url) VALUES ('아이스 카페 아메리카노 T', 4500, 'https://st.kakaocdn.net/product/gift/product/20231010111814_9a667f9eccc943648797925498bdd8a3.jpg');
INSERT INTO products (name, price, image_url) VALUES ('카페 라떼', 5800, 'http://image2.jpg');
INSERT INTO products (name, price, image_url) VALUES ('복숭아아이스티', 3000, 'http://image3.jpg');

-- 아이스 카페 아메리카노 T 옵션 추가
INSERT INTO options (name, quantity, product_id) VALUES ('얼음 적게', 100, 1);
INSERT INTO options (name, quantity, product_id) VALUES ('얼음 보통', 100, 1);
INSERT INTO options (name, quantity, product_id) VALUES ('얼음 많이', 100, 1);

-- 카페 라떼 옵션 추가
INSERT INTO options (name, quantity, product_id) VALUES ('라떼 원두 선택', 100, 2);
INSERT INTO options (name, quantity, product_id) VALUES ('라떼 우유 선택', 100, 2);

-- 복숭아아이스티 옵션 추가
INSERT INTO options (name, quantity, product_id) VALUES ('시럽 적게', 100, 3);
INSERT INTO options (name, quantity, product_id) VALUES ('시럽 보통', 100, 3);
INSERT INTO options (name, quantity, product_id) VALUES ('시럽 많이', 100, 3);