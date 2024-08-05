insert into CATEGORY (name,color,image_url,description) values ('교환권', 'color', 'http://a.com', 'description');
insert into CATEGORY (name,color,image_url,description) values ('상품권', 'color', 'http://a.com', 'description');
insert into CATEGORY (name,color,image_url,description) values ('뷰티', 'color', 'http://a.com', 'description');
insert into CATEGORY (name,color,image_url,description) values ('패션', 'color', 'http://a.com', 'description');
insert into CATEGORY (name,color,image_url,description) values ('식품', 'color', 'http://a.com', 'description');
insert into CATEGORY (name,color,image_url,description) values ('리빙/도서', 'color', 'http://a.com', 'description');
insert into CATEGORY (name,color,image_url,description) values ('레저/스포츠', 'color', 'http://a.com', 'description');
insert into CATEGORY (name,color,image_url,description) values ('아티스트/캐릭터', 'color', 'http://a.com', 'description');
insert into CATEGORY (name,color,image_url,description) values ('유아동/반려', 'color', 'http://a.com', 'description');
insert into CATEGORY (name,color,image_url,description) values ('카카오프렌즈', 'color', 'http://a.com', 'description');

insert into PRODUCTS (name, price, image_url, category_id) values ('productA', 1000, 'http://a.com', 1L);
insert into PRODUCTS (name, price, image_url, category_id) values ('productB', 5000, 'http://b.com', 2L);
insert into PRODUCTS (name, price, image_url, category_id) values ('productC', 10000, 'http://c.com', 3L);
insert into PRODUCTS (name, price, image_url, category_id) values ('productD', 50000, 'http://d.com', 4L);
insert into PRODUCTS (name, price, image_url, category_id) values ('productE', 3000, 'http://e.com', 5L);

insert into OPTIONS (name, quantity, product_id, version) values ('productA_optionA', 100, 1L, 0);
insert into OPTIONS (name, quantity, product_id, version) values ('productA_optionB', 500, 1L, 0);
insert into OPTIONS (name, quantity, product_id, version) values ('productA_optionC', 999, 1L, 0);
insert into OPTIONS (name, quantity, product_id, version) values ('productB_optionA', 1, 2L, 0);
insert into OPTIONS (name, quantity, product_id, version) values ('productC_optionA', 100000000, 3L, 0);
insert into OPTIONS (name, quantity, product_id, version) values ('productD_optionA', 10, 4L, 0);
insert into OPTIONS (name, quantity, product_id, version) values ('productE_optionA', 232324, 5L, 0);

INSERT INTO MEMBER(email,password,role,point) VALUES ('aaa123@a.com', '1234', 'ROLE_ADMIN', 1000);
INSERT INTO MEMBER(email,password,role,point) VALUES ('bbb123@b.com', '1234', 'ROLE_USER', 0);
INSERT INTO MEMBER(email,password,role,point) VALUES ('ccc123@c.com', '1234', 'ROLE_USER', 0);
INSERT INTO MEMBER(email,password,role,point) VALUES ('ddd123@d.com', '1234', 'ROLE_USER', 0);

INSERT INTO WISH(member_id, product_id, quantity) VALUES (1, 1, 1);
INSERT INTO WISH(member_id, product_id, quantity) VALUES (1, 2, 1);
INSERT INTO WISH(member_id, product_id, quantity) VALUES (1, 3, 1);
INSERT INTO WISH(member_id, product_id, quantity) VALUES (1, 4, 1);
INSERT INTO WISH(member_id, product_id, quantity) VALUES (1, 5, 1);
-- INSERT INTO WISH(member_id, product_id) VALUES (1, 6);
-- INSERT INTO WISH(member_id, product_id) VALUES (1, 7);
-- INSERT INTO WISH(member_id, product_id) VALUES (1, 8);
-- INSERT INTO WISH(member_id, product_id) VALUES (1, 9);
-- INSERT INTO WISH(member_id, product_id) VALUES (1, 10);
-- INSERT INTO WISH(member_id, product_id) VALUES (1, 11);
-- INSERT INTO WISH(member_id, product_id) VALUES (1, 12);
-- INSERT INTO WISH(member_id, product_id) VALUES (1, 13);
-- INSERT INTO WISH(member_id, product_id) VALUES (1, 14);
-- INSERT INTO WISH(member_id, product_id) VALUES (1, 15);
-- INSERT INTO WISH(member_id, product_id) VALUES (1, 16);
-- INSERT INTO WISH(member_id, product_id) VALUES (1, 17);
