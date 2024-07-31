-- 카테고리 데이터 입력
INSERT INTO category (id, name, description, color, image_url)
VALUES (2920, '생일', '감동을 높여줄 생일 선물 리스트', '#5949a3', 'https://img1.daumcdn.net/thumb/S104x104/?fname=https%3A%2F%2Ft1.daumcdn.net%2Fgift%2Fhome%2Ftheme%2F292020231106_MXMUB.png'),
       (2930, '교환권', '놓치면 후회할 교환권 특가', '#9290C3', 'https://img1.daumcdn.net/thumb/S104x104/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20240131153049_5a22b137a8d346e9beb020a7a7f4254a.jpg');

/*INSERT INTO category (name, color, image_url, description)
VALUES ('뷰티', '#e17ea5', 'https://gift-s.kakaocdn.net/dn/gift/images/m640/beauty_theme.png', '아름다움을 선사하는 뷰티 제품'),
         ('패션', '#4a90e2', 'https://gift-s.kakaocdn.net/dn/gift/images/m640/fashion_theme.png', '트렌디한 패션 아이템'),
         ('식품', '#7ed321', 'https://gift-s.kakaocdn.net/dn/gift/images/m640/food_theme.png', '맛있는 식품 선물'),
         ('리빙/도서', '#9b9b9b', 'https://gift-s.kakaocdn.net/dn/gift/images/m640/living_book_theme.png', '일상을 풍요롭게 하는 리빙 제품과 도서'),
         ('레저/스포츠', '#50e3c2', 'https://gift-s.kakaocdn.net/dn/gift/images/m640/leisure_sports_theme.png', '활동적인 삶을 위한 레저/스포츠 용품'),
         ('아티스트/캐릭터', '#bd10e0', 'https://gift-s.kakaocdn.net/dn/gift/images/m640/artist_character_theme.png', '좋아하는 아티스트와 캐릭터 상품'),
         ('유아동/반려', '#ff9500', 'https://gift-s.kakaocdn.net/dn/gift/images/m640/kids_pets_theme.png', '아이와 반려동물을 위한 선물'),
         ('디지털/가전', '#4a4a4a', 'https://gift-s.kakaocdn.net/dn/gift/images/m640/digital_appliance_theme.png', '최신 디지털 기기와 가전제품'),
         ('카카오프렌즈', '#fae100', 'https://gift-s.kakaocdn.net/dn/gift/images/m640/kakao_friends_theme.png', '귀여운 카카오프렌즈 상품'),
         ('트렌드 선물', '#ff5722', 'https://gift-s.kakaocdn.net/dn/gift/images/m640/trend_gift_theme.png', '최신 트렌드를 반영한 선물'),
         ('백화점', '#8e44ad', 'https://gift-s.kakaocdn.net/dn/gift/images/m640/department_store_theme.png', '프리미엄 백화점 상품');*/

-- 다양한 종류의 상품을 간단하고 중복 없는 imageUrl로 작성해서 입력하기 총 10개
-- 상품 데이터 입력
INSERT INTO product (id, name, image_url, price, category_id)
VALUES (3245119, '[단독각인] 피렌체 1221 에디션 오드코롱 50ml (13종 택1)', 'https://st.kakaocdn.net/product/gift/product/20240215083306_8e1db057580145829542463a84971ae3.png', 145000, 2920),
       (2263833, '외식 통합권 10만원권', 'https://st.kakaocdn.net/product/gift/product/20200513102805_4867c1e4a7ae43b5825e9ae14e2830e3.png', 100000, 2920),
       (6502823, '[선물포장/미니퍼퓸증정] 디켄터 리드 디퓨저 300ml + 메세지카드', 'https://st.kakaocdn.net/product/gift/product/20240215112140_11f857e972bc4de6ac1d2f1af47ce182.jpg', 108000, 2930),
       (1181831, '[선물포장] 소바쥬 오 드 뚜왈렛 60ML', 'https://st.kakaocdn.net/product/gift/product/20240214150740_ad25267defa64912a7c030a7b57dc090.jpg', 122000, 2930),
       (1379982, '[정관장] 홍삼정 에브리타임 리미티드 (10ml x 30포)', 'https://st.kakaocdn.net/product/gift/product/20240118135914_a6e1a7442ea04aa49add5e02ed62b4c3.jpg', 133000, 2920);

-- 회원 데이터 입력 (예시)
INSERT INTO member (email, password)
VALUES ('user1@example.com', 'password1'),
       ('user2@example.com', 'password2');

-- user1
INSERT INTO wish_list (id, product_id, member_id)
VALUES (1, 3245119, 1),
       (2, 2263833, 1);

-- 키보드 상품(PK 1)에 대한 옵션 데이터 입력
INSERT INTO option (id, name, quantity, product_id)
VALUES (1, 'Option A', 10, 3245119),
       (2, 'Option B', 20, 3245119);



