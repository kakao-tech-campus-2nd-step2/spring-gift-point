INSERT INTO category (name, color, image_url, description) VALUES ('교환권', '#6c95d1', 'https://gift-s.kakaocdn.net/dn/gift/images/m640/dimm_theme.png', 'test');
INSERT INTO category (name, color, image_url, description) VALUES ('상품권', '#6c95d1', 'https://gift-s.kakaocdn.net/dn/gift/images/m640/dimm_theme.png', 'test');
INSERT INTO category (name, color, image_url, description) VALUES ('뷰티', '#6c95d1', 'https://gift-s.kakaocdn.net/dn/gift/images/m640/dimm_theme.png', 'test');
INSERT INTO category (name, color, image_url, description) VALUES ('패션', '#6c95d1', 'https://gift-s.kakaocdn.net/dn/gift/images/m640/dimm_theme.png', 'test');
INSERT INTO category (name, color, image_url, description) VALUES ('식품', '#6c95d1', 'https://gift-s.kakaocdn.net/dn/gift/images/m640/dimm_theme.png', 'test');
INSERT INTO category (name, color, image_url, description) VALUES ('리빙/도서', '#6c95d1', 'https://gift-s.kakaocdn.net/dn/gift/images/m640/dimm_theme.png', 'test');
INSERT INTO category (name, color, image_url, description) VALUES ('레저/스포츠', '#6c95d1', 'https://gift-s.kakaocdn.net/dn/gift/images/m640/dimm_theme.png', 'test');
INSERT INTO category (name, color, image_url, description) VALUES ('아티스트/캐릭터', '#6c95d1', 'https://gift-s.kakaocdn.net/dn/gift/images/m640/dimm_theme.png', 'test');
INSERT INTO category (name, color, image_url, description) VALUES ('유아동/반려', '#6c95d1', 'https://gift-s.kakaocdn.net/dn/gift/images/m640/dimm_theme.png', 'test');

INSERT INTO product (category_id, name, price, image_url) VALUES (1, '아이스 카페 아메리카노 T', 4500, 'https://image.istarbucks.co.kr/upload/store/skuimg/2021/04/[110563]_20210426095937947.jpg');
INSERT INTO product (category_id, name, price, image_url) VALUES (3, '아이스 카페 라떼 T', 5000, 'https://image.istarbucks.co.kr/upload/store/skuimg/2021/04/[110569]_20210415143036138.jpg');
INSERT INTO product (category_id, name, price, image_url) VALUES (5, '아이스 스타벅스 돌체 라떼 T', 5900, 'https://image.istarbucks.co.kr/upload/store/skuimg/2021/04/[128695]_20210426092032110.jpg');

INSERT INTO option (product_id, name, quantity, version) VALUES (1, '사과맛', 80, 0);
INSERT INTO option (product_id, name, quantity, version) VALUES (1, '포도맛', 10, 0);
INSERT INTO option (product_id, name, quantity, version) VALUES (1, '복숭아맛', 30, 0);