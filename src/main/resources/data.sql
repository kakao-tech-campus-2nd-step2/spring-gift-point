INSERT INTO category (name) VALUES ('교환권');
INSERT INTO category (name) VALUES ('상품권');
INSERT INTO category (name) VALUES ('뷰티');
INSERT INTO category (name) VALUES ('패션');
INSERT INTO category (name) VALUES ('식품');
INSERT INTO category (name) VALUES ('리빙/도서');
INSERT INTO category (name) VALUES ('레저/스포츠');
INSERT INTO category (name) VALUES ('아티스트/캐릭터');
INSERT INTO category (name) VALUES ('유아동/반려');
INSERT INTO category (name) VALUES ('디지털/가전');
INSERT INTO category (name) VALUES ('카카오프렌즈');
INSERT INTO category (name) VALUES ('트렌드 선물');
INSERT INTO category (name) VALUES ('백화점');

INSERT INTO product (name, price, imageurl, category_id) VALUES ('신라면', 1500, 'https://image.nongshim.com/non/pro/1647822565539.jpg', (SELECT id FROM category WHERE name = '식품'));

INSERT INTO member (email, password) VALUES ('user1@example.com', 'password1');
INSERT INTO member (email, password) VALUES ('user2@example.com', 'password2');

INSERT INTO product_option (product_id, name, quantity) VALUES (1, '순한맛', 1500);
INSERT INTO product_option (product_id, name, quantity) VALUES (1, '매운맛', 1500);
INSERT INTO product_option (product_id, name, quantity) VALUES (1, '순한맛 큰 컵', 1700);
INSERT INTO product_option (product_id, name, quantity) VALUES (1, '매운맛 큰 컵', 1700);