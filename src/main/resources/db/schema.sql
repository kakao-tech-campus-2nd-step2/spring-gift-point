CREATE TABLE users
(
    user_id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    role              VARCHAR(255) NOT NULL,
    email             VARCHAR(255) NOT NULL,
    password          VARCHAR(255) NOT NULL,
    name              VARCHAR(255),
    profile_image_url VARCHAR(255),
    kakao_id          BIGINT,
    login_type        VARCHAR(255),
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
    category_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    image_url   VARCHAR(255) NOT NULL,
    color       VARCHAR(7)   NOT NULL
);

CREATE TABLE product
(
    product_id  BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    price       BIGINT       NOT NULL,
    image_url   VARCHAR(255) NOT NULL,
    category_id BIGINT       NOT NULL,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES category (category_id)
);

CREATE TABLE product_option
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    name       VARCHAR(255) NOT NULL,
    product_id BIGINT       NOT NULL,
    quantity   BIGINT       NOT NULL,

    FOREIGN KEY (product_id) REFERENCES product (product_id)
);

CREATE TABLE wishlist_product
(
    wishlist_product_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    wishlist_id         BIGINT NOT NULL,
    product_id          BIGINT NOT NULL,
    product_option_id   BIGINT,
    quantity            BIGINT NOT NULL,
    FOREIGN KEY (wishlist_id) REFERENCES wishlist (wishlist_id),
    FOREIGN KEY (product_id) REFERENCES product (product_id),
    FOREIGN KEY (product_option_id) REFERENCES product_option (id)
);

CREATE TABLE orders
(
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id         BIGINT NOT NULL,
    product_id      BIGINT NOT NULL,
    option_id       BIGINT NOT NULL,
    quantity        BIGINT NOT NULL,
    message         VARCHAR(255),
    is_receipt    BOOLEAN   DEFAULT FALSE,
    phone_number    VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users (user_id),
    FOREIGN KEY (option_id) REFERENCES product_option (id)
);



CREATE TABLE user_points
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id    BIGINT NOT NULL,
    point      BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
