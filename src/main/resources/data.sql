-- Insert Category '도서, 상품권, 의류, 식품, 디지털/가전'
INSERT INTO category (name, color, image_url, description) VALUES
('도서', '#FFFF00', 'https://media.istockphoto.com/id/157482029/ko/%EC%82%AC%EC%A7%84/%EC%8C%93%EB%8B%A4-of-%EB%8F%84%EC%84%9C%EB%8A%94.jpg?s=612x612&w=0&k=20&c=9n6HNuIq2jKwAXjiATjBa_U3_TLXj_kPZUQEabxcgRQ=', '읽을 수 있습니다.'),
('상품권', '#FF11FF', 'https://image.utoimage.com/preview/cp880403/2018/12/201812005319_500.jpg', '교환할 수 있습니다.'),
('의류', '#FF11FF', 'https://twicpics.celine.com/product-prd/images/large/2A73X270R.07BL_1_SPR23.jpg?twic=v1/cover=1:1/resize-max=720', '입을 수 있는 옷'),
('식품', '#FFFFFF', 'https://img.freepik.com/free-photo/real-food-pyramid-assortment-top-view_23-2150239079.jpg?size=626&ext=jpg&ga=GA1.1.2008272138.1722124800&semt=ais_user', '먹을 수 있는 식품'),
('디지털/가전', '#FF00FF', 'https://media.istockphoto.com/id/1211554164/ko/%EC%82%AC%EC%A7%84/%EA%B0%80%EC%A0%84-%EC%A0%9C%ED%92%88-%EC%BB%AC%EB%A0%89%EC%85%98-%EC%84%B8%ED%8A%B8%EC%9D%98-3d-%EB%A0%8C%EB%8D%94%EB%A7%81.jpg?s=612x612&w=0&k=20&c=q3wB0D8QIJjp5LWEce2T38nY_7aCFTxzmjIwN7OQEmM=', '디지털과 가전에 대한 것');

-- Insert '신라면'
INSERT INTO product (name, price, image_url, category_id)
VALUES ('신라면', 1500, 'https://image.nongshim.com/non/pro/1647822565539.jpg', (SELECT id FROM category WHERE name = '식품'));

CREATE TEMPORARY TABLE temp_product_id AS
SELECT id FROM product WHERE name = '신라면';

INSERT INTO option (name, quantity, product_id) VALUES
('매운맛', 100, (SELECT id FROM temp_product_id)),
('순한맛', 200, (SELECT id FROM temp_product_id));

DROP TABLE temp_product_id;

-- Insert '바지'
INSERT INTO product (name, price, image_url, category_id)
VALUES ('바지', 20000, 'https://balenciaga.dam.kering.com/m/31f6cf9f5d1f8418/Large-773763TJW794076_F.jpg?v=1', (SELECT id FROM category WHERE name = '의류'));

CREATE TEMPORARY TABLE temp_product_id AS
SELECT id FROM product WHERE name = '바지';

INSERT INTO option (name, quantity, product_id) VALUES
('L', 50, (SELECT id FROM temp_product_id)),
('XL', 30, (SELECT id FROM temp_product_id));

DROP TABLE temp_product_id;

-- Insert '백화점상품권'
INSERT INTO product (name, price, image_url, category_id)
VALUES ('백화점상품권', 50000, 'https://hyticket.co.kr/wp-content/uploads/2018/12/%EC%8B%A0%EC%84%B8%EA%B3%845%EB%A7%8C%EC%9B%90.jpg', (SELECT id FROM category WHERE name = '상품권'));

CREATE TEMPORARY TABLE temp_product_id AS
SELECT id FROM product WHERE name = '백화점상품권';

INSERT INTO option (name, quantity, product_id) VALUES
('신세계', 10, (SELECT id FROM temp_product_id)),
('롯데', 5, (SELECT id FROM temp_product_id));

DROP TABLE temp_product_id;

-- Insert '책'
INSERT INTO product (name, price, image_url, category_id)
VALUES ('책', 10000, 'https://media.istockphoto.com/id/157482029/ko/%EC%82%AC%EC%A7%84/%EC%8C%93%EB%8B%A4-of-%EB%8F%84%EC%84%9C%EB%8A%94.jpg?s=612x612&w=0&k=20&c=9n6HNuIq2jKwAXjiATjBa_U3_TLXj_kPZUQEabxcgRQ=', (SELECT id FROM category WHERE name = '도서'));

CREATE TEMPORARY TABLE temp_product_id AS
SELECT id FROM product WHERE name = '책';

INSERT INTO option (name, quantity, product_id) VALUES
('어린왕자', 150, (SELECT id FROM temp_product_id)),
('테스', 500, (SELECT id FROM temp_product_id));

DROP TABLE temp_product_id;

-- Insert Local Users
INSERT INTO users (email, password, sns, points) VALUES
('example1@email.com', '11111', 'local', 0),
('example2@email.com', '22222', 'local', 0),
('example3@email.com', '33333', 'local', 0);

-- Insert SNS Users
INSERT INTO users (sns_id, sns, points) VALUES
('11111', 'kakao', 0),
('22222', 'kakao', 0),
('33333', 'kakao', 0);

-- Insert Wishlist Items
INSERT INTO wishlist (user_id, product_id, quantity, option_id) VALUES
((SELECT id FROM users WHERE email = 'example1@email.com'), (SELECT id FROM product WHERE name = '신라면'), 2, (SELECT id FROM option WHERE name = '매운맛')),
((SELECT id FROM users WHERE email = 'example1@email.com'), (SELECT id FROM product WHERE name = '책'), 2, (SELECT id FROM option WHERE name = '어린왕자')),
((SELECT id FROM users WHERE email = 'example2@email.com'), (SELECT id FROM product WHERE name = '바지'), 1, (SELECT id FROM option WHERE name = 'L')),
((SELECT id FROM users WHERE email = 'example2@email.com'), (SELECT id FROM product WHERE name = '책'), 2, (SELECT id FROM option WHERE name = '어린왕자')),
((SELECT id FROM users WHERE email = 'example3@email.com'), (SELECT id FROM product WHERE name = '백화점상품권'), 3, (SELECT id FROM option WHERE name = '신세계')),
((SELECT id FROM users WHERE email = 'example3@email.com'), (SELECT id FROM product WHERE name = '책'), 2, (SELECT id FROM option WHERE name = '어린왕자')),
((SELECT id FROM users WHERE sns_id = '11111'), (SELECT id FROM product WHERE name = '책'), 1, (SELECT id FROM option WHERE name = '어린왕자')),
((SELECT id FROM users WHERE sns_id = '11111'), (SELECT id FROM product WHERE name = '신라면'), 2, (SELECT id FROM option WHERE name = '매운맛'));
