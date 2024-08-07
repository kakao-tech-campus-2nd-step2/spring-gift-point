-- category data for test
INSERT INTO category (id, name, description, color, image_url) VALUES (901, '교환권', 'description', '#FFFFFF', 'imageUrl');
INSERT INTO category (id, name, description, color, image_url) VALUES (902, '상품권', 'description', '#FFFFFF', 'imageUrl');
INSERT INTO category (id, name, description, color, image_url) VALUES (903, '뷰티', 'description', '#FFFFFF', 'imageUrl');
INSERT INTO category (id, name, description, color, image_url) VALUES (904, '패션', 'description', '#FFFFFF', 'imageUrl');
INSERT INTO category (id, name, description, color, image_url) VALUES (905, '식품', 'description', '#FFFFFF', 'imageUrl');
INSERT INTO category (id, name, description, color, image_url) VALUES (906, '리빙/도서', 'description', '#FFFFFF', 'imageUrl');
INSERT INTO category (id, name, description, color, image_url) VALUES (907, '레저/스포츠', 'description', '#FFFFFF', 'imageUrl');
INSERT INTO category (id, name, description, color, image_url) VALUES (908, '아티스트/캐릭터', 'description', '#FFFFFF', 'imageUrl');

-- product data for test
INSERT INTO product (id, name, price, image_url, category_id) VALUES (1000, '아이스 카페 아메리카노 T', 4500, 'https://st.kakaocdn.net/product/gift/product/20231010111814_9a667f9eccc943648797925498bdd8a3.jpg', 901);
INSERT INTO product (id, name, price, image_url, category_id) VALUES (2000, '아이스 카페 아메리카노 G', 4500, 'https://st.kakaocdn.net/product/gift/product/20231010111814_9a667f9eccc943648797925498bdd8a3.jpg', 901);

-- option data for test
INSERT INTO option (id, name, quantity, product_id) VALUES (1, 'option1', 100, 1000);
INSERT INTO option (id, name, quantity, product_id) VALUES (2, 'option2', 100, 1000);
INSERT INTO option (id, name, quantity, product_id) VALUES (3, 'option3', 100, 1000);

INSERT INTO option (id, name, quantity, product_id) VALUES (4, 'option4', 100, 2000);

-- member data for test
INSERT INTO member (id, email, password, kakao_access_token, kakao_refresh_token, login_type) VALUES (1, 'member@kakao.com', 'asdf', null, null, 1)