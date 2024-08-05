CREATE TABLE IF NOT EXISTS category (
                                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                        name VARCHAR(255) NOT NULL UNIQUE,
    color VARCHAR(255),
    image_url VARCHAR(255),
    description VARCHAR(255)
    );

CREATE TABLE IF NOT EXISTS member (
                                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                      email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    active_token VARCHAR(255),
    points INTEGER NOT NULL DEFAULT 0
    );

CREATE TABLE IF NOT EXISTS product (
                                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                       name VARCHAR(255) NOT NULL,
    price INTEGER NOT NULL,
    image_url VARCHAR(255),
    category_id BIGINT NOT NULL,
    CONSTRAINT fk_product_category FOREIGN KEY (category_id) REFERENCES category(id)
    );

CREATE TABLE IF NOT EXISTS option (
                                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                      name VARCHAR(255) NOT NULL,
    quantity INTEGER NOT NULL,
    product_id BIGINT,
    CONSTRAINT fk_option_product FOREIGN KEY (product_id) REFERENCES product(id)
    );

CREATE TABLE IF NOT EXISTS wishlist (
                                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                        member_id BIGINT NOT NULL,
                                        product_id BIGINT NOT NULL,
                                        CONSTRAINT fk_wishlist_member FOREIGN KEY (member_id) REFERENCES member(id),
    CONSTRAINT fk_wishlist_product FOREIGN KEY (product_id) REFERENCES product(id)
    );
