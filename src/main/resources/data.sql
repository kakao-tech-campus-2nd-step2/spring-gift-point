insert into CATEGORY (name) values ('교환권');
insert into CATEGORY (name) values ('상품권');
insert into CATEGORY (name) values ('뷰티');
insert into CATEGORY (name) values ('패션');
insert into CATEGORY (name) values ('식품');
insert into CATEGORY (name) values ('리빙/도서');
insert into CATEGORY (name) values ('레저/스포츠');
insert into CATEGORY (name) values ('아티스트/캐릭터');
insert into CATEGORY (name) values ('유아동/반려');
insert into CATEGORY (name) values ('카카오프렌즈');

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

INSERT INTO MEMBER(email,password,role) VALUES ('aaa123@a.com', '1234', 'ROLE_ADMIN');
INSERT INTO MEMBER(email,password,role) VALUES ('bbb123@b.com', '1234', 'ROLE_USER');
INSERT INTO MEMBER(email,password,role) VALUES ('ccc123@c.com', '1234', 'ROLE_USER');
INSERT INTO MEMBER(email,password,role) VALUES ('ddd123@d.com', '1234', 'ROLE_USER');

INSERT INTO WISH(member_id, product_id) VALUES (1, 1);
INSERT INTO WISH(member_id, product_id) VALUES (1, 2);
INSERT INTO WISH(member_id, product_id) VALUES (1, 3);
INSERT INTO WISH(member_id, product_id) VALUES (1, 4);
INSERT INTO WISH(member_id, product_id) VALUES (1, 5);
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
