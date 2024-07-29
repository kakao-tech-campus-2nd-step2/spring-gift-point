DROP TABLE IF EXISTS WISHES;
DROP TABLE IF EXISTS OPTIONS;
DROP TABLE IF EXISTS PRODUCTS;
DROP TABLE IF EXISTS CATEGORIES;
DROP TABLE IF EXISTS MEMBERS;
DROP TABLE IF EXISTS KAKAO_MEMBERS;
DROP TABLE IF EXISTS ORDERS;

CREATE TABLE CATEGORIES (
                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            name VARCHAR(255) NOT NULL UNIQUE,
                            color VARCHAR(255) NOT NULL,
                            image_url VARCHAR(255) NOT NULL,
                            description VARCHAR(255) NOT NULL
);

CREATE TABLE PRODUCTS (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          name VARCHAR(15) NOT NULL,
                          price INT NOT NULL,
                          image_url VARCHAR(255) NOT NULL,
                          category_id BIGINT,
                          FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE CASCADE
);

CREATE TABLE OPTIONS (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         name VARCHAR(255) NOT NULL UNIQUE,
                         quantity INT,
                         product_id BIGINT,
                         FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
);

CREATE TABLE MEMBERS (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         email VARCHAR(255) NOT NULL UNIQUE,
                         password VARCHAR(255)
);

CREATE TABLE WISHES (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        member_id BIGINT,
                        option_id BIGINT,
                        quantity INT NOT NULL,
                        FOREIGN KEY (member_id) REFERENCES members(id) ON DELETE CASCADE,
                        FOREIGN KEY (option_id) REFERENCES options(id) ON DELETE CASCADE
);

CREATE TABLE KAKAO_MEMBERS (
                               id BIGINT AUTO_INCREMENT PRIMARY KEY,
                               email VARCHAR(255) NOT NULL,
                               access_token VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE ORDERS (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        quantity INT NOT NULL,
                        order_date_time TIMESTAMP NOT NULL,
                        message VARCHAR(255),
                        member_id BIGINT,
                        option_id BIGINT,
                        CONSTRAINT fk_member
                            FOREIGN KEY (member_id)
                                REFERENCES members (id),
                        CONSTRAINT fk_option
                            FOREIGN KEY (option_id)
                                REFERENCES options (id)
);
