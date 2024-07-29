-- category data for test
INSERT INTO category VALUES (901, '교환권');
INSERT INTO category VALUES (902, '상품권');
INSERT INTO category VALUES (903, '뷰티');
INSERT INTO category VALUES (904, '패션');
INSERT INTO category VALUES (905, '식품');
INSERT INTO category VALUES (906, '리빙/도서');
INSERT INTO category VALUES (907, '레저/스포츠');
INSERT INTO category VALUES (908, '아티스트/캐릭터');

-- product data for test
INSERT INTO product (id, name, price, image_url, category_id) VALUES (1, '아이스 카페 아메리카노 T', 4500, 'https://st.kakaocdn.net/product/gift/product/20231010111814_9a667f9eccc943648797925498bdd8a3.jpg', 901);
INSERT INTO product (id, name, price, image_url, category_id) VALUES (2, '아이스 카페 아메리카노 G', 4500, 'https://st.kakaocdn.net/product/gift/product/20231010111814_9a667f9eccc943648797925498bdd8a3.jpg', 901);

-- option data for test
INSERT INTO option (id, name, quantity, product_id) VALUES (1, 'option1', 100, 1);
INSERT INTO option (id, name, quantity, product_id) VALUES (2, 'option1', 100, 2);
