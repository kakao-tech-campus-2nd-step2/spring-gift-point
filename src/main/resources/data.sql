INSERT INTO members (email, password)
VALUES ('admin@email.com', 'password');

INSERT INTO categories (name, color, img_url, description)
VALUES ('교환권', '#6c95d1', 'https://gift-s.kakaocdn.net/dn/gift/images/m640/dimm_theme.png', '');
INSERT INTO categories (name, color, img_url, description)
VALUES ('상품권', '#6c95d2', 'https://gift-s.kakaocdn.net/dn/gift/images/m641/dimm_theme.png', '');
INSERT INTO categories (name, color, img_url, description)
VALUES ('뷰티', '#6c95d3', 'https://gift-s.kakaocdn.net/dn/gift/images/m642/dimm_theme.png', '');
INSERT INTO categories (name, color, img_url, description)
VALUES ('패션', '#6c95d4', 'https://gift-s.kakaocdn.net/dn/gift/images/m643/dimm_theme.png', '');
INSERT INTO categories (name, color, img_url, description)
VALUES ('식품', '#6c95d5', 'https://gift-s.kakaocdn.net/dn/gift/images/m644/dimm_theme.png', '');
INSERT INTO categories (name, color, img_url, description)
VALUES ('리빙/도서', '#6c95d6', 'https://gift-s.kakaocdn.net/dn/gift/images/m645/dimm_theme.png', '');
INSERT INTO categories (name, color, img_url, description)
VALUES ('레저/스포츠', '#6c95d7', 'https://gift-s.kakaocdn.net/dn/gift/images/m646/dimm_theme.png', '');
INSERT INTO categories (name, color, img_url, description)
VALUES ('아티스트/캐릭터', '#6c95d8', 'https://gift-s.kakaocdn.net/dn/gift/images/m647/dimm_theme.png',
        '');
INSERT INTO categories (name, color, img_url, description)
VALUES ('유아동/반려', '#6c95d9', 'https://gift-s.kakaocdn.net/dn/gift/images/m648/dimm_theme.png', '');
INSERT INTO categories (name, color, img_url, description)
VALUES ('디지털/가전', '#6c95da', 'https://gift-s.kakaocdn.net/dn/gift/images/m649/dimm_theme.png', '');
INSERT INTO categories (name, color, img_url, description)
VALUES ('카카오프렌즈', '#6c95db', 'https://gift-s.kakaocdn.net/dn/gift/images/m650/dimm_theme.png', '');
INSERT INTO categories (name, color, img_url, description)
VALUES ('트렌드 선물', '#6c95dc', 'https://gift-s.kakaocdn.net/dn/gift/images/m651/dimm_theme.png', '');
INSERT INTO categories (name, color, img_url, description)
VALUES ('백화점', '#6c95dd', 'https://gift-s.kakaocdn.net/dn/gift/images/m652/dimm_theme.png', '');


INSERT INTO products (name, price, img_url, category_id)
VALUES ('테일러센츠 리필파우치', 28000,
        'https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20240624145743_8d1960bec2614754b6a64b05b60d3f2b.jpg',
        6);
INSERT INTO products (name, price, img_url, category_id)
VALUES ('e카드 3만원 교환권', 30000,
        'https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20231010105204_8632b94c327549c686f3f090415c5969.jpg',
        1);
INSERT INTO products (name, price, img_url, category_id)
VALUES ('발렌티노 고-스프레이', 42000,
        'https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20240621150643_90a1cae8892444578b424749b6652221.jpg',
        3);

INSERT INTO options (name, quantity, product_id)
VALUES ('가든브리즈', 969, 1);
INSERT INTO options (name, quantity, product_id)
VALUES ('가든파티', 200, 1);
INSERT INTO options (name, quantity, product_id)
VALUES ('e카드 3만원 교환권', 500, 2);
INSERT INTO options (name, quantity, product_id)
VALUES ('본 인 로마 핑크 [BEST]', 300, 3);
INSERT INTO options (name, quantity, product_id)
VALUES ('본 인 로마 코랄', 300, 3);
INSERT INTO options (name, quantity, product_id)
VALUES ('보체 비바 [선물하기 단독]', 300, 3);

