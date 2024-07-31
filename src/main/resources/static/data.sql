-- Category 데이터 삽입
INSERT INTO `category` (`name`) VALUES ('Electronics');
INSERT INTO `category` (`name`) VALUES ('Books');
INSERT INTO `category` (`name`) VALUES ('Clothing');
INSERT INTO `category` (`name`) VALUES ('Toys');
INSERT INTO `category` (`name`) VALUES ('Furniture');

--- Options 데이터 삽입,

-- User 데이터 삽입
INSERT INTO `users` (`email`, `password`, `roles`) VALUES ('user1@example.com', 'password1', 'USER');
INSERT INTO `users` (`email`, `password`, `roles`) VALUES ('user2@example.com', 'password2', 'USER');
INSERT INTO `users` (`email`, `password`, `roles`) VALUES ('admin@example.com', 'adminpassword', 'ADMIN');
INSERT INTO `users` (`email`, `password`, `roles`) VALUES ('manager@example.com', 'managerpassword', 'ADMIN');
INSERT INTO `users` (`email`, `password`, `roles`) VALUES ('guest@example.com', 'guestpassword', 'GUEST');

-- Product 데이터 삽입
INSERT INTO `product` (`name`, `price`, `image_url`, `category_id`) VALUES ('Product 1', 1000, 'http://example.com/images/product1.jpg', 1);
INSERT INTO `product` (`name`, `price`, `image_url`, `category_id`) VALUES ('Product 2', 2000, 'http://example.com/images/product2.jpg', 2);
INSERT INTO `product` (`name`, `price`, `image_url`, `category_id`) VALUES ('Product 3', 3000, 'http://example.com/images/product3.jpg', 3);
INSERT INTO `product` (`name`, `price`, `image_url`, `category_id`) VALUES ('Product 4', 4000, 'http://example.com/images/product4.jpg', 4);
INSERT INTO `product` (`name`, `price`, `image_url`, `category_id`) VALUES ('Product 5', 5000, 'http://example.com/images/product5.jpg', 5);
INSERT INTO `product` (`name`, `price`, `image_url`, `category_id`) VALUES ('Product 6', 6000, 'http://example.com/images/product6.jpg', 1);
INSERT INTO `product` (`name`, `price`, `image_url`, `category_id`) VALUES ('Product 7', 7000, 'http://example.com/images/product7.jpg', 2);
INSERT INTO `product` (`name`, `price`, `image_url`, `category_id`) VALUES ('Product 8', 8000, 'http://example.com/images/product8.jpg', 3);
INSERT INTO `product` (`name`, `price`, `image_url`, `category_id`) VALUES ('Product 9', 9000, 'http://example.com/images/product9.jpg', 4);
INSERT INTO `product` (`name`, `price`, `image_url`, `category_id`) VALUES ('Product 10', 10000, 'http://example.com/images/product10.jpg', 5);
INSERT INTO `product` (`name`, `price`, `image_url`, `category_id`) VALUES ('Product 11', 11000, 'http://example.com/images/product11.jpg', 1);
INSERT INTO `product` (`name`, `price`, `image_url`, `category_id`) VALUES ('Product 12', 12000, 'http://example.com/images/product12.jpg', 2);
INSERT INTO `product` (`name`, `price`, `image_url`, `category_id`) VALUES ('Product 13', 13000, 'http://example.com/images/product13.jpg', 3);
INSERT INTO `product` (`name`, `price`, `image_url`, `category_id`) VALUES ('Product 14', 14000, 'http://example.com/images/product14.jpg', 4);
INSERT INTO `product` (`name`, `price`, `image_url`, `category_id`) VALUES ('Product 15', 15000, 'http://example.com/images/product15.jpg', 5);
INSERT INTO `product` (`name`, `price`, `image_url`, `category_id`) VALUES ('Product 16', 16000, 'http://example.com/images/product16.jpg', 1);
INSERT INTO `product` (`name`, `price`, `image_url`, `category_id`) VALUES ('Product 17', 17000, 'http://example.com/images/product17.jpg', 2);
INSERT INTO `product` (`name`, `price`, `image_url`, `category_id`) VALUES ('Product 18', 18000, 'http://example.com/images/product18.jpg', 3);
INSERT INTO `product` (`name`, `price`, `image_url`, `category_id`) VALUES ('Product 19', 19000, 'http://example.com/images/product19.jpg', 4);
INSERT INTO `product` (`name`, `price`, `image_url`, `category_id`) VALUES ('Product 20', 20000, 'http://example.com/images/product20.jpg', 5);
INSERT INTO `product` (`name`, `price`, `image_url`, `category_id`) VALUES ('Product 21', 21000, 'http://example.com/images/product21.jpg', 1);
INSERT INTO `product` (`name`, `price`, `image_url`, `category_id`) VALUES ('Product 22', 22000, 'http://example.com/images/product22.jpg', 2);
INSERT INTO `product` (`name`, `price`, `image_url`, `category_id`) VALUES ('Product 23', 23000, 'http://example.com/images/product23.jpg', 3);
INSERT INTO `product` (`name`, `price`, `image_url`, `category_id`) VALUES ('Product 24', 24000, 'http://example.com/images/product24.jpg', 4);
INSERT INTO `product` (`name`, `price`, `image_url`, `category_id`) VALUES ('Product 25', 25000, 'http://example.com/images/product25.jpg', 5);
INSERT INTO `product` (`name`, `price`, `image_url`, `category_id`) VALUES ('Product 26', 26000, 'http://example.com/images/product26.jpg', 1);
INSERT INTO `product` (`name`, `price`, `image_url`, `category_id`) VALUES ('Product 27', 27000, 'http://example.com/images/product27.jpg', 2);
INSERT INTO `product` (`name`, `price`, `image_url`, `category_id`) VALUES ('Product 28', 28000, 'http://example.com/images/product28.jpg', 3);
INSERT INTO `product` (`name`, `price`, `image_url`, `category_id`) VALUES ('Product 29', 29000, 'http://example.com/images/product29.jpg', 4);
INSERT INTO `product` (`name`, `price`, `image_url`, `category_id`) VALUES ('Product 30', 30000, 'http://example.com/images/product30.jpg', 5);
INSERT INTO `product` (`name`, `price`, `image_url`, `category_id`) VALUES ('Product 31', 31000, 'http://example.com/images/product31.jpg', 1);
-- Product 데이터 삽입
INSERT INTO product (name, price, image_url, category_id) VALUES ('Product 32', 32000, 'http://example.com/images/product32.jpg', 2);
INSERT INTO product (name, price, image_url, category_id) VALUES ('Product 33', 33000, 'http://example.com/images/product33.jpg', 3);
INSERT INTO product (name, price, image_url, category_id) VALUES ('Product 34', 34000, 'http://example.com/images/product34.jpg', 4);
INSERT INTO product (name, price, image_url, category_id) VALUES ('Product 35', 35000, 'http://example.com/images/product35.jpg', 5);
INSERT INTO product (name, price, image_url, category_id) VALUES ('Product 36', 36000, 'http://example.com/images/product36.jpg', 1);
INSERT INTO product (name, price, image_url, category_id) VALUES ('Product 37', 37000, 'http://example.com/images/product37.jpg', 2);
INSERT INTO product (name, price, image_url, category_id) VALUES ('Product 38', 38000, 'http://example.com/images/product38.jpg', 3);

-- WishProduct 데이터 삽입
INSERT INTO wish_product (member_id, product_id) VALUES (1, 1);
INSERT INTO wish_product (member_id, product_id) VALUES (1, 2);
INSERT INTO wish_product (member_id, product_id) VALUES (1, 3);
INSERT INTO wish_product (member_id, product_id) VALUES (1, 4);
INSERT INTO wish_product (member_id, product_id) VALUES (1, 5);
INSERT INTO wish_product (member_id, product_id) VALUES (2, 6);
INSERT INTO wish_product (member_id, product_id) VALUES (2, 7);
INSERT INTO wish_product (member_id, product_id) VALUES (2, 8);
INSERT INTO wish_product (member_id, product_id) VALUES (2, 9);
INSERT INTO wish_product (member_id, product_id) VALUES (2, 10);
INSERT INTO wish_product (member_id, product_id) VALUES (3, 11);
INSERT INTO wish_product (member_id, product_id) VALUES (3, 12);
INSERT INTO wish_product (member_id, product_id) VALUES (3, 13);
INSERT INTO wish_product (member_id, product_id) VALUES (3, 14);
INSERT INTO wish_product (member_id, product_id) VALUES (3, 15);
INSERT INTO wish_product (member_id, product_id) VALUES (4, 16);
INSERT INTO wish_product (member_id, product_id) VALUES (4, 17);
INSERT INTO wish_product (member_id, product_id) VALUES (4, 18);
INSERT INTO wish_product (member_id, product_id) VALUES (4, 19);
INSERT INTO wish_product (member_id, product_id) VALUES (4, 20);
