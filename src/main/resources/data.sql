INSERT INTO category (name, color, image_url, description) VALUES ('교환권', '#6c95d1', 'https://gift-s.kakaocdn.net/dn/gift/images/m640/dimm_theme.png', '');
INSERT INTO category (name, color, image_url, description) VALUES ('상품권', '#d1a36c', 'https://gift-s.kakaocdn.net/dn/gift/images/m640/dimm_theme.png', '');
INSERT INTO category (name, color, image_url, description) VALUES ('뷰티', '#f4b400', 'https://gift-s.kakaocdn.net/dn/gift/images/m640/dimm_theme.png', '');
INSERT INTO category (name, color, image_url, description) VALUES ('패션', '#db4437', 'https://gift-s.kakaocdn.net/dn/gift/images/m640/dimm_theme.png', '');
INSERT INTO category (name, color, image_url, description) VALUES ('식품', '#0f9d58', 'https://gift-s.kakaocdn.net/dn/gift/images/m640/dimm_theme.png', '');
INSERT INTO category (name, color, image_url, description) VALUES ('리빙_도서', '#ab47bc', 'https://gift-s.kakaocdn.net/dn/gift/images/m640/dimm_theme.png', '');
INSERT INTO category (name, color, image_url, description) VALUES ('레저_스포츠', '#00acc1', 'https://gift-s.kakaocdn.net/dn/gift/images/m640/dimm_theme.png', '');
INSERT INTO category (name, color, image_url, description) VALUES ('아티스트_캐릭터', '#ff7043', 'https://gift-s.kakaocdn.net/dn/gift/images/m640/dimm_theme.png', '');
INSERT INTO category (name, color, image_url, description) VALUES ('유아동_반려', '#8d6e63', 'https://gift-s.kakaocdn.net/dn/gift/images/m640/dimm_theme.png', '');
INSERT INTO category (name, color, image_url, description) VALUES ('디지털_가전', '#78909c', 'https://gift-s.kakaocdn.net/dn/gift/images/m640/dimm_theme.png', '');
INSERT INTO category (name, color, image_url, description) VALUES ('카카오프렌즈', '#fb8c00', 'https://gift-s.kakaocdn.net/dn/gift/images/m640/dimm_theme.png', '');
INSERT INTO category (name, color, image_url, description) VALUES ('트렌드_선물', '#c2185b', 'https://gift-s.kakaocdn.net/dn/gift/images/m640/dimm_theme.png', '');
INSERT INTO category (name, color, image_url, description) VALUES ('백화점', '#7e57c2', 'https://gift-s.kakaocdn.net/dn/gift/images/m640/dimm_theme.png', '');

INSERT INTO product (name, price, description, image_url, category_id) VALUES ('Product1', 10.00, 'Description for Product1', 'http://example.com/product1.jpg', (SELECT id FROM category WHERE name = '교환권'));
INSERT INTO product (name, price, description, image_url, category_id) VALUES ('Product2', 20.00, 'Description for Product2', 'http://example.com/product2.jpg', (SELECT id FROM category WHERE name = '상품권'));

INSERT INTO option (name, quantity, product_id) VALUES
('Option1', 100, (SELECT id FROM product WHERE name = 'Product1')),
('Option2', 200, (SELECT id FROM product WHERE name = 'Product2'));
