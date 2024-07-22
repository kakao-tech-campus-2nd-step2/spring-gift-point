-- 예시 카테고리 데이터
INSERT INTO category (name, color, image_url, description)
VALUES ('교환권', '#F15F5F', 'https://via.placeholder.com/150?text=category1', 'description1'),
       ('상품권', '#F29661', 'https://via.placeholder.com/150?text=category2', 'description2'),
       ('뷰티', '#F2CB61', 'https://via.placeholder.com/150?text=category3', 'description3'),
       ('식품', '#E5D85C', 'https://via.placeholder.com/150?text=category4', 'description4'),
       ('리빙/도서', '#BCE55C', 'https://via.placeholder.com/150?text=category5', 'description5'),
       ('레저/스포츠', '#86E57F', 'https://via.placeholder.com/150?text=category6', 'description6'),
       ('아티스트/캐릭터', '#5CD1E5', 'https://via.placeholder.com/150?text=category7', 'description7'),
       ('유아동/반려', '#6799FF', 'https://via.placeholder.com/150?text=category8', 'description8'),
       ('디지털/가전', '#6B66FF', 'https://via.placeholder.com/150?text=category9', 'description9'),
       ('카카오프렌즈', '#A566FF', 'https://via.placeholder.com/150?text=category10', 'description10'),
       ('트렌드 선물', '#F361DC', 'https://via.placeholder.com/150?text=category11', 'description11'),
       ('백화점', '#A6A6A6', 'https://via.placeholder.com/150?text=category12', 'description12');

-- 예시 상품 데이터
INSERT INTO product (name, price, image_url, category_id)
VALUES ('Product1', 1000, 'https://via.placeholder.com/150?text=product1', 1),
       ('Product2', 2000, 'https://via.placeholder.com/150?text=product2', 2),
       ('Product3', 3000, 'https://via.placeholder.com/150?text=product3', 3),
       ('Product4', 4000, 'https://via.placeholder.com/150?text=product4', 4),
       ('Product5', 5000, 'https://via.placeholder.com/150?text=product5', 5),
       ('Product6', 6000, 'https://via.placeholder.com/150?text=product6', 6),
       ('Product7', 7000, 'https://via.placeholder.com/150?text=product7', 7),
       ('Product8', 8000, 'https://via.placeholder.com/150?text=product8', 8),
       ('Product9', 9000, 'https://via.placeholder.com/150?text=product9', 9),
       ('Product10', 10000, 'https://via.placeholder.com/150?text=product10', 10),
       ('Product11', 11000, 'https://via.placeholder.com/150?text=product11', 11);

-- 예시 회원 데이터
INSERT INTO member (email, password)
VALUES ('cussle@kakao.com', 'admin'),
       ('tester@kakao.com', 'test');

-- 예시 위시 리스트 데이터
INSERT INTO wish (member_id, product_id)
VALUES (1, 1),
       (1, 2),
       (2, 1),
       (2, 2),
       (2, 6),
       (2, 7),
       (2, 10),
       (2, 11);
