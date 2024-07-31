INSERT INTO category (name)
VALUES ('생일');
INSERT INTO category (name)
VALUES ('교환권');

INSERT INTO product (name, price, image_url, category_id)
VALUES ('외식 통합권 10만원권', 100000, 'image_url_1', 1),
       ('[선물포장] 소바쥬 오 드 뚜왈렛 60ML', 122000, 'image_url_4', 1),
       ('[선물포장/미니퍼퓸증정] 디켄터 리드 디퓨저 300ml + 메시지카드', 108000, 'image_url_2', 2),
       ('[단독각인] 피렌체 1221 에디션 오드코롱 50ml (13종 택1)', 145000, 'image_url_3', 2);

INSERT INTO option (product_id, name, quantity)
VALUES (1, 'Option A', 100),
       (1, 'Option B', 200),
       (1, 'Option C', 300);

INSERT INTO option (product_id, name, quantity)
VALUES (2, 'Option A', 150),
       (2, 'Option B', 250),
       (2, 'Option C', 350);

INSERT INTO option (product_id, name, quantity)
VALUES (3, 'Option A', 100),
       (3, 'Option B', 200),
       (3, 'Option C', 300);

INSERT INTO option (product_id, name, quantity)
VALUES (4, 'Option A', 100),
       (4, 'Option B', 200),
       (4, 'Option C', 300);
