CREATE TABLE IF NOT EXISTS category (
                                        id BIGINT NOT NULL AUTO_INCREMENT,
                                        name VARCHAR(255) NOT NULL,
                                        color VARCHAR(7) NOT NULL,
                                        image_url VARCHAR(255) NOT NULL,
                                        description VARCHAR(255),
                                        PRIMARY KEY (id),
                                        UNIQUE (name)
);

-- Create the product table if not exists
CREATE TABLE IF NOT EXISTS product (
                                       id BIGINT NOT NULL AUTO_INCREMENT,
                                       name VARCHAR(255) NOT NULL,
                                       price INTEGER NOT NULL,
                                       category_id BIGINT NOT NULL,
                                       image_url VARCHAR(255) NOT NULL,
                                       PRIMARY KEY (id),
                                       FOREIGN KEY (category_id) REFERENCES category(id)
);

-- Create the member table if not exists
CREATE TABLE IF NOT EXISTS member (
                                      id BIGINT NOT NULL AUTO_INCREMENT,
                                      name VARCHAR(255) NOT NULL,
                                      email VARCHAR(255) NOT NULL,
                                      password VARCHAR(255) NOT NULL,
                                      PRIMARY KEY (id)
);

-- Create the wishlist table if not exists
CREATE TABLE IF NOT EXISTS wishlist (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          option_id BIGINT NOT NULL,
                          member_id BIGINT NOT NULL,
                          product_id BIGINT NOT NULL,
                          FOREIGN KEY (member_id) REFERENCES member(id),
                          FOREIGN KEY (product_id) REFERENCES product(id)
);