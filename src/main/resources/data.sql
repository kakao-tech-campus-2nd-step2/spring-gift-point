--- 카테고리 데이터 12개 생성
INSERT INTO category(name,color,description,image_url) values ( '생일','#d6c574','감동을 높여줄 생일 선물 리스트','https://img1.kakaocdn.net/thumb/C414x414@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20240417111629_616eccb9d4cd464fa06d3430947dce15.jpg');
INSERT INTO category(name,color,description,image_url) values ( '스몰럭셔리','#cb92f7','당신을 위한 작은 사치, 스몰 럭셔리 아이템','https://img1.kakaocdn.net/thumb/C305x305@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20240701091625_56c893ada14846d893d669b1b9f02c52.jpg');
INSERT INTO category(name,color,description,image_url) values ( '명품선물','#ddba5e','품격있는 명품 선물 제안','https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTWYXZWDLYCXlIGD8swntMMDpt1II9MYlptpw&s');
INSERT INTO category(name,color,description,image_url) values ( '가벼운 선물','#484b1a','예산은 가볍게, 감동은 무겁게❤️','https://img1.kakaocdn.net/thumb/C305x305@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20240213103359_7a873afe090d4643b96774da928b5c8b.jpg');
INSERT INTO category(name,color,description,image_url) values ( '시원한 선물','#67b2b3','어느새 찾아온 무더운 여름,기분 좋아지는 시원한 선물☀️','https://img1.kakaocdn.net/thumb/C305x305@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20240508110848_bb58ddb7f624485289e031f0c26907c7.jpg');
INSERT INTO category(name,color,description,image_url) values ( '결혼/집들이','#0a9d82','들려오는 지인들의 좋은 소식','https://img1.kakaocdn.net/thumb/C305x305@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20231223154836_2cf64ad710104533ab2c022bbf3a9616.jpg');
INSERT INTO category(name,color,description,image_url) values ( '팬심저격','#db1378','최애에 진심인 당신을 위한 팬심저격 굿즈 모음','https://img1.kakaocdn.net/thumb/C305x305@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20240705101301_2ab500a69ba84adf8df2654e23efbea9.jpg');
INSERT INTO category(name,color,description,image_url) values ( '시험/응원','#1b358b','잘했고, 잘하고 있고, 잘할 거야','https://img1.kakaocdn.net/thumb/C305x305@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20231115163510_335e7a502e7243179432a48627e0a603.jpg');
INSERT INTO category(name,color,description,image_url) values ( '교환권','#94c3fe','싸다 싸','https://img1.kakaocdn.net/thumb/C414x414@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20240723165144_28b6bf71f7754fa6967766f3c9799ce1.jpg');
INSERT INTO category(name,color,description,image_url) values ( '건강/비타민','#450901','건강을 기원하는 마음 담아 선물하세요❤️','https://img1.kakaocdn.net/thumb/C305x305@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20240722152218_1e053d55cd8a44e19d52acbe610b77e2.jpg');
INSERT INTO category(name,color,description,image_url) values ( '과일/한우','#b23d1a','정성 가득한 과일/한우 선물로 감사한 마음을 전해보세요❤️','https://img1.kakaocdn.net/thumb/C305x305@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20240516094905_425d02258d7d4fffaccc7219285a951d.jpg');
INSERT INTO category(name,color,description,image_url) values ( '출산/키즈','#808804','벅찬 감동의 마음을 전할 엄마와 아이를 위한 세심한 선물👼🏻','https://img1.kakaocdn.net/thumb/C414x414@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20240723154022_4293c778400d4257bd1961c3538899d9.jpg');

--- 상품&옵션 데이터 생성
INSERT INTO product (name, price, image_url, category_id)
VALUES ('[단독]하겐다즈 프리미엄 수제 아이스크림 케이크 리얼블랑 (바닐라+벨지안초코)', 29900,'https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20240417111629_616eccb9d4cd464fa06d3430947dce15.jpg',
        1);
INSERT INTO option (name, quantity, product_id) VALUES ('옵션 없음', 50, 1);
INSERT INTO option (name, quantity, product_id) VALUES ('옵션 없는데 있음', 50, 1);

INSERT INTO product (name, price, image_url, category_id)
VALUES ('스타벅스 더블 초콜릿 케이크', 33000,'https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20240517113350_31cb48285fa148c2a08d2f8715a6f94d.jpg',
        1);
INSERT INTO option (name, quantity, product_id) VALUES ('옵션 없음', 50, 2);
INSERT INTO option (name, quantity, product_id) VALUES ('옵션 없는데 있음', 50, 2);

INSERT INTO product (name, price, image_url, category_id)
VALUES ('[샤워볼&핸드크림 증정/선물포장] 바디클렌저 250ml 세트 (자몽/머스크)', 36000,
        'https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20240725163505_a3c5336e7f6b44c883bc0f78e994a524.jpg',
        2);
INSERT INTO option (name, quantity, product_id) VALUES ('자몽', 50, 3);
INSERT INTO option (name, quantity, product_id) VALUES ('머스크', 75, 3);

INSERT INTO product (name, price, image_url, category_id)
VALUES ('[헤어오일1위/선물포장] 글로시 헤어 에센스 에브리데이 헤어 오일 60ML+베이비 스크런치 1PCS 세트', 27000,
        'https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20240703120413_8cbb48aae6a4422eac6fcad302c1feb3.jpg',
        2);
INSERT INTO option (name, quantity, product_id) VALUES ('Love you', 50, 4);
INSERT INTO option (name, quantity, product_id) VALUES ('Happy Day', 75, 4);

INSERT INTO product (name, price, image_url, category_id)
VALUES ('[구찌 하트] 인터로킹 G 반지', 490000,
        'https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20210118200008_4428c7418a334fe6a04418c0bf339d4f',
        3);

INSERT INTO option (name, quantity, product_id) VALUES ('사이즈 08', 10, 5);
INSERT INTO option (name, quantity, product_id) VALUES ('사이즈 09', 3, 5);
INSERT INTO option (name, quantity, product_id) VALUES ('사이즈 10', 2, 5);

INSERT INTO product (name, price, image_url, category_id)
VALUES ('플리스 소재의 생 로랑 토트백', 1450000,
        'https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F8bw5KMLJqwh42YlfD5kfgg%2FtYRA1iecdsCGRLyaYdVkdUmkZKmLwXV-ftzEJ6IIyHQ',
        3);
INSERT INTO option (name, quantity, product_id) VALUES ('옵션 없음', 50, 6);


INSERT INTO product (name, price, image_url, category_id)
VALUES ('프랑스 프리미엄 마카롱 (12입/선물세트)', 13900,
        'https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20240213092703_1e00da7c33374a62a5220a29fb123b73.jpg',
        4);
INSERT INTO option (name, quantity, product_id) VALUES ('옵션 없음', 50, 7);

INSERT INTO product (name, price, image_url, category_id)
VALUES ('[상콤달콤 간식선물] 상큼한 딸기가 그대로 쏙! 잔망루피 딸기크림떡', 16900,
        'https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20240213103359_7a873afe090d4643b96774da928b5c8b.jpg',
        4);
INSERT INTO option (name, quantity, product_id) VALUES ('옵션 없음', 50, 8);

INSERT INTO product (name, price, image_url, category_id)
VALUES ('시원하게 수분충전, BEST 오설록 포레스트 티박스 (6종 42입)', 29900,
        'https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20240716105217_c5f8ed6781b442ebaa6cf19ef89a1589.png',
        5);
INSERT INTO option (name, quantity, product_id) VALUES ('옵션 없음', 50, 9);
INSERT INTO option (name, quantity, product_id) VALUES ('옵션 있음', 50, 9);

INSERT INTO product (name, price, image_url, category_id)
VALUES ('[급속살얼음] 에어컨선풍기 휴대용 냉각 쿨링팬 미니 선풍기 올 여름 나만 믿어!', 16900,
        'https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20240723182944_ccd88a5817954aef987bb3162d656ad1.jpg',
        5);

INSERT INTO option (name, quantity, product_id) VALUES ('화이트', 13, 10);
INSERT INTO option (name, quantity, product_id) VALUES ('블루', 15, 10);


INSERT INTO product (name, price, image_url, category_id)
VALUES ('인사이디 휴대용 무선 LP 턴테이블 LED 무드등 블루투스 스피커', 69900,
        'https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20240801104650_404d1b98cf074ea680164da9570b9fe5.jpg',
        6);
INSERT INTO option (name, quantity, product_id) VALUES ('IBT-Retro Light_베이지 (무선)', 40, 11);
INSERT INTO option (name, quantity, product_id) VALUES ('IBT-Retro Light_옐로우 (무선)', 35, 11);

INSERT INTO product (name, price, image_url, category_id)
VALUES ('돌체구스토 캡슐 커피머신 인피니시마+스타벅스GIFT', 89900,
        'https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20240722102315_f77fc9c665de49cca4abab1e927812fc.jpg',
        6);
INSERT INTO option (name, quantity, product_id) VALUES ('블랙', 40, 12);
INSERT INTO option (name, quantity, product_id) VALUES ('화이트', 40, 12);

INSERT INTO product (name, price, image_url, category_id)
VALUES ('NOS7 X 모남희 쏜희 피규어 키링', 19000,
        'https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20240719210139_a51b634cb6224485919f18d1b858cdb5.jpg',
        7);
INSERT INTO option (name, quantity, product_id) VALUES ('7월 30일부터 순차 발송', 40, 13);
INSERT INTO option (name, quantity, product_id) VALUES ('8월 30일부터 순차 발송', 40, 13);
INSERT INTO option (name, quantity, product_id) VALUES ('9월 30일부터 순차 발송', 40, 13);

INSERT INTO product (name, price, image_url, category_id)
VALUES ('[최고심] ''행운&행복&사랑'' 메시지인형 5종 택1', 19000,
        'https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20240725144220_53dee344d55e43ff82d9fd641647b466.jpg',
        7);
INSERT INTO option (name, quantity, product_id) VALUES ('안경고심', 88, 14);
INSERT INTO option (name, quantity, product_id) VALUES ('아가고심', 250, 14);
INSERT INTO option (name, quantity, product_id) VALUES ('아힘들어', 34, 14);

INSERT INTO product (name, price, image_url, category_id)
VALUES ('''마음의 선물'' 힘내라 홍삼정 마음을 담은 6년근 홍삼스틱 30포', 18900,
        'https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20240531150628_2af8065733ab449bb81d9d5b55a68c28.jpg',
        8);
INSERT INTO option (name, quantity, product_id) VALUES ('너의 내일을 응원해!', 88, 15);
INSERT INTO option (name, quantity, product_id) VALUES ('너의 대박을 응원해!', 250, 15);
INSERT INTO option (name, quantity, product_id) VALUES ('너의 행운을 소망해!', 34, 15);

INSERT INTO product (name, price, image_url, category_id)
VALUES ('스타벅스 파베 생 초콜릿 (밀크 or 다크) 1EA', 19900,
        'https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20240105174500_8239a8f780754322861599425cd924ac.png',
        8);

INSERT INTO option (name, quantity, product_id) VALUES ('밀크', 1500, 16);
INSERT INTO option (name, quantity, product_id) VALUES ('다크', 1590, 16);

INSERT INTO product (name, price, image_url, category_id)
VALUES ('이마트 모바일금액권 5만원권', 50000,
        'https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20240319133340_790b6fb28fa24f1fbb3eee4471c0a7fb.jpg',
        9);
INSERT INTO option (name, quantity, product_id) VALUES ('옵션 없음', 50, 17);

INSERT INTO product (name, price, image_url, category_id)
VALUES ('이마트 모바일금액권 10만원권', 100000,
        'https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20240319133308_f2dcc635a99e464f9d005562a3bc613a.jpg',
        9);
INSERT INTO option (name, quantity, product_id) VALUES ('옵션 없음', 50, 18);

INSERT INTO product (name, price, image_url, category_id)
VALUES ('[정관장] 홍삼정 에브리타임 샷 (20ml x 30병)', 69900,
        'https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20240423084545_74c2b383354d44a3a93ee328816aed12.jpg',
        10);
INSERT INTO option (name, quantity, product_id) VALUES ('옵션 없음', 800, 19);

INSERT INTO product (name, price, image_url, category_id)
VALUES ('농협안심한우 1등급 ''스페셜 구이세트'' 900g (등심+채끝+안심, 각 300g)', 10900,
        'https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20240516094905_425d02258d7d4fffaccc7219285a951d.jpg',
        11);
INSERT INTO option (name, quantity, product_id) VALUES ('옵션 없음', 800, 20);

INSERT INTO product (name, price, image_url, category_id)
VALUES ('"하기스 9종 출산선물세트" NEW 하기스 네이처메이드 기저귀 출산 축하 선물세트 C', 69900,
        'https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20240723154022_4293c778400d4257bd1961c3538899d9.jpg',
        12);
INSERT INTO option (name, quantity, product_id) VALUES ('옵션 없음', 800, 21);
