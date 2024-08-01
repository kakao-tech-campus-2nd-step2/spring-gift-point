INSERT INTO Category(name, color, image_url, description)
VALUES('교환권', '갈색', 'https...', '커피 카테고리입니다.');

INSERT INTO Category(name, color, image_url, description)
VALUES('상품권', '갈색', 'https...', '상품권 카테고리입니다.');

INSERT INTO Category(name, color, image_url, description)
VALUES('뷰티', '갈색', 'https...', '뷰티 카테고리입니다.');

INSERT INTO Category(name, color, image_url, description)
VALUES('패션', '갈색', 'https...', '패션 카테고리입니다.');

INSERT INTO Category(name, color, image_url, description)
VALUES('식품', '갈색', 'https...', '식품 카테고리입니다.');

INSERT INTO Category(name, color, image_url, description)
VALUES('리빙/도서', '갈색', 'https...', '리빙/도서 카테고리입니다.');

INSERT INTO Category(name, color, image_url, description)
VALUES('레저/스포츠', '갈색', 'https...', '레저/스포츠 카테고리입니다.');

INSERT INTO Category(name, color, image_url, description)
VALUES('아티스트/캐릭터', '갈색', 'https...', '아티스트/캐릭터 카테고리입니다.');

INSERT INTO Category(name, color, image_url, description)
VALUES('유아동/반려', '갈색', 'https...', '유아동/반려 카테고리입니다.');

INSERT INTO Category(name, color, image_url, description)
VALUES('디지털/가전', '갈색', 'https...', '디지털/가전 카테고리입니다.');

INSERT INTO Category(name, color, image_url, description)
VALUES('카카오프렌즈', '갈색', 'https...', '카카오프렌즈 카테고리입니다.');

INSERT INTO Category(name, color, image_url, description)
VALUES('트렌드 선물', '갈색', 'https...', '트렌드 선물 카테고리입니다.');

INSERT INTO Category(name, color, image_url, description)
VALUES('백화점', '갈색', 'https...', '백화점 카테고리입니다.');

INSERT INTO Product(name, price, image_url, category) VALUES(
    '씨솔트 카라멜 콜드 브루',
    8000,
    'https://image.istarbucks.co.kr/upload/store/skuimg/2024/04/[9200000004544]_20240423124241716.jpg',
    1
);

INSERT INTO Product(name, price, image_url, category) VALUES(
    '돌체 콜드 브루',
    5000,
    'https://image.istarbucks.co.kr/upload/store/skuimg/2021/04/[9200000002081]_20210415133656839.jpg',
    3
);

INSERT INTO Product(name, price, image_url, category) VALUES(
    '나이트로 바닐라 크림',
    7000,
    'https://image.istarbucks.co.kr/upload/store/skuimg/2021/04/[9200000002487]_20210426091745467.jpg',
    2
);

INSERT INTO Product(name, price, image_url, category) VALUES(
    '리저브 나이트로',
    6500,
    'https://image.istarbucks.co.kr/upload/store/skuimg/2021/02/[9200000002407]_20210225095106743.jpg',
    4
);


INSERT INTO Product(name, price, image_url, category) VALUES(
    '여수 윤슬 헤이즐넛 콜드브루',
    7500,
    'https://image.istarbucks.co.kr/upload/store/skuimg/2023/08/[9200000004750]_20230801101408624.jpg',
    5
);

INSERT INTO Product(name, price, image_url, category) VALUES(
    '콜드 브루',
    4500,
    'https://image.istarbucks.co.kr/upload/store/skuimg/2021/04/[9200000000038]_20210430113202458.jpg',
    6
);


INSERT INTO Member(email, password) VALUES('admin@admin', 'admin');
INSERT INTO Member(email, password) VALUES('root@admin', 'root');

INSERT INTO Options(name, quantity, product_id) VALUES('스타벅스 라떼', 15, 1);
INSERT INTO Options(name, quantity, product_id) VALUES('스타벅스 아메리카노', 50, 1);
INSERT INTO Options(name, quantity, product_id) VALUES('스타벅스 에스프레소', 20, 1);
INSERT INTO Options(name, quantity, product_id) VALUES('스타벅스 프라프치노', 3, 1);
INSERT INTO Options(name, quantity, product_id) VALUES('신세계 백화점 5만원권', 5, 2);
INSERT INTO Options(name, quantity, product_id) VALUES('현대 백화점 10만원권', 10, 2);


