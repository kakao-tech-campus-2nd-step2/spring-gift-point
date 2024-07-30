CREATE TABLE category
(
    id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE product
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(255),
    price       BIGINT,
    category_id BIGINT NOT NULL,
    image_url   VARCHAR(255),
    FOREIGN KEY (category_id) REFERENCES category (id)
);

CREATE TABLE member
(
    id       BIGINT AUTO_INCREMENT PRIMARY KEY,
    name     VARCHAR(255) NOT NULL,
    email    VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role     VARCHAR(50)  NOT NULL
);

CREATE TABLE wishlist
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id  BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    FOREIGN KEY (member_id) REFERENCES member (id),
    FOREIGN KEY (product_id) REFERENCES product (id)
);

CREATE TABLE options
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    name       VARCHAR(255) NOT NULL,
    quantity   BIGINT       NOT NULL,
    product_id BIGINT       NOT NULL,
    FOREIGN KEY (product_id) REFERENCES product (id),
    UNIQUE (product_id, name)
);

CREATE TABLE orders
(
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    option_id       BIGINT    NOT NULL,
    quantity        BIGINT    NOT NULL,
    order_date_time TIMESTAMP NOT NULL,
    message         VARCHAR(255),
    member_id       BIGINT    NOT NULL,
    FOREIGN KEY (option_id) REFERENCES options (id),
    FOREIGN KEY (member_id) REFERENCES member (id)
);