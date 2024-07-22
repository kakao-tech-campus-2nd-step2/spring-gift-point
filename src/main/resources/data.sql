-- categories
INSERT INTO categories (name, color, image_url, description) VALUES ('교환권', '#6c95d1', 'https://', 'ㅁ');
INSERT INTO categories (name, color, image_url, description) VALUES ('식품', '#ff5733', 'https://', '다양한 식품 교환권');
INSERT INTO categories (name, color, image_url, description) VALUES ('음료', '#33ff57', 'https://', '다양한 음료 교환권');
INSERT INTO categories (name, color, image_url, description) VALUES ('의류', '#3357ff', 'https://', '다양한 의류 교환권');
INSERT INTO categories (name, color, image_url, description) VALUES ('전자제품', '#ff33a6', 'https://', '다양한 전자제품 교환권');


-- products
INSERT INTO products (name, price, image_url, category_id) VALUES ('test1', 10000, 'http://', 1);
INSERT INTO products (name, price, image_url, category_id) VALUES ('test2', 20000, 'http://', 1);
INSERT INTO products (name, price, image_url, category_id) VALUES ('test3', 20000, 'http://', 1);
INSERT INTO products (name, price, image_url, category_id) VALUES ('test4', 20000, 'http://', 1);
INSERT INTO products (name, price, image_url, category_id) VALUES ('test5', 20000, 'http://', 1);
INSERT INTO products (name, price, image_url, category_id) VALUES ('test6', 20000, 'http://', 1);
INSERT INTO products (name, price, image_url, category_id) VALUES ('test7', 20000, 'http://', 1);
INSERT INTO products (name, price, image_url, category_id) VALUES ('test8', 20000, 'http://', 1);
INSERT INTO products (name, price, image_url, category_id) VALUES ('test9', 20000, 'http://', 1);
INSERT INTO products (name, price, image_url, category_id) VALUES ('test10', 20000, 'http://', 1);
INSERT INTO products (name, price, image_url, category_id) VALUES ('test11', 20000, 'http://', 1);
INSERT INTO products (name, price, image_url, category_id) VALUES ('test12', 20000, 'http://', 1);
INSERT INTO products (name, price, image_url, category_id) VALUES ('test13', 20000, 'http://', 1);
INSERT INTO products (name, price, image_url, category_id) VALUES ('test14', 20000, 'http://', 1);
INSERT INTO products (name, price, image_url, category_id) VALUES ('test15', 20000, 'http://', 1);
INSERT INTO products (name, price, image_url, category_id) VALUES ('test16', 20000, 'http://', 1);
INSERT INTO products (name, price, image_url, category_id) VALUES ('test17', 20000, 'http://', 1);
INSERT INTO products (name, price, image_url, category_id) VALUES ('test18', 20000, 'http://', 1);
INSERT INTO products (name, price, image_url, category_id) VALUES ('test19', 20000, 'http://', 1);
INSERT INTO products (name, price, image_url, category_id) VALUES ('test20', 20000, 'http://', 1);
INSERT INTO products (name, price, image_url, category_id) VALUES ('test21', 20000, 'http://', 1);
INSERT INTO products (name, price, image_url, category_id) VALUES ('test22', 20000, 'http://', 1);
INSERT INTO products (name, price, image_url, category_id) VALUES ('test23', 20000, 'http://', 1);

-- options
INSERT INTO options (name, count, product_id) VALUES ('name1', 20000, 1);
INSERT INTO options (name, count, product_id) VALUES ('name2', 20000, 1);
INSERT INTO options (name, count, product_id) VALUES ('name3', 20000, 2);
INSERT INTO options (name, count, product_id) VALUES ('name4', 20000, 2);
INSERT INTO options (name, count, product_id) VALUES ('name5', 20000, 3);


-- members
INSERT INTO members (member_type, email, password, nickname) VALUES ('USER', 'email', 'password', 'nickname');
INSERT INTO members (member_type, email, password, nickname) VALUES ('USER', 'email1', 'password1', 'nickname1');
INSERT INTO members (member_type, email, password, nickname) VALUES ('USER', 'email2', 'password2', 'nickname2');

-- wishes
INSERT INTO wishes (member_id, product_id, product_count) VALUES (1, 1, 10);
INSERT INTO wishes (member_id, product_id, product_count) VALUES (1, 2, 10);
INSERT INTO wishes (member_id, product_id, product_count) VALUES (1, 3, 10);
INSERT INTO wishes (member_id, product_id, product_count) VALUES (1, 4, 10);
INSERT INTO wishes (member_id, product_id, product_count) VALUES (1, 5, 10);
INSERT INTO wishes (member_id, product_id, product_count) VALUES (1, 6, 10);
INSERT INTO wishes (member_id, product_id, product_count) VALUES (1, 7, 10);
INSERT INTO wishes (member_id, product_id, product_count) VALUES (1, 8, 10);
INSERT INTO wishes (member_id, product_id, product_count) VALUES (1, 9, 10);
INSERT INTO wishes (member_id, product_id, product_count) VALUES (1, 10, 10);
INSERT INTO wishes (member_id, product_id, product_count) VALUES (1, 11, 10);
INSERT INTO wishes (member_id, product_id, product_count) VALUES (1, 12, 10);
INSERT INTO wishes (member_id, product_id, product_count) VALUES (1, 13, 10);
INSERT INTO wishes (member_id, product_id, product_count) VALUES (1, 14, 10);
INSERT INTO wishes (member_id, product_id, product_count) VALUES (1, 15, 10);
INSERT INTO wishes (member_id, product_id, product_count) VALUES (1, 16, 10);
INSERT INTO wishes (member_id, product_id, product_count) VALUES (1, 17, 10);
INSERT INTO wishes (member_id, product_id, product_count) VALUES (1, 18, 10);
INSERT INTO wishes (member_id, product_id, product_count) VALUES (1, 19, 10);
INSERT INTO wishes (member_id, product_id, product_count) VALUES (1, 20, 10);
INSERT INTO wishes (member_id, product_id, product_count) VALUES (1, 21, 10);
INSERT INTO wishes (member_id, product_id, product_count) VALUES (1, 22, 10);
INSERT INTO wishes (member_id, product_id, product_count) VALUES (1, 23, 10);

INSERT INTO wishes (member_id, product_id, product_count) VALUES (2, 1, 10);
INSERT INTO wishes (member_id, product_id, product_count) VALUES (2, 2, 10);


