-- 사용자 데이터 삽입
INSERT INTO users (role, name, email, password, profile_image_url, kakao_id, login_type)
VALUES ('USER', '테스트유저', 'test@test.com', 'test', NULL, NULL, 'NORMAL');

-- 카테고리 데이터 삽입
INSERT INTO category (name, description, image_url, color)
VALUES ('Electronics', 'Electronic devices and gadgets', 'http://example.com/electronics.jpg', '#ff0000');

INSERT INTO category (name, description, image_url, color)
VALUES ('Books', 'Books and literature', 'http://example.com/books.jpg', '#0000ff');

-- 상품 데이터 삽입
INSERT INTO product (name, price, image_url, category_id)
VALUES ('Smartphone', 699.99, 'http://example.com/smartphone.jpg',
        (SELECT category_id FROM category WHERE name = 'Electronics'));

INSERT INTO product (name, price, image_url, category_id)
VALUES ('Laptop', 999.99, 'http://example.com/laptop.jpg',
        (SELECT category_id FROM category WHERE name = 'Electronics'));

INSERT INTO product (name, price, image_url, category_id)
VALUES ('Novel', 19.99, 'http://example.com/novel.jpg', (SELECT category_id FROM category WHERE name = 'Books'));

-- 상품 옵션 데이터 삽입
INSERT INTO product_option (name, product_id, quantity)
VALUES ('64GB', (SELECT product_id FROM product WHERE name = 'Smartphone'), 100);

INSERT INTO product_option (name, product_id, quantity)
VALUES ('128GB', (SELECT product_id FROM product WHERE name = 'Smartphone'), 50);

INSERT INTO product_option (name, product_id, quantity)
VALUES ('256GB', (SELECT product_id FROM product WHERE name = 'Smartphone'), 25);

INSERT INTO product_option (name, product_id, quantity)
VALUES ('16GB RAM', (SELECT product_id FROM product WHERE name = 'Laptop'), 30);

INSERT INTO product_option (name, product_id, quantity)
VALUES ('32GB RAM', (SELECT product_id FROM product WHERE name = 'Laptop'), 15);

INSERT INTO product_option (name, product_id, quantity)
VALUES ('Hardcover', (SELECT product_id FROM product WHERE name = 'Novel'), 50);

INSERT INTO product_option (name, product_id, quantity)
VALUES ('Paperback', (SELECT product_id FROM product WHERE name = 'Novel'), 100);
