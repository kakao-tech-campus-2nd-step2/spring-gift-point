-- 데이터 삽입 전에 기존 데이터 삭제
DELETE FROM wish;
DELETE FROM orders;
DELETE FROM option;
DELETE FROM product;
DELETE FROM category;
DELETE FROM member;

-- Category 데이터 추가
INSERT INTO category (name, color, img_url, description)
VALUES ('교환권', '#6c95d1', 'https://category.png', 'detail');

-- Product 데이터 추가
INSERT INTO product (name, img_url, price, category_id)
VALUES ('Sample Product', 'https://product.png', 1000, (SELECT id FROM category WHERE name = '교환권'));

-- Option 데이터 추가
INSERT INTO option (name, quantity, product_id)
VALUES ('Sample Option', 10, (SELECT id FROM product WHERE name = 'Sample Product'));

-- Member 데이터 추가
INSERT INTO member (email, password)
VALUES ('test@example.com', 'password');

-- Wish 데이터 추가
INSERT INTO wish (member_id, product_id, option_id)
VALUES ((SELECT id FROM member WHERE email = 'test@example.com'),
        (SELECT id FROM product WHERE name = 'Sample Product'),
        (SELECT id FROM option WHERE name = 'Sample Option'));

-- Order 데이터 추가
INSERT INTO orders (option_id, quantity, message, order_date_time)
VALUES ((SELECT id FROM option WHERE name = 'Sample Option'), 2, 'Please handle this order with care.', CURRENT_TIMESTAMP);