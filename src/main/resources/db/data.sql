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
INSERT INTO product (id, name, price, image_url, category_id) VALUES (3, '아이스 카페 아메리카노 V', 4500, 'https://st.kakaocdn.net/product/gift/product/20231010111814_9a667f9eccc943648797925498bdd8a3.jpg', 901);
INSERT INTO product (id, name, price, image_url, category_id) VALUES (4, '아이스 카페 라테 T', 4500, 'https://st.kakaocdn.net/product/gift/product/20231010111814_9a667f9eccc943648797925498bdd8a3.jpg', 901);
INSERT INTO product (id, name, price, image_url, category_id) VALUES (5, '아이스 카페 라테 G', 4500, 'https://st.kakaocdn.net/product/gift/product/20231010111814_9a667f9eccc943648797925498bdd8a3.jpg', 901);
INSERT INTO product (id, name, price, image_url, category_id) VALUES (6, '아이스 카페 라테 V', 4500, 'https://st.kakaocdn.net/product/gift/product/20231010111814_9a667f9eccc943648797925498bdd8a3.jpg', 901);
INSERT INTO product (id, name, price, image_url, category_id) VALUES (7, '아이스 카푸치노 T', 4500, 'https://st.kakaocdn.net/product/gift/product/20231010111814_9a667f9eccc943648797925498bdd8a3.jpg', 901);
INSERT INTO product (id, name, price, image_url, category_id) VALUES (8, '아이스 카푸치노 G', 4500, 'https://st.kakaocdn.net/product/gift/product/20231010111814_9a667f9eccc943648797925498bdd8a3.jpg', 901);
INSERT INTO product (id, name, price, image_url, category_id) VALUES (9, '아이스 카푸치노 V', 4500, 'https://st.kakaocdn.net/product/gift/product/20231010111814_9a667f9eccc943648797925498bdd8a3.jpg', 901);
INSERT INTO product (id, name, price, image_url, category_id) VALUES (10, '자바칩 프라푸치노 T', 4500, 'https://st.kakaocdn.net/product/gift/product/20231010111814_9a667f9eccc943648797925498bdd8a3.jpg', 901);
INSERT INTO product (id, name, price, image_url, category_id) VALUES (11, '시그니처 초콜릿 T', 4500, 'https://st.kakaocdn.net/product/gift/product/20231010111814_9a667f9eccc943648797925498bdd8a3.jpg', 901);

-- member data for test
INSERT INTO member VALUES (1, 'member1.com', 'asdf');
INSERT INTO member VALUES (2, 'member2.com', 'asdf');

-- wish data for test
INSERT INTO wish VALUES (1, 1, 1);
INSERT INTO wish VALUES (2, 1, 2);
INSERT INTO wish VALUES (3, 2, 4);
INSERT INTO wish VALUES (4, 1, 4);
INSERT INTO wish VALUES (5, 1, 3);
INSERT INTO wish VALUES (6, 2, 1);
INSERT INTO wish VALUES (7, 2, 3);

-- option data for test
INSERT INTO option VALUES (1, 'option1', 100, 1);
INSERT INTO option VALUES (2, 'option2', 100, 1);
INSERT INTO option VALUES (3, 'option3', 100, 1);
