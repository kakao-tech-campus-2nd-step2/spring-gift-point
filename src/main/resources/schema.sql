CREATE TABLE categories
(
    id            BIGINT AUTO_INCREMENT NOT NULL,
    name          VARCHAR(255)          NULL,
    color         VARCHAR(255)          NULL,
    image_url     VARCHAR(255)          NULL,
    `description` VARCHAR(255)          NULL,
    CONSTRAINT pk_categories PRIMARY KEY (id)
);

CREATE TABLE members
(
    id           BIGINT AUTO_INCREMENT NOT NULL,
    member_type  VARCHAR(255)          NULL,
    access_token VARCHAR(255)          NULL,
    email        VARCHAR(255)          NULL,
    password     VARCHAR(255)          NULL,
    nickname     VARCHAR(255)          NULL,
    CONSTRAINT pk_members PRIMARY KEY (id)
);

CREATE TABLE options
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    product_id BIGINT                NULL,
    name       VARCHAR(255)          NULL,
    quantity   BIGINT                NULL,
    CONSTRAINT pk_options PRIMARY KEY (id)
);

CREATE TABLE orders
(
    id          BIGINT AUTO_INCREMENT NOT NULL,
    member_id   BIGINT                NULL,
    option_id   BIGINT                NULL,
    quantity    BIGINT                NULL,
    message     VARCHAR(255)          NULL,
    total_price BIGINT                NULL,
    CONSTRAINT pk_orders PRIMARY KEY (id)
);

CREATE TABLE products
(
    id          BIGINT AUTO_INCREMENT NOT NULL,
    category_id BIGINT                NULL,
    name        VARCHAR(255)          NULL,
    price       BIGINT                NULL,
    image_url   VARCHAR(255)          NULL,
    CONSTRAINT pk_products PRIMARY KEY (id)
);

CREATE TABLE wishes
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    member_id  BIGINT                NULL,
    product_id BIGINT                NULL,
    CONSTRAINT pk_wishes PRIMARY KEY (id)
);

ALTER TABLE members
    ADD CONSTRAINT uc_members_email UNIQUE (email);

ALTER TABLE members
    ADD CONSTRAINT uc_members_nickname UNIQUE (nickname);

ALTER TABLE options
    ADD CONSTRAINT uc_options_name UNIQUE (name);

ALTER TABLE options
    ADD CONSTRAINT FK_OPTIONS_ON_PRODUCT FOREIGN KEY (product_id) REFERENCES products (id);

ALTER TABLE orders
    ADD CONSTRAINT FK_ORDERS_ON_MEMBER FOREIGN KEY (member_id) REFERENCES members (id);

ALTER TABLE orders
    ADD CONSTRAINT FK_ORDERS_ON_OPTION FOREIGN KEY (option_id) REFERENCES options (id);

ALTER TABLE products
    ADD CONSTRAINT FK_PRODUCTS_ON_CATEGORY FOREIGN KEY (category_id) REFERENCES categories (id);

ALTER TABLE wishes
    ADD CONSTRAINT FK_WISHES_ON_MEMBER FOREIGN KEY (member_id) REFERENCES members (id);

ALTER TABLE wishes
    ADD CONSTRAINT FK_WISHES_ON_PRODUCT FOREIGN KEY (product_id) REFERENCES products (id);