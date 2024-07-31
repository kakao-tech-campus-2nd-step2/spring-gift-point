-- Insert default category
INSERT INTO category (name, color, image_url) VALUES ('Default Category', '#FFFFFF', 'http://example.com/default.png');

-- Insert product with valid CATEGORY_ID
INSERT INTO product (name, price, image_url, category_id) VALUES ('아이스 아메리카노', 4500, 'https://st.kakaocdn.net/product/gift/product/20231010111814_9a667f9eccc943648797925498bdd8a3.jpg', 1);
