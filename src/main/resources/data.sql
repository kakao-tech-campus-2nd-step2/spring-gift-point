INSERT INTO member (email, password) VALUES ('user1@example.com', '$2a$10$DowQ5TeDZa/WZMkEzZcV/OiTFeYYOCfMZ1zxPC5iH70Z/84kO0GhO'); -- password: password
INSERT INTO product (name, price, image_url) VALUES ('Product1', 1000, 'http://example.com/image1.jpg');
INSERT INTO product (name, price, image_url) VALUES ('Product2', 2000, 'http://example.com/image2.jpg');
INSERT INTO wishlist (member_id, product_id) VALUES (1, 1);
