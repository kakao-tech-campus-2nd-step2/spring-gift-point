INSERT INTO category (name, color, imageUrl, description)
VALUES ('교환권', '#6c95d1', 'https://gift-s.kakaocdn.net/dn/gift/images/m640/dimm_theme.png', '');
INSERT INTO category (name, color, imageUrl, description)
VALUES ('상품권', '#6c95d1', 'https://gift-s.kakaocdn.net/dn/gift/images/m640/dimm_theme.png', 'description for 상품권');

INSERT INTO product (name, price, imageUrl, category_id) VALUES ('아이스 아메리카노', 4500, 'https://st.kakaocdn.net/product/gift/product/20231010111814_9a667f9eccc943648797925498bdd8a3.jpg', 1);
INSERT INTO product (name, price, imageUrl, category_id) VALUES ('아이스 카라멜마키아또', 5500, 'https://image.istarbucks.co.kr/upload/store/skuimg/2021/04/[110582]_20210415142706078.jpg', 1);
INSERT INTO product (name, price, imageUrl, category_id) VALUES ('아이스 카푸치노', 5300, 'https://image.istarbucks.co.kr/upload/store/skuimg/2021/04/[110601]_20210415143400773.jpg', 1);

INSERT INTO member (email, password) VALUES ('user1@example.com', 'password1');
INSERT INTO member (email, password) VALUES ('user2@example.com', 'password2');

INSERT INTO wish (member_id, product_id) VALUES (1, 1);