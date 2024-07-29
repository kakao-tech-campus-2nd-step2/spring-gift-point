insert into member (email, password) values ('testuser@gmail.com', 'test123');

-- 더미 데이터: Category 삽입
insert into category (name, image_url, color, description) values ('교환권', 'https://gift-s.kakaocdn.net/dn/gift/images/m640/dimm_theme.png', '#6c95d1', '교환권입니다.');
insert into category (name, image_url, color, description) values ('의류', 'https://gift-s.kakaocdn.net/dn/gift/images/m640/dimm_theme.png', '#6c95d1', '의류입니다.');

-- 더미 데이터: Product 삽입
INSERT INTO product (name, price, image_url, category_id) VALUES ('카카오 선물하기 교환권', 10000.0, 'http://example.com/image1.jpg', 1);
INSERT INTO product (name, price, image_url, category_id) VALUES ('빨간 티셔츠', 15000.0, 'http://example.com/image2.jpg', 2);

-- 더미 데이터: Option 삽입 (Product 1의 옵션)
INSERT INTO option (name, quantity, product_id) VALUES ('10000원권', 10, 1);
INSERT INTO option (name, quantity, product_id) VALUES ('20000원권', 20, 1);
INSERT INTO option (name, quantity, product_id) VALUES ('30000원권', 15, 1);

-- 더미 데이터: Option 삽입 (Product 2의 옵션)
INSERT INTO option (name, quantity, product_id) VALUES ('S', 5, 2);
INSERT INTO option (name, quantity, product_id) VALUES ('M', 8, 2);
INSERT INTO option (name, quantity, product_id) VALUES ('L', 12, 2);
