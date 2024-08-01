INSERT INTO category (name, color, image_url, description) VALUES
    ('교환권', '#6c95d1', 'https://gift-s.kakaocdn.net/dn/gift/images/m640/dimm_theme.png', '');

INSERT INTO product (name, price, image_url, category_id) VALUES
    ('아이스 카페 아메리카노 T', 4500, 'https://st.kakaocdn.net/product/gift/product/20231010111814_9a667f9eccc943648797925498bdd8a3.jpg', 1);

INSERT INTO member (email, password) VALUES
    ('admin@email.com', 'cGFzc3dvcmQ='); -- Base64 encoded 'password'

INSERT INTO options (name, quantity, product_id) VALUES
    ('01. [Best] 시어버터 핸드 & 시어 스틱 립 밤', 969, 1);