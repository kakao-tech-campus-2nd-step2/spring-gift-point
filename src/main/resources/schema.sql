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
    category_id LONG
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
    quantity INT
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
    product_id LONG
);
CREATE TABLE orders(
    id LONG AUTO_INCREMENT PRIMARY KEY,
    user_id LONG,
    option_id LONG,
    product_id LONG,
    quantity INT,
    message VARCHAR(255),
    order_date_time TIMESTAMP
);