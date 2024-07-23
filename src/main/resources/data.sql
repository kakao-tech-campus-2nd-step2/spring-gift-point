-- member
INSERT INTO member (email, password) VALUES ('user1@example.com', '$2a$10$DowQ5TeDZa/WZMkEzZcV/OiTFeYYOCfMZ1zxPC5iH70Z/84kO0GhO'); -- password: password

-- category
INSERT INTO category (name, color, image_url, description) VALUES ('Category1', 'Red', 'https://example.com/category1.jpg', 'Description for Category 1');
INSERT INTO category (name, color, image_url, description) VALUES ('Category2', 'Blue', 'https://example.com/category2.jpg', 'Description for Category 2');

-- product
INSERT INTO product (name, price, image_url, category_id) VALUES ('Product1', 1000, 'https://example.com/image1.jpg', 1);
INSERT INTO product (name, price, image_url, category_id) VALUES ('Product2', 2000, 'https://example.com/image2.jpg', 2);

-- option
INSERT INTO option (name, quantity, product_id) VALUES ('Option1', 100, 1);
INSERT INTO option (name, quantity, product_id) VALUES ('Option2', 200, 1);

-- wish
INSERT INTO wishlist (member_id, product_id) VALUES (1, 1);
