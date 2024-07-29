INSERT INTO CATEGORIES (name, color, image_url, description)
values ('교환권',
        'color1',
        '이미지URL1',
        '설명-교환권');


INSERT INTO CATEGORIES (name, color, image_url, description)
values ('상품권',
        'color2',
        '이미지URL2',
        '설명-상품권');


INSERT INTO CATEGORIES (name, color, image_url, description)
values ('뷰티',
        'color3',
        '이미지URL3',
        '설명-뷰티');


INSERT INTO CATEGORIES (name, color, image_url, description)
values ('패션',
        'color4',
        '이미지URL4',
        '설명-패션');


INSERT INTO CATEGORIES (name, color, image_url, description)
values ('식품',
        'color5',
        '이미지URL5',
        '설명-식품');


INSERT INTO CATEGORIES (name, color, image_url, description)
values ('리빙/도서',
        'color6',
        '이미지URL6',
        '설명-리빙/도서');


INSERT INTO CATEGORIES (name, color, image_url, description)
values ('레저/스포츠',
        'color7',
        '이미지URL7',
        '설명-레저/스포츠');


INSERT INTO CATEGORIES (name, color, image_url, description)
values ('아티스트/캐릭터',
        'color8',
        '이미지URL8',
        '설명-아티스트/캐릭터');


INSERT INTO CATEGORIES (name, color, image_url, description)
values ('유아동/반려',
        'color9',
        '이미지URL9',
        '설명-유아동/반려');


INSERT INTO CATEGORIES (name, color, image_url, description)
values ('디지털/가전',
        'color10',
        '이미지URL10',
        '설명-디지털/가전');


INSERT INTO CATEGORIES (name, color, image_url, description)
values ('카카오프렌즈',
        'color11',
        '이미지URL11',
        '설명-카카오프렌즈');


INSERT INTO CATEGORIES (name, color, image_url, description)
values ('트렌드 선물',
        'color12',
        '이미지URL12',
        '설명-트렌드 선물');


INSERT INTO CATEGORIES (name, color, image_url, description)
values ('백화점',
        'color13',
        '이미지URL13',
        '설명-백화점');


INSERT INTO PRODUCTS (category_id, name, price, image_url)
values (1,
        '아메리카노',
        5000,
        '이미지URL.....');


INSERT INTO PRODUCTS (category_id, name, price, image_url)
values (2,
        '카페라떼',
        6000,
        '이미지URL2.....');


INSERT INTO PRODUCTS (category_id, name, price, image_url)
values (3,
        '바닐라라떼',
        6000,
        '이미지URL3.....');


INSERT INTO USERS (email, nickname, password, user_type)
values ('yoonsu0325@gmail.com',
        'yoonsu0325',
        'thisIsPassword1234',
        'normal_user');

INSERT INTO OPTIONS (name, quantity, product_id)
values ('Tall',
        100,
        1);

INSERT INTO OPTIONS (name, quantity, product_id)
values ('Tall',
        20,
        2);

INSERT INTO OPTIONS (name, quantity, product_id)
values ('Grande',
        30,
        2);

INSERT INTO OPTIONS (name, quantity, product_id)
values ('Venti',
        120,
        3);


