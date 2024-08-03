INSERT INTO categories (name, color, image_url, description)
VALUES('교환권', '#FF0000', 'http://example.com/image.jpg', ''),
      ('상품권', '#FF0000', 'http://example.com/image.jpg', ''),
      ('뷰티', '#FF0000', 'http://example.com/image.jpg', ''),
      ('패션', '#FF0000', 'http://example.com/image.jpg', ''),
      ('식품', '#FF0000', 'http://example.com/image.jpg', ''),
      ('리빙/도서', '#FF0000', 'http://example.com/image.jpg', ''),
      ('레저/스포츠', '#FF0000', 'http://example.com/image.jpg', ''),
      ('아티스트/캐릭터', '#FF0000', 'http://example.com/image.jpg', ''),
      ('유아동/반려', '#FF0000', 'http://example.com/image.jpg', ''),
      ('디지털/가전', '#FF0000', 'http://example.com/image.jpg', ''),
      ('카카오프렌즈', '#FF0000', 'http://example.com/image.jpg', ''),
      ('트렌드 선물', '#FF0000', 'http://example.com/image.jpg', ''),
      ('백화점', '#FF0000', 'http://example.com/image.jpg', '');

INSERT INTO users (email, password) VALUES
    ('seolbin@example.com', 'password123');

INSERT INTO products (name, price, image_url, category_id) VALUES
    ('Sample Product', 1000, 'https://via.placeholder.com/150', 1);

INSERT INTO options (product_id, name, quantity) VALUES
                                                     (1, 'Option 1', 100),
                                                     (1, 'Option 2', 200);