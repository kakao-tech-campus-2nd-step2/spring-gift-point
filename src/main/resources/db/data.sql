insert into category(id,name,color,description,image_url) values(2920,'생일','#5949A3','감동을 높여줄 생일 선물 리스트','https://img1.daumcdn.net/thumb/S104x104/?fname=https%3A%2F%2Ft1.daumcdn.net%2Fgift%2Fhome%2Ftheme%2F292020231106_MXMUB.png');
insert into category(id,name,color,description,image_url) values(2930, '교환권','#9290C3','놓치면 후회할 교환권 특가','https://img1.daumcdn.net/thumb/S104x104/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20240131153049_5a22b137a8d346e9beb020a7a7f4254a.jpg');

insert into product(id, name, price, category_id, image_url) values (3245119, '[단독각인] 피렌체 1221 에디션 오드코롱 50ml (13종 택1)',145000, 2920, 'https://st.kakaocdn.net/product/gift/product/20240215083306_8e1db057580145829542463a84971ae3.png');
insert into product(id, name, price, category_id, image_url) values (2263833, '외식 통합권 10만원권',100000, 2930, 'https://st.kakaocdn.net/product/gift/product/20200513102805_4867c1e4a7ae43b5825e9ae14e2830e3.png');
insert into product(id, name, price, category_id, image_url) values (6502823, '[선물포장/미니퍼퓸증정] 디켄터 리드 디퓨저 300ml + 메세지카드',108000, 2920,  'https://st.kakaocdn.net/product/gift/product/20240215112140_11f857e972bc4de6ac1d2f1af47ce182.jpg');
insert into product(id, name, price, category_id, image_url) values (1181831, '[선물포장] 소바쥬 오 드 뚜왈렛 60ML', 122000, 2920, 'https://st.kakaocdn.net/product/gift/product/20240214150740_ad25267defa64912a7c030a7b57dc090.jpg');
insert into product(id, name, price, category_id, image_url) values (1379982, '[정관장] 홍삼정 에브리타임 리미티드 (10ml x 30포)', 133000, 2920, 'https://st.kakaocdn.net/product/gift/product/20240118135914_a6e1a7442ea04aa49add5e02ed62b4c3.jpg');

insert into option(id,name,quantity,product_id) values(1,'Option A',10,3245119);
insert into option(id,name,quantity,product_id) values(2,'Option A2',11,3245119);
insert into option(id,name,quantity,product_id) values(3,'Option B',20,2263833);
insert into option(id,name,quantity,product_id) values(4,'Option C',5,2263833);
insert into option(id,name,quantity,product_id) values(5,'Option D',15,1181831);
insert into option(id,name,quantity,product_id) values(6,'Option E',12,1379982);
insert into option(id,name,quantity,product_id) values(7,'Option E',12,1379982);

insert into user_tb(email,password) values(test,test);


