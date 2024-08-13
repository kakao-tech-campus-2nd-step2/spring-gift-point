INSERT INTO category (name, color, image_url, description)
VALUES ('교환권', '#00FF00', '예시1 Image Url', ''),
       ('백화점', '#00FF00', '예시1 Image Url', '');

INSERT INTO product (name, price, category_id, image_url)
VALUES ('예시1', '100', 1, '예시1 Image Url'),
       ('예시2', '200', 2, '예시2 Image Url');

INSERT INTO member (email, password, role, points)
VALUES ('admin@email.com', 'password', 'admin', 10000);
INSERT INTO member (email, password, role, points)
VALUES ('member@email.com', 'password', 'user', 100);
INSERT INTO member (email, password, role, points)
VALUES ('kakaouser@email.com', 'password', 'kakaouser', 1000);

INSERT INTO options (name, quantity, product_id)
VALUES ('임시 옵션1', 1, 1),
       ('임시 옵션2', 1, 1),
       ('임시 옵션1', 1, 2),
       ('임시 옵션2', 1, 2)