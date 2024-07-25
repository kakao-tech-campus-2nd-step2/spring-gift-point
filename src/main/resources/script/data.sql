INSERT INTO app_user (email, password, role, registration_date, modification_date)
VALUES ('yoo@example.com', '1234', 'ADMIN', NOW(), NOW()),
       ('kyeong@example.com', '1234', 'USER', NOW(), NOW()),
       ('miiii@example.com', '1234', 'USER', NOW(), NOW());

INSERT INTO category (name, description, registration_date, modification_date)
VALUES ('기타', '기타 카테고리 입니다', NOW(), NOW()),
       ('교환권', '교환권 카테고리 입니다', NOW(), NOW()),
       ('카페', '카페 카테고리 입니다', NOW(), NOW());


INSERT INTO product (name, price, image_url, user_id, category_id, registration_date, modification_date)
VALUES ('상품 1', 100000, 'http://example.com/image1.jpg', 1, 1, NOW(), NOW()),
       ('상품 2', 20000, 'http://example.com/image2.jpg', 1, 2, NOW(), NOW()),
       ('상품 3', 1500, NULL, 1, 1, NOW(), NOW());

INSERT INTO wish (user_id, product_id, quantity, registration_date, modification_date)
VALUES (1, 1, 2, NOW(), NOW()),
       (1, 2, 1, NOW(), NOW()),
       (2, 3, 3, NOW(), NOW()),
       (2, 1, 1, NOW(), NOW()),
       (3, 1, 1, NOW(), NOW());
