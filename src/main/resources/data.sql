INSERT INTO category (name, color, image_url, description)
VALUES ('Electronics', 'Blue', 'http://example.com/electronics.jpg', 'Various electronic items'),
       ('Books', 'Green', 'http://example.com/books.jpg', 'A collection of books'),
       ('Clothing', 'Red', 'http://example.com/clothing.jpg', 'Fashionable clothing items');

INSERT INTO product (name, price, image_url, category_id)
VALUES ('Smartphone', 699, 'http://example.com/smartphone.jpg', 1),
       ('Laptop', 1199, 'http://example.com/laptop.jpg', 1),
       ('Java Programming', 39, 'http://example.com/java-book.jpg', 2),
       ('Spring Boot Guide', 29, 'http://example.com/spring-boot.jpg', 2),
       ('T-Shirt', 19, 'http://example.com/tshirt.jpg', 3),
       ('Jeans', 49, 'http://example.com/jeans.jpg', 3);

INSERT INTO option (name, quantity, product_id)
VALUES ('128GB Storage', 50, 1),
       ('256GB Storage', 30, 1),
       ('16GB RAM', 20, 2),
       ('32GB RAM', 10, 2),
       ('Large', 100, 5),
       ('Medium', 150, 5),
       ('32 Waist', 60, 6),
       ('34 Waist', 40, 6);

INSERT INTO users (email, password, kakao_access_token, role, point)
VALUES ('user1@example.com', 'password1', 'token1', 'ROLE_USER', 10000),
       ('user2@example.com', 'password2', 'token2', 'ROLE_USER', 0),
       ('admin@example.com', 'adminpassword', 'admintoken', 'ROLE_ADMIN', 300);

INSERT INTO wish (user_id, product_id, count)
VALUES (1, 1, 1),
       (1, 2, 2),
       (2, 3, 1),
       (2, 4, 2),
       (3, 5, 3);