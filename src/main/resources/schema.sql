CREATE TABLE category (
                          id BIGINT PRIMARY KEY AUTO_INCREMENT,
                          name VARCHAR(255) NOT NULL UNIQUE,
                          color VARCHAR(7) NOT NULL,
                          imageurl VARCHAR(255) NOT NULL,
                          description VARCHAR(255)
);

CREATE TABLE product (
                         id BIGINT PRIMARY KEY AUTO_INCREMENT,
                         name VARCHAR(15) NOT NULL,
                         price INT NOT NULL,
                         imageurl VARCHAR(255) NOT NULL,
                         category_id BIGINT NOT NULL,
                         FOREIGN KEY (category_id) REFERENCES category(id)
);

CREATE TABLE option (
                        id BIGINT PRIMARY KEY AUTO_INCREMENT,
                        name VARCHAR(50) NOT NULL UNIQUE,
                        quantity INT NOT NULL CHECK (quantity BETWEEN 1 AND 99999999),
                        product_id BIGINT NOT NULL,
                        FOREIGN KEY (product_id) REFERENCES product(id)
);

CREATE TABLE member (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        email VARCHAR(255) NOT NULL,
                        password VARCHAR(255) NOT NULL
);

CREATE TABLE wish (
                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                      member_id BIGINT NOT NULL,
                      product_id BIGINT NOT NULL,
                      FOREIGN KEY (member_id) REFERENCES member(id),
                      FOREIGN KEY (product_id) REFERENCES product(id)
);