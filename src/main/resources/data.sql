-- 카테고리 데이터 입력
INSERT INTO category (id, name, description, color, image_url)
VALUES (2920, '생일', '감동을 높여줄 생일 선물 리스트', '#5949a3', 'https://img1.daumcdn.net/thumb/S104x104/?fname=https%3A%2F%2Ft1.daumcdn.net%2Fgift%2Fhome%2Ftheme%2F292020231106_MXMUB.png'),
       (2930, '교환권', '놓치면 후회할 교환권 특가', '#9290C3', 'https://img1.daumcdn.net/thumb/S104x104/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20240131153049_5a22b137a8d346e9beb020a7a7f4254a.jpg');

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

-- 옵션 데이터 입력
INSERT INTO option (id, name, quantity, product_id)
VALUES (1, 'Option A', 10, 3245119),
       (2, 'Option B', 20, 3245119);
