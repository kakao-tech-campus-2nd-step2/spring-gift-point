INSERT INTO category (name, color, image_url, description)
VALUES ('생일', '#6c95d1', 'https://gift-s.kakaocdn.net/dn/gift/images/m640/dimm_theme.png', '생일 카테고리입니다!');
INSERT INTO category (name, color, image_url, description)
VALUES ('교환권', '#6c95d1', 'https://gift-s.kakaocdn.net/dn/gift/images/m640/dimm_theme.png', '교환권 카테고리입니다!');

INSERT INTO product (name, price, image_url, category_id)
VALUES ('외식 통합권 10만원권', 100000, 'image_url_1', 1),
       ('[선물포장] 소바쥬 오 드 뚜왈렛 60ML', 122000, 'image_url_4', 1),
       ('[선물포장/미니퍼퓸증정] 디켄터 리드 디퓨저 300ml + 메시지카드', 108000, 'image_url_2', 2),
       ('[단독각인] 피렌체 1221 에디션 오드코롱 50ml (13종 택1)', 145000, 'image_url_3', 2);

INSERT INTO option (product_id, name, version, quantity)
VALUES (1, 'Option A', 0, 100),
       (1, 'Option B', 0, 200),
       (1, 'Option C', 0, 300);

INSERT INTO option (product_id, name, version, quantity)
VALUES (2, 'Option A', 0, 150),
       (2, 'Option B', 0, 250),
       (2, 'Option C', 0, 350);

INSERT INTO option (product_id, name, version, quantity)
VALUES (3, 'Option A', 0, 100),
       (3, 'Option B', 0, 200),
       (3, 'Option C', 0, 300);

INSERT INTO option (product_id, name, version, quantity)
VALUES (4, 'Option A', 0, 100),
       (4, 'Option B', 0, 200),
       (4, 'Option C', 0, 300);
