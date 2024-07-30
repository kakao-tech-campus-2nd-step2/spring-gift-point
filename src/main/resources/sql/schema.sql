DROP TABLE IF EXISTS product;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS wishlist;
DROP TABLE IF EXISTS category;
DROP TABLE IF EXISTS option;
DROP TABLE IF EXISTS orders;
CREATE TABLE product (
    id LONG AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    price INT,
    image_url VARCHAR(255),
    category_id LONG,
    FOREIGN KEY (category_id) REFERENCES category(id) ON DELETE CASCADE
);
CREATE TABLE users (
    id LONG AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    email VARCHAR(255) ,
    password VARCHAR(255),
    role VARCHAR(255)
);
CREATE TABLE wishlist (
    id LONG AUTO_INCREMENT PRIMARY KEY,
    user_id LONG NOT NULL,
    product_id LONG NOT NULL,
    quantity INT,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES product(id) ON DELETE CASCADE
);
CREATE TABLE category (
    id LONG AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL ,
    color VARCHAR(255),
    image_url VARCHAR(255),
    description VARCHAR(255)
);
CREATE TABLE option (
    id LONG AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) UNIQUE,
    quantity INT,
    product_id LONG,
    FOREIGN KEY (product_id) REFERENCES product(id) ON DELETE CASCADE
);
CREATE TABLE orders(
    id LONG AUTO_INCREMENT PRIMARY KEY,
    user_id LONG,
    option_id LONG,
    product_id LONG,
    quantity INT,
    message VARCHAR(255),
    order_date_time TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES product(id) ON DELETE CASCADE,
    FOREIGN KEY (option_id) REFERENCES option(id) ON DELETE CASCADE
);

