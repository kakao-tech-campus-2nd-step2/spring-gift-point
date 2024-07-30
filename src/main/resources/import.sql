insert into category(name, color, image_url, description) values('상품', 'red', 'https://st.kakaocdn.net/category1.jpg', '상품 카테고리입니다.')
insert into category(name, color, image_url, description) values('상품권', 'red', 'https://st.kakaocdn.net/category1.jpg', '상품권 카테고리입니다.')
insert into category(name, color, image_url, description) values('패션', 'blue', 'https://st.kakaocdn.net/category2.jpg', '패션 카테고리입니다.')
insert into category(name, color, image_url, description) values('뷰티', 'green', 'https://st.kakaocdn.net/category3.jpg', '뷰티 카테고리입니다.')

insert into product(name, price, image_url, category_id) values ('product1', 1000, 'https://st.kakaocdn.net/product1.jpg', 2)
insert into product(name, price, image_url, category_id) values ('product2', 2000, 'https://st.kakaocdn.net/product2.jpg', 3)
insert into product(name, price, image_url, category_id) values ('product3', 3000, 'https://st.kakaocdn.net/product3.jpg', 4)
insert into product(name, price, image_url, category_id) values ('product4', 3000, 'https://st.kakaocdn.net/product4.jpg', 2)
insert into product(name, price, image_url, category_id) values ('product5', 3000, 'https://st.kakaocdn.net/product5.jpg', 3)
insert into product(name, price, image_url, category_id) values ('product6', 3000, 'https://st.kakaocdn.net/product6.jpg', 4)

insert into users(password, email, social_Type) values ('yso3865', 'yso829612@gmail.com', 'DEFAULT')

insert into wish(user_id, product_id) values(1, 1)
insert into wish(user_id, product_id) values(1, 2)
insert into wish(user_id, product_id) values(1, 3)
insert into wish(user_id, product_id) values(1, 4)

insert into options(name, quantity, product_id) values('option1', 100, 1)
insert into options(name, quantity, product_id) values('option2', 100, 2)
