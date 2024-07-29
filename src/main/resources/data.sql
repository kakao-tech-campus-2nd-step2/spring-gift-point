-- Category data insertion
INSERT INTO Category (name, color, image_url, description) VALUES ('category1', 'red', 'image.jpg', '');
INSERT INTO Category (name, color, image_url, description) VALUES ('category2', 'blue', 'image.jpg', '');
INSERT INTO Category (name, color, image_url, description) VALUES ('category3', 'yellow', 'image.jpg', '');

-- Product data insertion
INSERT INTO Product (name, price, image_url, category_id) VALUES ('product1', 1000, 'image.jpg', 1);
INSERT INTO Product (name, price, image_url, category_id) VALUES ('product2', 2000, 'image.jpg', 2);
INSERT INTO Product (name, price, image_url, category_id) VALUES ('product3', 3000, 'image.jpg', 3);

-- Option data insertion
INSERT INTO Option (name, quantity, product_id) VALUES ('option1', 10, 1);
INSERT INTO Option (name, quantity, product_id) VALUES ('option2', 20, 2);
INSERT INTO Option (name, quantity, product_id) VALUES ('option3', 30, 3);

