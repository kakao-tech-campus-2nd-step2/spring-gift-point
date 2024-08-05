-- 유저 추가
INSERT INTO users (email, password, point, access_token)
VALUES ('testuser@example.com', 'password', 10000, 'demoToken');


-- 카테고리 더미 데이터 추가
INSERT INTO categories (name, color, image_url, description)
VALUES ('교환권', '#6c95d1',
        'https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20240319133340_790b6fb28fa24f1fbb3eee4471c0a7fb.jpg',
        '교환권 카테고리입니다.');
INSERT INTO categories (name, color, image_url, description)
VALUES ('상품권', '#6c95d2',
        'https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20231010105204_8632b94c327549c686f3f090415c5969.jpg',
        '상품권 카테고리입니다.');
INSERT INTO categories (name, color, image_url, description)
VALUES ('뷰티', '#6c95d3',
        'https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20240725132635_f56aa979b4dc4713a0e1c1a0500fc070.jpg',
        '뷰티 카테고리입니다.');
INSERT INTO categories (name, color, image_url, description)
VALUES ('패션', '#6c95d4',
        'https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20240411144027_97c9ee88f7394d37bb366e0b497cd389.jpg',
        '패션 카테고리입니다.');
INSERT INTO categories (name, color, image_url, description)
VALUES ('식품', '#6c95d5',
        'https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20240624170318_f8d1c7e02ac647529987fcd0119aa58c.jpg',
        '식품 카테고리입니다.');
INSERT INTO categories (name, color, image_url, description)
VALUES ('리빙/도서', '#6c95d6',
        'https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2FUZX9TjlN0px6OYDThf2kmQ%2F2g4C4BhCZ7UN8Qn6rX_buPJHY1aOY2lER1uplNGntfU',
        '리빙/도서 카테고리입니다.');
INSERT INTO categories (name, color, image_url, description)
VALUES ('레저/스포츠', '#6c95d7',
        'https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2FHwZkAUJyM1ORu7sAgpUaHw%2FtouTX4G77Oc25fp2oWLkh6isia8KfMC0PNPkzmhQp1E.jpg',
        '레저/스포츠 카테고리입니다.');
INSERT INTO categories (name, color, image_url, description)
VALUES ('아티스트/캐릭터', '#6c95d8',
        'https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20240213103359_7a873afe090d4643b96774da928b5c8b.jpg',
        '아티스트/캐릭터 카테고리입니다.');
INSERT INTO categories (name, color, image_url, description)
VALUES ('유아동/반려', '#6c95d9',
        'https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20220609170758_7d8278e5321845748cc71ae4cbd610f9.jpg',
        '유아동/반려 카테고리입니다.');
INSERT INTO categories (name, color, image_url, description)
VALUES ('디지털/가전', '#6c95da',
        'https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20211108115915_6984ea3826c74ac8adb948faa35fda75.jpg',
        '디지털/가전 카테고리입니다.');
INSERT INTO categories (name, color, image_url, description)
VALUES ('카카오프렌즈', '#6c95db',
        'https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20230901160136_ecae5aef552c4fb0a77aa08fe45272de.jpg',
        '카카오프렌즈 카테고리입니다.');
INSERT INTO categories (name, color, image_url, description)
VALUES ('트렌드 선물', '#6c95dc',
        'https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20230331164317_fcf6a6df886b47059a07e5cab1127f73.png',
        '트렌드 선물 카테고리입니다.');

-- 제품 더미 데이터 추가 (카테고리와 연관)
INSERT INTO products (name, price, image_url, category_id)
VALUES ('이마트 모바일 금액권', 50000,
        'https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20240319133340_790b6fb28fa24f1fbb3eee4471c0a7fb.jpg',
        1);
INSERT INTO products (name, price, image_url, category_id)
VALUES ('e카드 3만원 교환권', 30000,
        'https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20231010105204_8632b94c327549c686f3f090415c5969.jpg',
        2);
INSERT INTO products (name, price, image_url, category_id)
VALUES ('헤라 센슈얼 누드 밤,글로스', 36000,
        'https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20240722070553_82d2b070f7624e869d7d00cca721844d.jpg',
        3);
INSERT INTO products (name, price, image_url, category_id)
VALUES ('WOOALONG 볼캡', 23000,
        'https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20240411144027_97c9ee88f7394d37bb366e0b497cd389.jpg',
        4);
INSERT INTO products (name, price, image_url, category_id)
VALUES ('샤인머스켓 세트', 15000,
        'https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20240624170318_f8d1c7e02ac647529987fcd0119aa58c.jpg',
        5);
INSERT INTO products (name, price, image_url, category_id)
VALUES ('[도서] 어느하루', 12000,
        'https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2FUZX9TjlN0px6OYDThf2kmQ%2F2g4C4BhCZ7UN8Qn6rX_buPJHY1aOY2lER1uplNGntfU',
        6);
INSERT INTO products (name, price, image_url, category_id)
VALUES ('나이키 백', 20000,
        'https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2FHwZkAUJyM1ORu7sAgpUaHw%2FtouTX4G77Oc25fp2oWLkh6isia8KfMC0PNPkzmhQp1E.jpg',
        7);
INSERT INTO products (name, price, image_url, category_id)
VALUES ('잔망 루피 모찌', 8000,
        'https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20240213103359_7a873afe090d4643b96774da928b5c8b.jpg',
        8);
INSERT INTO products (name, price, image_url, category_id)
VALUES ('무지개 피아노', 9000,
        'https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20220609170758_7d8278e5321845748cc71ae4cbd610f9.jpg',
        9);
INSERT INTO products (name, price, image_url, category_id)
VALUES ('에어팟 프로', 210000,
        'https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20211108115915_6984ea3826c74ac8adb948faa35fda75.jpg',
        10);
INSERT INTO products (name, price, image_url, category_id)
VALUES ('춘식이 키링', 7000,
        'https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20230901160136_ecae5aef552c4fb0a77aa08fe45272de.jpg',
        11);
INSERT INTO products (name, price, image_url, category_id)
VALUES ('서촌 핫플패스 모바일권', 10000,
        'https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20230331164317_fcf6a6df886b47059a07e5cab1127f73.png',
        12);
INSERT INTO products (name, price, image_url, category_id)
VALUES ('텀블러 세트', 17000,
        'https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20240619170546_e1237aae130546b6bb542c3e8a0dbf8e',
        4);
INSERT INTO products (name, price, image_url, category_id)
VALUES ('복숭아 케이크 + 아메리카노', 15700,
        'https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20240624170139_de4e5324f9124d438e9b60a4ab9b19a8.jpg',
        1);
INSERT INTO products (name, price, image_url, category_id)
VALUES ('카카오페이 상품권', 30000,
        'https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20220218175702_d408d902f38d496790467e6999dd7ed5.png',
        2);

-- 옵션 더미 데이터 추가 (각 제품당 2개 옵션)

-- Product 1
INSERT INTO options (name, quantity, product_id)
VALUES ('모바일 상품권 50,000원', 969, 1);
INSERT INTO options (name, quantity, product_id)
VALUES ('실물 상품권 50,000원', 500, 1);

-- Product 2
INSERT INTO options (name, quantity, product_id)
VALUES ('모바일 상품권 30,000원', 969, 2);
INSERT INTO options (name, quantity, product_id)
VALUES ('실물 상품권 30,000원', 500, 2);

-- Product 3
INSERT INTO options (name, quantity, product_id)
VALUES ('(밤) 모닝베이지(카카오 단독)', 350, 3);
INSERT INTO options (name, quantity, product_id)
VALUES ('(글로스) 얼리라벤더(카카오 단독)', 350, 3);
INSERT INTO options (name, quantity, product_id)
VALUES ('(밤) 핑크브라운', 350, 3);
INSERT INTO options (name, quantity, product_id)
VALUES ('(글로스) 윔플라워', 350, 3);

-- Product 4
INSERT INTO options (name, quantity, product_id)
VALUES ('그린', 969, 4);
INSERT INTO options (name, quantity, product_id)
VALUES ('블루', 500, 4);
INSERT INTO options (name, quantity, product_id)
VALUES ('오렌지', 500, 4);
INSERT INTO options (name, quantity, product_id)
VALUES ('그레이', 500, 4);

-- Product 5
INSERT INTO options (name, quantity, product_id)
VALUES ('빅 사이즈 2송이', 969, 5);
INSERT INTO options (name, quantity, product_id)
VALUES ('스몰 사이즈 3송이', 500, 5);

-- Product 6
INSERT INTO options (name, quantity, product_id)
VALUES ('[한정판] 작가 친필 사인본', 969, 6);
INSERT INTO options (name, quantity, product_id)
VALUES ('기본판', 500, 6);

-- Product 7
INSERT INTO options (name, quantity, product_id)
VALUES ('[이벤트] 텀블러 추가', 969, 7);
INSERT INTO options (name, quantity, product_id)
VALUES ('[이벤트] 나이키 양말 추가', 500, 7);

-- Product 8
INSERT INTO options (name, quantity, product_id)
VALUES ('[Best] 딸기맛', 969, 8);
INSERT INTO options (name, quantity, product_id)
VALUES ('자몽맛', 500, 8);

-- Product 9
INSERT INTO options (name, quantity, product_id)
VALUES ('파란색 에디션', 969, 9);
INSERT INTO options (name, quantity, product_id)
VALUES ('분홍색 에디션', 500, 9);

-- Product 10
INSERT INTO options (name, quantity, product_id)
VALUES ('교환권으로 받기', 969, 10);
INSERT INTO options (name, quantity, product_id)
VALUES ('배송으로 받기', 500, 10);

-- Product 11
INSERT INTO options (name, quantity, product_id)
VALUES ('[이벤트] 춘식이 볼펜 추가', 969, 11);
INSERT INTO options (name, quantity, product_id)
VALUES ('[이벤트] 춘식이 수건 추가', 500, 11);

-- Product 12
INSERT INTO options (name, quantity, product_id)
VALUES ('모바일 교환권 10,000원', 969, 12);
INSERT INTO options (name, quantity, product_id)
VALUES ('실물 교환권 10,000원', 500, 12);

-- Product 13
INSERT INTO options (name, quantity, product_id)
VALUES ('퍼플 + 옐로우', 969, 13);
INSERT INTO options (name, quantity, product_id)
VALUES ('파스텔 핑크 + 옐로우', 500, 13);
INSERT INTO options (name, quantity, product_id)
VALUES ('파스텔 스카이 + 그린', 500, 13);

-- Product 14
INSERT INTO options (name, quantity, product_id)
VALUES ('딸기 케이크 + 아메리카노', 969, 14);
INSERT INTO options (name, quantity, product_id)
VALUES ('오레오 케이크 + 아메리카노', 500, 14);

-- Product 15
INSERT INTO options (name, quantity, product_id)
VALUES ('모바일 상품권 30,000원', 969, 15);
INSERT INTO options (name, quantity, product_id)
VALUES ('실물 상품권 30,000원', 500, 15);


INSERT INTO wishes (user_id, product_id, created_date)
VALUES (1, 1, CURRENT_TIMESTAMP);
INSERT INTO wishes (user_id, product_id, created_date)
VALUES (1, 2, CURRENT_TIMESTAMP);
INSERT INTO wishes (user_id, product_id, created_date)
VALUES (1, 3, CURRENT_TIMESTAMP);
INSERT INTO wishes (user_id, product_id, created_date)
VALUES (1, 4, CURRENT_TIMESTAMP);
INSERT INTO wishes (user_id, product_id, created_date)
VALUES (1, 5, CURRENT_TIMESTAMP);
INSERT INTO wishes (user_id, product_id, created_date)
VALUES (1, 6, CURRENT_TIMESTAMP);
INSERT INTO wishes (user_id, product_id, created_date)
VALUES (1, 7, CURRENT_TIMESTAMP);
INSERT INTO wishes (user_id, product_id, created_date)
VALUES (1, 8, CURRENT_TIMESTAMP);
INSERT INTO wishes (user_id, product_id, created_date)
VALUES (1, 9, CURRENT_TIMESTAMP);
INSERT INTO wishes (user_id, product_id, created_date)
VALUES (1, 10, CURRENT_TIMESTAMP);
INSERT INTO wishes (user_id, product_id, created_date)
VALUES (1, 11, CURRENT_TIMESTAMP);
INSERT INTO wishes (user_id, product_id, created_date)
VALUES (1, 12, CURRENT_TIMESTAMP);
INSERT INTO wishes (user_id, product_id, created_date)
VALUES (1, 13, CURRENT_TIMESTAMP);
INSERT INTO wishes (user_id, product_id, created_date)
VALUES (1, 14, CURRENT_TIMESTAMP);
INSERT INTO wishes (user_id, product_id, created_date)
VALUES (1, 15, CURRENT_TIMESTAMP);
