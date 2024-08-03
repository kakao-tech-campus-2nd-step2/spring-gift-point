CREATE TABLE IF NOT EXISTS members
(
    id       BIGINT AUTO_INCREMENT PRIMARY KEY,
    email    VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role     VARCHAR(255) NOT NULL
);


CREATE TABLE IF NOT EXISTS wishes
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id  BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    FOREIGN KEY (member_id) REFERENCES members (id)
);

CREATE TABLE IF NOT EXISTS categories
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(255) NOT NULL UNIQUE,
    color       VARCHAR(255),
    img_url     VARCHAR(255),
    description VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS products
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(255) NOT NULL UNIQUE,
    price       INT          NOT NULL,
    img_url     VARCHAR(255) NOT NULL,
    category_id BIGINT,
    CONSTRAINT fk_category FOREIGN KEY (category_id) REFERENCES categories (id)
);

CREATE TABLE IF NOT EXISTS options
(
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    name           VARCHAR(255) NOT NULL,
    stock_quantity INT          NOT NULL,
    product_id     BIGINT,
    CONSTRAINT fk_product FOREIGN KEY (product_id) REFERENCES products (id)
);