CREATE TABLE IF NOT EXISTS category (
                                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                        name VARCHAR(255) NOT NULL UNIQUE
    );

CREATE TABLE IF NOT EXISTS product (
                                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                       name VARCHAR(255) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    image_url VARCHAR(255),
    category_id BIGINT NOT NULL,
    CONSTRAINT fk_category FOREIGN KEY (category_id) REFERENCES category(id)
    );

CREATE TABLE IF NOT EXISTS members (
                                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                       email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
    );

CREATE TABLE IF NOT EXISTS wishes (
                                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                      member_id BIGINT NOT NULL,
                                      product_name VARCHAR(255) NOT NULL,
    CONSTRAINT fk_member FOREIGN KEY (member_id) REFERENCES members(id)
    );

CREATE TABLE IF NOT EXISTS kakaoProduct (
                                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                            name VARCHAR(255) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    image_url VARCHAR(255)
    );

CREATE TABLE IF NOT EXISTS option (
                                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                      name VARCHAR(50) NOT NULL,
    quantity INT NOT NULL,
    product_id BIGINT NOT NULL,
    CONSTRAINT fk_product FOREIGN KEY (product_id) REFERENCES product(id),
    CONSTRAINT unique_product_option UNIQUE (product_id, name)
    );

CREATE TABLE IF NOT EXISTS kakaoToken (
                                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                            userEmail VARCHAR(255) NOT NULL UNIQUE,
    token VARCHAR(255) NOT NULL
    );