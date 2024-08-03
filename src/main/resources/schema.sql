CREATE TABLE IF NOT EXISTS category (
                                        id BIGINT NOT NULL AUTO_INCREMENT,
                                        name VARCHAR(255) NOT NULL,
                                        color VARCHAR(7) NOT NULL,
                                        image_url VARCHAR(255) NOT NULL,
                                        description VARCHAR(255),
                                        PRIMARY KEY (id),
                                        UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS product (
                                       id BIGINT NOT NULL AUTO_INCREMENT,
                                       name VARCHAR(255) NOT NULL,
                                       price INTEGER NOT NULL,
                                       category_id BIGINT NOT NULL,
                                       image_url VARCHAR(255) NOT NULL,
                                       PRIMARY KEY (id),
                                       FOREIGN KEY (category_id) REFERENCES category(id)
);

CREATE TABLE IF NOT EXISTS member (
                                      id BIGINT NOT NULL AUTO_INCREMENT,
                                      name VARCHAR(255) NOT NULL,
                                      email VARCHAR(255) NOT NULL,
                                      password VARCHAR(255) NOT NULL,
                                      PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS wishlist (
                                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                        option_id BIGINT NOT NULL,
                                        member_id BIGINT NOT NULL,
                                        product_id BIGINT NOT NULL,
                                        FOREIGN KEY (member_id) REFERENCES member(id),
                                        FOREIGN KEY (product_id) REFERENCES product(id)
);

CREATE TABLE IF NOT EXISTS product_option (
                                              id BIGINT NOT NULL AUTO_INCREMENT,
                                              name VARCHAR(255) NOT NULL,
                                              price INTEGER NOT NULL,
                                              product_id BIGINT NOT NULL,
                                              PRIMARY KEY (id),
                                              FOREIGN KEY (product_id) REFERENCES product(id)
);

CREATE TABLE IF NOT EXISTS `order` (
                                       id BIGINT NOT NULL AUTO_INCREMENT,
                                       message VARCHAR(255),
                                       order_date_time DATETIME(6),
                                       quantity INTEGER NOT NULL,
                                       product_option_id BIGINT NOT NULL,
                                       PRIMARY KEY (id)
);

ALTER TABLE `order`
    ADD CONSTRAINT FKbtvfujlvyoah90rtq73gq2412
        FOREIGN KEY (product_option_id)
            REFERENCES product_option (id);