INSERT INTO category(name, color, image_url, description) VALUES ('생일', '#5949a3', 'https://img1.daumcdn.net/thumb/S104x104/?fname=https%3A%2F%2Ft1.daumcdn.net%2Fgift%2Fhome%2Ftheme%2F292020231106_MXMUB.png', '감동을 높여줄 생일 선물 리스트');
INSERT INTO category(name, color, image_url, description) VALUES ('교환권', '#9290C3', 'https://img1.daumcdn.net/thumb/S104x104/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20240131153049_5a22b137a8d346e9beb020a7a7f4254a.jpg', '놓치면 후회할 교환권 특가');
INSERT INTO category(name, color, image_url, description) VALUES ('결혼/집들이', '#95785d', 'https://img1.daumcdn.net/thumb/S104x104/?fname=https%3A%2F%2Ft1.daumcdn.net%2Fgift%2Fhome%2Ftheme%2F314220231231_QTOXR.png', '들려오는 지인들의 좋은 소식 💌');
INSERT INTO category(name, color, image_url, description) VALUES ('출산/키즈', '#fc8197', 'https://img1.daumcdn.net/thumb/S104x104/?fname=https%3A%2F%2Ft1.daumcdn.net%2Fgift%2Fhome%2Ftheme%2F292620240221_MLFJR.jpeg', '엄마와 아이를 위한 세심한 선물👼🏻');

INSERT INTO product(name, price, image_url, category_id) VALUES ('[단독각인] 피렌체 1221 에디션 오드코롱 50ml (13종 택1)', 145000, 'https://st.kakaocdn.net/product/gift/product/20240215083306_8e1db057580145829542463a84971ae3.png', 1);
INSERT INTO product(name, price, image_url, category_id) VALUES ('외식 통합권 10만원권', 100000, 'https://st.kakaocdn.net/product/gift/product/20200513102805_4867c1e4a7ae43b5825e9ae14e2830e3.png', 2);
INSERT INTO product(name, price, image_url, category_id) VALUES ('[정관장] 홍삼정 에브리타임 리미티드 (10ml x 30포)', 133000, 'https://st.kakaocdn.net/product/gift/product/20240118135914_a6e1a7442ea04aa49add5e02ed62b4c3.jpg', 3);
INSERT INTO product(name, price, image_url, category_id) VALUES ('[선물포장/미니퍼퓸증정] 디켄터 리드 디퓨저 300ml + 메세지카드', 120000, 'https://st.kakaocdn.net/product/gift/product/20240215112140_11f857e972bc4de6ac1d2f1af47ce182.jpg', 1);
INSERT INTO product(name, price, image_url, category_id) VALUES ('[선물포장] 소바쥬 오 드 뚜왈렛 60ML', 150000, 'https://st.kakaocdn.net/product/gift/product/20240214150740_ad25267defa64912a7c030a7b57dc090.jpg', 1);
INSERT INTO product(name, price, image_url, category_id) VALUES ('장난감', 100000, 'https://st.kakaocdn.net/product/gift/product/20200513102805_4867c1e4a7ae43b5825e9ae14e2830e3.png', 4);

INSERT INTO option(name, quantity, product_id) VALUES ('Option A', 10, 1);
INSERT INTO option(name, quantity, product_id) VALUES ('Option B', 120, 1);
INSERT INTO option(name, quantity, product_id) VALUES ('Option C', 200, 2);
INSERT INTO option(name, quantity, product_id) VALUES ('Option D', 4, 2);
INSERT INTO option(name, quantity, product_id) VALUES ('Option E', 500, 3);
INSERT INTO option(name, quantity, product_id) VALUES ('Option F', 1500, 4);
INSERT INTO option(name, quantity, product_id) VALUES ('Option G', 30, 5);
INSERT INTO option(name, quantity, product_id) VALUES ('Option H', 20, 6);
