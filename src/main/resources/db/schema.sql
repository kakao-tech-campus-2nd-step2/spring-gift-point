CREATE TABLE users
(
    user_id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    role              VARCHAR(255) NOT NULL,
    name              VARCHAR(255) NOT NULL,
    email             VARCHAR(255) NOT NULL,
    password          VARCHAR(255) NOT NULL,
    profile_image_url VARCHAR(255),
    kakao_id          BIGINT,
    created_at        TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE wishlist
(
    wishlist_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id     BIGINT    NOT NULL,
    created_at  TIMESTAMP NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (user_id)
);

CREATE TABLE category
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    color       VARCHAR(7)   NOT NULL,
    description VARCHAR(255),
    image_url   VARCHAR(255) NOT NULL
);

CREATE TABLE product
(
    product_id  BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    price DOUBLE NOT NULL,
    image_url   VARCHAR(255) NOT NULL,
    category_id BIGINT       NOT NULL,
    FOREIGN KEY (category_id) REFERENCES category (id)
);

CREATE TABLE product_option
(
    product_option_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name              VARCHAR(255) NOT NULL,
    product_id        BIGINT       NOT NULL,
    quentity          BIGINT       NOT NULL,
    FOREIGN KEY (product_id) REFERENCES product (product_id)
);

CREATE TABLE wishlist_product
(
    wishlist_product_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    wishlist_id         BIGINT NOT NULL,
    product_id          BIGINT NOT NULL,
    quentity            BIGINT NOT NULL,
    FOREIGN KEY (wishlist_id) REFERENCES wishlist (wishlist_id),
    FOREIGN KEY (product_id) REFERENCES product (product_id)
);
