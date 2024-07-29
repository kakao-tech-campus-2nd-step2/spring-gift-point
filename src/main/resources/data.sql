-- 카테고리 데이터 입력
INSERT INTO category (name, color, image_url, description)
VALUES ('교환권', '#6c95d1', 'https://gift-s.kakaocdn.net/dn/gift/images/m640/dimm_theme.png', '다른 상품으로 교환할 수 있는 교환권'),
         ('상품권', '#f2d042', 'https://gift-s.kakaocdn.net/dn/gift/images/m640/gift_card_theme.png', '다양한 상품권을 만나보세요'),
         ('뷰티', '#e17ea5', 'https://gift-s.kakaocdn.net/dn/gift/images/m640/beauty_theme.png', '아름다움을 선사하는 뷰티 제품'),
         ('패션', '#4a90e2', 'https://gift-s.kakaocdn.net/dn/gift/images/m640/fashion_theme.png', '트렌디한 패션 아이템'),
         ('식품', '#7ed321', 'https://gift-s.kakaocdn.net/dn/gift/images/m640/food_theme.png', '맛있는 식품 선물'),
         ('리빙/도서', '#9b9b9b', 'https://gift-s.kakaocdn.net/dn/gift/images/m640/living_book_theme.png', '일상을 풍요롭게 하는 리빙 제품과 도서'),
         ('레저/스포츠', '#50e3c2', 'https://gift-s.kakaocdn.net/dn/gift/images/m640/leisure_sports_theme.png', '활동적인 삶을 위한 레저/스포츠 용품'),
         ('아티스트/캐릭터', '#bd10e0', 'https://gift-s.kakaocdn.net/dn/gift/images/m640/artist_character_theme.png', '좋아하는 아티스트와 캐릭터 상품'),
         ('유아동/반려', '#ff9500', 'https://gift-s.kakaocdn.net/dn/gift/images/m640/kids_pets_theme.png', '아이와 반려동물을 위한 선물'),
         ('디지털/가전', '#4a4a4a', 'https://gift-s.kakaocdn.net/dn/gift/images/m640/digital_appliance_theme.png', '최신 디지털 기기와 가전제품'),
         ('카카오프렌즈', '#fae100', 'https://gift-s.kakaocdn.net/dn/gift/images/m640/kakao_friends_theme.png', '귀여운 카카오프렌즈 상품'),
         ('트렌드 선물', '#ff5722', 'https://gift-s.kakaocdn.net/dn/gift/images/m640/trend_gift_theme.png', '최신 트렌드를 반영한 선물'),
         ('백화점', '#8e44ad', 'https://gift-s.kakaocdn.net/dn/gift/images/m640/department_store_theme.png', '프리미엄 백화점 상품');

-- 다양한 종류의 상품을 간단하고 중복 없는 imageUrl로 작성해서 입력하기 총 10개
-- 상품 데이터 입력
INSERT INTO product (name, price, image_url)
VALUES ('키보드', 10000, 'https://www.google.com/keyboard.png'),
         ('마우스', 5000, 'https://www.google.com/mouse.png'),
         ('모니터', 20000, 'https://www.google.com/monitor.png'),
         ('노트북', 30000, 'https://www.google.com/laptop.png'),
         ('키보드2', 10000, 'https://www.google.com/keyboard2.png'),
         ('마우스2', 5000, 'https://www.google.com/mouse2.png'),
         ('모니터2', 20000, 'https://www.google.com/monitor2.png'),
         ('노트북2', 30000, 'https://www.google.com/laptop2.png'),
         ('키보드3', 10000, 'https://www.google.com/keyboard3.png'),
         ('마우스3', 5000, 'https://www.google.com/mouse3.png');


-- 회원 데이터 입력: 관리자 1명, 사용자 3명
INSERT INTO member (email, password)
VALUES ('admin@email.com', 'admin'),
       ('user1@email.com', 'password1'),
       ('user2@email.com', 'password2'),
       ('user3@email.com', 'password3');

-- 위시리스트 데이터 입력: 사용자 3명이 각각 3개씩 상품을 담음

-- user1
INSERT INTO wish_list (product_id, member_id, quantity)
VALUES (1, 2, 1),
       (2, 2, 2),
       (3, 2, 3);

-- user2
INSERT INTO wish_list (product_id, member_id, quantity)
VALUES (4, 3, 1),
       (5, 3, 2),
       (6, 3, 3);

-- user3
INSERT INTO wish_list (product_id, member_id, quantity)
VALUES (7, 4, 1),
       (8, 4, 2),
       (9, 4, 3);

-- 키보드 상품(PK 1)에 대한 옵션 데이터 입력
INSERT INTO option (name, quantity, product_id)
VALUES ('청축', 10, 1),
       ('적축', 10, 1),
       ('흑축', 10, 1);



