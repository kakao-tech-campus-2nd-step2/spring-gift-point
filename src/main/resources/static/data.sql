-- Category 데이터 삽입
INSERT INTO `category` (`name`,`image_url`) VALUES ('노트북','https://img.danawa.com/prod_img/500000/921/799/img/13799921_1.jpg?shrink=330:*&_v=20211210141001');
--- Options 데이터 삽입,

-- User 데이터 삽입
INSERT INTO `users` (`email`, `password`, `roles`,`point`) VALUES ('user1@example.com', 'password1', 'USER', 1000000);
INSERT INTO `users` (`email`, `password`, `roles`, `point`) VALUES ('user2@example.com', 'password2', 'USER', 2000000);
INSERT INTO `users` (`email`, `password`, `roles`,point) VALUES ('admin@example.com', 'adminpassword', 'ADMIN',300000);
INSERT INTO `users` (`email`, `password`, `roles`,point) VALUES ('manager@example.com', 'managerpassword', 'ADMIN',100);
INSERT INTO `users` (`email`, `password`, `roles`,point) VALUES ('guest@example.com', 'guestpassword', 'GUEST',0);

-- Product 데이터 삽입
INSERT INTO `product` (`name`, `price`, `image_url`, `category_id`) VALUES ('노트북 1', 1000, 'https://img.danawa.com/prod_img/500000/921/799/img/13799921_1.jpg?shrink=330:*&_v=20211210141001', 1);
INSERT INTO `product` (`name`, `price`, `image_url`, `category_id`) VALUES ('노트북 2', 2000, 'https://shop-phinf.pstatic.net/20240411_141/1712813506817dK9qK_JPEG/40116190695957272_489172248.jpg?type=m510', 1);

----options 데이터 삽입
INSERT INTO `Option` (`name`, `quantity`, `product_id`) VALUES ('Option 1', 100, 1);
INSERT INTO `Option` (`name`, `quantity`, `product_id`) VALUES ('Option 2', 200, 1);
INSERT INTO `Option` (`name`, `quantity`, `product_id`) VALUES ('Option 1', 150, 2);
INSERT INTO `Option` (`name`, `quantity`, `product_id`) VALUES ('Option 2', 250, 2);

