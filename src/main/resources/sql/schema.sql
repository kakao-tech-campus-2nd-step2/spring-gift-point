DROP TABLE IF EXISTS product;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS wishlist;
DROP TABLE IF EXISTS category;
DROP TABLE IF EXISTS option;
DROP TABLE IF EXISTS orders;
CREATE TABLE category (
    id LONG AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL ,
    color VARCHAR(255),
    image_url VARCHAR(512),
    description VARCHAR(255)
);
CREATE TABLE product (
    id LONG AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    price INT,
    image_url VARCHAR(512),
    category_id LONG,
    FOREIGN KEY (category_id) REFERENCES category(id) ON DELETE CASCADE
);
CREATE TABLE users (
    id LONG AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    email VARCHAR(255) ,
    password VARCHAR(255),
    role VARCHAR(255)
);
CREATE TABLE wishlist (
    id LONG AUTO_INCREMENT PRIMARY KEY,
    user_id LONG NOT NULL,
    product_id LONG NOT NULL,
    quantity INT,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES product(id) ON DELETE CASCADE
);
CREATE TABLE option (
    id LONG AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) UNIQUE,
    quantity INT,
    product_id LONG,
    FOREIGN KEY (product_id) REFERENCES product(id) ON DELETE CASCADE
);
CREATE TABLE orders(
    id LONG AUTO_INCREMENT PRIMARY KEY,
    user_id LONG,
    option_id LONG,
    product_id LONG,
    quantity INT,
    message VARCHAR(255),
    order_date_time TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES product(id) ON DELETE CASCADE,
    FOREIGN KEY (option_id) REFERENCES option(id) ON DELETE CASCADE
);

-- Dummy Data
INSERT INTO category (color, description, image_url, name) VALUES
                                                              ('White', '교환권', 'https://gift-s.kakaocdn.net/dn/gift/images/m640/dimm_theme.png', '교환권'),
                                                              ('Green', '상품권', 'https://img1.kakaocdn.net/thumb/C305x305@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20220218175850_c234b015b60d4bf3bebadf8430f3f0c0.png', '상품권'),
                                                              ('Red', '뷰티', 'https://gift-s.kakaocdn.net/dn/gift/images/m640/dimm_theme.png', '뷰티');

INSERT INTO product (category_id, name, price, image_url) VALUES
                                                             (1, '아메리카노', 4500, 'https://www.google.com/imgres?q=%EC%95%84%EB%A9%94%EB%A6%AC%EC%B9%B4%EB%85%B8&imgurl=https%3A%2F%2Fimg.danawa.com%2Fprod_img%2F500000%2F609%2F014%2Fimg%2F3014609_1.jpg%3F1650002669098&imgrefurl=https%3A%2F%2Fprod.danawa.com%2Finfo%2F%3Fpcode%3D3014609&docid=TsU6yggMJqFboM&tbnid=LdD0kv-H8h3V2M&vet=12ahUKEwiU5ofjls-HAxWbaPUHHUF_CbIQM3oECFIQAA..i&w=500&h=500&hcb=2&ved=2ahUKEwiU5ofjls-HAxWbaPUHHUF_CbIQM3oECFIQAA'),
                                                             (1, '허니콤보 웨지감자 세트', 30,'https://img1.kakaocdn.net/thumb/C305x305@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20230323110332_8f48d7d12c2d4253945582fb8e7ecbdd.jpg'),
                                                             (2, '배민상품권 5만원 교환권', 50000, 'https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20230830171418_a6fe142e25f94ae5a01f8622b4b8bae9.png'),
                                                             (3, 'MAC 립스틱', 35000, 'https://img1.kakaocdn.net/thumb/C305x305@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20240725143149_9e945ef602b0439d8087a454e1472f85.jpg');

INSERT INTO option (name, quantity, product_id) VALUES
                                                    ('아이스 카페 아메리카노 T', 10, 1),
                                                    ('허니콤보 웨지감자세트', 10, 2),
                                                    ('배민상품권 5만원', 10, 3),
                                                    ('칠리 (웜 브릭 레드)', 10, 4),
                                                    ('멀 잇 투 더 맥스 (더티 피치)', 10, 4),
                                                    ('루비 우 (비비드 블루 레드)', 10, 4)
