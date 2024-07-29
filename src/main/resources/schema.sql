CREATE TABLE category
(
    id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE product
(
    id        BIGINT AUTO_INCREMENT PRIMARY KEY,
    name      VARCHAR(255) NOT NULL,
    price     INT          NOT NULL,
    image_url VARCHAR(255) NOT NULL,
    category  BIGINT       NOT NULL,
    FOREIGN KEY (category) REFERENCES category (id)
);

CREATE TABLE option
(
    id       BIGINT AUTO_INCREMENT PRIMARY KEY,
    name     VARCHAR(255),
    quantity INT,
    product  BIGINT,
    FOREIGN KEY (product) REFERENCES product (id)
);

CREATE TABLE member
(
    email    VARCHAR(255) PRIMARY KEY,
    password VARCHAR(255) NOT NULL
);

CREATE Table wishlist
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id   BIGINT       NOT NULL,
    member_email VARCHAR(255) NOT NULL,
    FOREIGN KEY (product_id) REFERENCES product (id) ON DELETE CASCADE,
    FOREIGN KEY (member_email) REFERENCES member (email) ON DELETE CASCADE
);