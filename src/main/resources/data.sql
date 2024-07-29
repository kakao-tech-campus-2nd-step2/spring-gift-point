INSERT INTO CATEGORY(name, color, image_url, description) VALUES ('교환권', '#6c95d1', 'https://gift-s.kakaocdn.net/dn/gift/images/m640/dimm_theme.png', '');
INSERT INTO CATEGORY(name, color, image_url, description) VALUES ('상품권', '#8fcc5d', 'https://any-image.net/giftcard.png', '');
INSERT INTO CATEGORY(name, color, image_url, description) VALUES ('뷰티', '#e09edc', 'https://any-image.net/beauty.png', '');
INSERT INTO CATEGORY(name, color, image_url, description) VALUES ('패션', '#304b99', 'https://any-image.net/fashion.png', '');
INSERT INTO CATEGORY(name, color, image_url, description) VALUES ('식품', '#42bf3c', 'https://any-image.net/food.png', '');

INSERT INTO PRODUCT(category_id, name, price, image_url) VALUES (1, '아이스 카페 아메리카노 T', 4500, 'https://st.kakaocdn.net/product/gift/product/20231010111814_9a667f9eccc943648797925498bdd8a3.jpg');
INSERT INTO PRODUCT(category_id, name, price, image_url) VALUES (1, '아이스 카페 라떼 T', 5000, 'https://some/cafe/latte/image');
INSERT INTO PRODUCT(category_id, name, price, image_url) VALUES (3, '베스트 핸드크림 & 립 세트', 30000, 'https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20240711094324_87cc0c06c2dd4b748928f23a63488591.jpg');

INSERT INTO OPTIONS(product_id, name, quantity) VALUES (3, '01. [Best] 시어버터 핸드 & 시어 스틱 립 밤', 969);
INSERT INTO OPTIONS(product_id, name, quantity) VALUES (3, '02. 시어버터 핸드 & 시어 립 밤', 300);
INSERT INTO OPTIONS(product_id, name, quantity) VALUES (3, '03. 체리 핸드 & 체리 립 밤', 200);

INSERT INTO MEMBER(email, password, role) VALUES ('kakao@kakao.com', 'helloKakao12', 'ADMIN');
INSERT INTO MEMBER(email, password, role) VALUES ('test@test.com', 'Tester789', 'USER');
INSERT INTO MEMBER(email, password, role) VALUES ('hws2008@naver.com', 'testKakao123', 'ADMIN');

INSERT INTO WISH(member_id, product_id, quantity) VALUES (1, 1, 3);