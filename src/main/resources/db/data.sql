INSERT INTO member_table ( email, password) VALUES ('admin@email.com','password');

INSERT INTO product (price, name, image_url) VALUES (4500,'아이스 카페 아메리카노 T',  'https://st.kakaocdn.net/product/gift/product/20231010111814_9a667f9eccc943648797925498bdd8a3.jpg');

INSERT INTO wish (member_id, product_id) VALUES (1, 1);
INSERT INTO wish (member_id, product_id) VALUES (1, 2);

INSERT INTO wish (member_id, product_id) VALUES (2, 1);
INSERT INTO wish (member_id, product_id) VALUES (2, 2);

INSERT INTO category