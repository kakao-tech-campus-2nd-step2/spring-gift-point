-- 사용자 데이터 삽입
INSERT INTO users (role, name, email, password, profile_image_url, kakao_id, login_type)
VALUES ('USER', '테스트유저', 'test@test.com', '$2a$10$SGtj9v1PCwZNmTGxwAHi3.YwwHW1xjeHLp13FEUfMAnaA8a/jJsdy', NULL, NULL,
        'NORMAL');

-- 카테고리 데이터 삽입
INSERT INTO category (name, description, image_url, color)
VALUES ('선물', '선물하기 좋은 것들',
        'https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20240417111629_616eccb9d4cd464fa06d3430947dce15.jpg',
        '#0000ff');

INSERT INTO category (name, description, image_url, color)
VALUES ('스몰 럭셔리', '스몰 럭셔리 상품들',
        'https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20240722070659_be50b3e055754d4390058be02d116169.jpg',
        '#ff5733');

-- 상품 데이터 삽입
INSERT INTO product (name, price, image_url, category_id)
VALUES ('[단독]푸드장 프리미엄 구이 선물세트1.15kg(부채살+살치살+토시살+소목등심(척아이롤))', 49900,
        'https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20240129103642_a5ca62d182ec419285ba708b51cb72c2.jpg',
        (SELECT category_id FROM category WHERE name = '선물'));

INSERT INTO product (name, price, image_url, category_id)
VALUES ('[단독]하겐다즈 프리미엄 수제 아이스크림 케이크 리얼블랑 (바닐라+벨지안초코)', 29900,
        'https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20240417111629_616eccb9d4cd464fa06d3430947dce15.jpg',
        (SELECT category_id FROM category WHERE name = '선물'));

INSERT INTO product (name, price, image_url, category_id)
VALUES ('[센트룸] 맛있는 멀티비타민 미네랄 구미 (80구미)', 24000,
        'https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20240722113431_fd84a02c41514839a0afaa9ba040f003.jpg',
        (SELECT category_id FROM category WHERE name = '선물'));


INSERT INTO product (name, price, image_url, category_id)
VALUES ('[생일/선물포장] ''휩드 선물 1위'' 비건 팩클렌저 디스커버리 키트(4종) + 팝업 카드 + 수플레 크림 2종 샘플 증정', 25700,
        'https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20240725090331_257e7b0d12b441cd8dc41d49e0fb27dc.jpg',
        (SELECT category_id FROM category WHERE name = '스몰 럭셔리'));

INSERT INTO product (name, price, image_url, category_id)
VALUES ('[한정수량/각인/선물포장] 헤라 센슈얼 누드 밤 무디,할라피뇨 중 택 1(+블랙 손거울 증정)', 36000,
        'https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20240725132635_f56aa979b4dc4713a0e1c1a0500fc070.jpg',
        (SELECT category_id FROM category WHERE name = '스몰 럭셔리'));

-- 상품 옵션 데이터 삽입
INSERT INTO product_option (name, product_id, quantity)
VALUES ('[단독]푸드장 프리미엄 구이 선물세트1.15kg(부채살+살치살+토시살+소목등심(척아이롤))',
        (SELECT product_id FROM product WHERE name = '[단독]푸드장 프리미엄 구이 선물세트1.15kg(부채살+살치살+토시살+소목등심(척아이롤))'), 50);

INSERT INTO product_option (name, product_id, quantity)
VALUES ('[단독]하겐다즈 프리미엄 수제 아이스크림 케이크 리얼블랑 (바닐라+벨지안초코)',
        (SELECT product_id FROM product WHERE name = '[단독]하겐다즈 프리미엄 수제 아이스크림 케이크 리얼블랑 (바닐라+벨지안초코)'), 30);


INSERT INTO product_option (name, product_id, quantity)
VALUES ('[센트룸] 맛있는 멀티비타민 미네랄 구미 (80구미)', (SELECT product_id FROM product WHERE name = '[센트룸] 맛있는 멀티비타민 미네랄 구미 (80구미)'),
        60);


INSERT INTO product_option (name, product_id, quantity)
VALUES ('[생일/선물포장] ''휩드 선물 1위'' 비건 팩클렌저 디스커버리 키트(4종) + 팝업 카드 + 수플레 크림 2종 샘플 증정', (SELECT product_id
                                                                                  FROM product
                                                                                  WHERE name =
                                                                                        '[생일/선물포장] ''휩드 선물 1위'' 비건 팩클렌저 디스커버리 키트(4종) + 팝업 카드 + 수플레 크림 2종 샘플 증정'),
        100);

INSERT INTO product_option (name, product_id, quantity)
VALUES ('무디', (SELECT product_id FROM product WHERE name = '[한정수량/각인/선물포장] 헤라 센슈얼 누드 밤 무디,할라피뇨 중 택 1(+블랙 손거울 증정)'),
        0);
VALUES ('할라피뇨', (SELECT product_id FROM product WHERE name = '[한정수량/각인/선물포장] 헤라 센슈얼 누드 밤 무디,할라피뇨 중 택 1(+블랙 손거울 증정)'),
        30);


INSERT INTO user_points (user_id, point)
VALUES (1, 1000);
