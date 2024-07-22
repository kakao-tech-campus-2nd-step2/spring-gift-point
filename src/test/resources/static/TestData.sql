INSERT INTO product (deleted, price, img_url, name)
VALUES (0, 100, 'http://example.com/img1.jpg', 'Product 1'),
       (0, 200, 'http://example.com/img2.jpg', 'Product 2'),
       (0, 300, 'http://example.com/img3.jpg', 'Product 3'),
       (0, 400, 'http://example.com/img4.jpg', 'Product 4'),
       (0, 500, 'http://example.com/img5.jpg', 'Product 5'),
       (0, 600, 'http://example.com/img6.jpg', 'Product 6'),
       (0, 700, 'http://example.com/img7.jpg', 'Product 7'),
       (0, 800, 'http://example.com/img8.jpg', 'Product 8'),
       (0, 900, 'http://example.com/img9.jpg', 'Product 9'),
       (0, 1000, 'http://example.com/img10.jpg', 'Product 10');

INSERT INTO member (password, username, role)
VALUES ('password1', 'user1', 'USER'),
       ('password2', 'user2', 'USER'),
       ('password3', 'user3', 'USER'),
       ('password4', 'user4', 'USER'),
       ('password5', 'user5', 'USER'),
       ('password6', 'user6', 'USER'),
       ('password7', 'user7', 'USER'),
       ('password8', 'user8', 'USER'),
       ('password9', 'user9', 'USER'),
       ('password10', 'user10', 'USER');
commit;
INSERT INTO wish (amount, deleted, product_id, member_id)
VALUES (1, 0, 1, 1),
       (2, 0, 2, 1),
       (3, 0, 3, 1),
       (4, 0, 4, 1),
       (5, 0, 5, 1),
       (6, 0, 6, 1),
       (7, 0, 7, 1),
       (8, 0, 8, 1),
       (9, 0, 9, 1),
       (10, 0, 10, 1);
