-- 카테고리 삽입
INSERT INTO category (id, name, color, image_url, description) VALUES (1, 'coffee', '#0000FF', 'http://example.com/category-x.jpg', 'This is a category of coffee');
INSERT INTO category (id, name, color, image_url, description) VALUES (2, 'chicken', '#FF0000', 'http://example.com/category-y.jpg', 'This is a category of chicken');
INSERT INTO category (id, name, color, image_url, description) VALUES (3, 'cake', '#0000FF', 'http://example.com/category-z.jpg', 'This is a category of cake');
INSERT INTO category (id, name, color, image_url, description) VALUES (4, 'donut', '#0000FF', 'http://example.com/category-w.jpg', 'This is a category of donut');

-- 상품 삽입
-- Coffee Category
INSERT INTO product (id, name, price, image_url, category_id) VALUES (5, 'Americano', 3500, 'http://example.com/product-americano.jpg', 1);
INSERT INTO product (id, name, price, image_url, category_id) VALUES (6, 'Latte', 4000, 'http://example.com/product-latte.jpg', 1);
INSERT INTO product (id, name, price, image_url, category_id) VALUES (7, 'Cappuccino', 3800, 'http://example.com/product-cappuccino.jpg', 1);
INSERT INTO product (id, name, price, image_url, category_id) VALUES (8, 'Macchiato', 4500, 'http://example.com/product-macchiato.jpg', 1);
INSERT INTO product (id, name, price, image_url, category_id) VALUES (9, 'Mocha', 4200, 'http://example.com/product-mocha.jpg', 1);
-- Chicken Category
INSERT INTO product (id, name, price, image_url, category_id) VALUES (10, 'Grilled Chicken', 16000, 'http://example.com/product-grilled-chicken.jpg', 2);
INSERT INTO product (id, name, price, image_url, category_id) VALUES (11, 'Spicy Chicken', 17000, 'http://example.com/product-spicy-chicken.jpg', 2);
INSERT INTO product (id, name, price, image_url, category_id) VALUES (12, 'BBQ Chicken', 18000, 'http://example.com/product-bbq-chicken.jpg', 2);
INSERT INTO product (id, name, price, image_url, category_id) VALUES (13, 'Lemon Chicken', 15500, 'http://example.com/product-lemon-chicken.jpg', 2);
INSERT INTO product (id, name, price, image_url, category_id) VALUES (14, 'Garlic Chicken', 16500, 'http://example.com/product-garlic-chicken.jpg', 2);
-- Cake Category
INSERT INTO product (id, name, price, image_url, category_id) VALUES (15, 'Chocolate Cake', 6000, 'http://example.com/product-chocolate-cake.jpg', 3);
INSERT INTO product (id, name, price, image_url, category_id) VALUES (16, 'Fruit Cake', 5500, 'http://example.com/product-fruit-cake.jpg', 3);
INSERT INTO product (id, name, price, image_url, category_id) VALUES (17, 'Carrot Cake', 6500, 'http://example.com/product-carrot-cake.jpg', 3);
INSERT INTO product (id, name, price, image_url, category_id) VALUES (18, 'Red Velvet Cake', 7000, 'http://example.com/product-red-velvet-cake.jpg', 3);
INSERT INTO product (id, name, price, image_url, category_id) VALUES (19, 'Vanilla Cake', 5000, 'http://example.com/product-vanilla-cake.jpg', 3);
-- Donut Category
INSERT INTO product (id, name, price, image_url, category_id) VALUES (20, 'Glazed Donut', 1800, 'http://example.com/product-glazed-donut.jpg', 4);
INSERT INTO product (id, name, price, image_url, category_id) VALUES (21, 'Sugar Donut', 1500, 'http://example.com/product-sugar-donut.jpg', 4);
INSERT INTO product (id, name, price, image_url, category_id) VALUES (22, 'Jelly Donut', 2000, 'http://example.com/product-jelly-donut.jpg', 4);
INSERT INTO product (id, name, price, image_url, category_id) VALUES (23, 'Cinnamon Donut', 2100, 'http://example.com/product-cinnamon-donut.jpg', 4);
INSERT INTO product (id, name, price, image_url, category_id) VALUES (24, 'Maple Donut', 2200, 'http://example.com/product-maple-donut.jpg', 4);

-- 옵션 삽입
-- Espresso Options
INSERT INTO option (id, name, quantity, product_id) VALUES (5, 'Soy Milk', 30, 1);
INSERT INTO option (id, name, quantity, product_id) VALUES (6, 'Caramel Syrup', 15, 1);
INSERT INTO option (id, name, quantity, product_id) VALUES (7, 'Vanilla Syrup', 20, 1);
-- Fried Chicken Options
INSERT INTO option (id, name, quantity, product_id) VALUES (8, 'Spicy Sauce', 25, 2);
INSERT INTO option (id, name, quantity, product_id) VALUES (9, 'Extra Crispy', 30, 2);
INSERT INTO option (id, name, quantity, product_id) VALUES (10, 'Honey Mustard', 20, 2);
-- Cheesecake Options
INSERT INTO option (id, name, quantity, product_id) VALUES (11, 'Whipped Cream', 20, 3);
INSERT INTO option (id, name, quantity, product_id) VALUES (12, 'Caramel Topping', 15, 3);
INSERT INTO option (id, name, quantity, product_id) VALUES (13, 'Chocolate Drizzle', 10, 3);
-- Chocolate Donut Options
INSERT INTO option (id, name, quantity, product_id) VALUES (14, 'Sprinkles', 25, 4);
INSERT INTO option (id, name, quantity, product_id) VALUES (15, 'Peanut Butter', 10, 4);
INSERT INTO option (id, name, quantity, product_id) VALUES (16, 'Coconut Flakes', 15, 4);
-- Americano Options
INSERT INTO option (id, name, quantity, product_id) VALUES (17, 'Extra Ice', 20, 5);
INSERT INTO option (id, name, quantity, product_id) VALUES (18, 'Lemon Slice', 10, 5);
INSERT INTO option (id, name, quantity, product_id) VALUES (19, 'Mint Leaves', 15, 5);
-- Grilled Chicken Options
INSERT INTO option (id, name, quantity, product_id) VALUES (20, 'BBQ Sauce', 25, 10);
INSERT INTO option (id, name, quantity, product_id) VALUES (21, 'Garlic Sauce', 30, 10);
INSERT INTO option (id, name, quantity, product_id) VALUES (22, 'Lemon Herb', 20, 10);
-- Chocolate Cake Options
INSERT INTO option (id, name, quantity, product_id) VALUES (23, 'Extra Chocolate', 15, 15);
INSERT INTO option (id, name, quantity, product_id) VALUES (24, 'Berry Sauce', 20, 15);
INSERT INTO option (id, name, quantity, product_id) VALUES (25, 'Whipped Cream', 10, 15);
-- Glazed Donut Options
INSERT INTO option (id, name, quantity, product_id) VALUES (26, 'Extra Glaze', 25, 20);
INSERT INTO option (id, name, quantity, product_id) VALUES (27, 'Sprinkles', 30, 20);
INSERT INTO option (id, name, quantity, product_id) VALUES (28, 'Chocolate Drizzle', 20, 20);
