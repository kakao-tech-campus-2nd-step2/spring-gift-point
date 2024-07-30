DROP TABLE IF EXISTS wish;
DROP TABLE IF EXISTS product_option;
DROP TABLE IF EXISTS product;
DROP TABLE IF EXISTS member;
DROP TABLE IF EXISTS category;
DROP TABLE IF EXISTS orders;

CREATE TABLE category (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE product (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         name VARCHAR(15) NOT NULL,
                         price INT NOT NULL,
                         imageurl VARCHAR(1000),
                         category_id BIGINT NOT NULL,
                         FOREIGN KEY (category_id) REFERENCES category(id)
);

CREATE TABLE member (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        email VARCHAR(255) NOT NULL UNIQUE,
                        password VARCHAR(255) NOT NULL
);

CREATE TABLE product_option (
                                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                name VARCHAR(50) NOT NULL,
                                quantity INT NOT NULL,
                                product_id BIGINT NOT NULL,
                                FOREIGN KEY (product_id) REFERENCES product(id),
                                UNIQUE (product_id, name)
);

CREATE TABLE wish (
                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                      member_id BIGINT NOT NULL,
                      product_id BIGINT NOT NULL,
                      product_option_id BIGINT NOT NULL,
                      FOREIGN KEY (member_id) REFERENCES member(id),
                      FOREIGN KEY (product_id) REFERENCES product(id),
                      FOREIGN KEY (product_option_id) REFERENCES product_option(id)
);

CREATE TABLE orders (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        product_option_id BIGINT NOT NULL,
                        member_id BIGINT NOT NULL,
                        quantity INT NOT NULL,
                        message VARCHAR(255),
                        order_date_time TIMESTAMP NOT NULL,
                        FOREIGN KEY (product_option_id) REFERENCES product_option(id),
                        FOREIGN KEY (member_id) REFERENCES member(id)
);