CREATE TABLE IF NOT EXISTS products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price BIGINT NOT NULL,
    image_url VARCHAR(255) NULL
) engine=InnoDB;

CREATE TABLE IF NOT EXISTS options (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    count BIGINT NOT NULL
) engine=InnoDB;

create table categories
(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    color VARCHAR(255) NULL,
    image_url VARCHAR(255) NULL,
    description VARCHAR(255) NULL
) engine=InnoDB;

CREATE TABLE IF NOT EXISTS members (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_type VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    nickname VARCHAR(255) NOT NULL
) engine=InnoDB;

CREATE TABLE wishes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    product_count BIGINT NOT NULL,
    FOREIGN KEY (member_id) REFERENCES members(id),
    FOREIGN KEY (product_id) REFERENCES products(id)
) engine=InnoDB;

alter table members
    add constraint uk_member_email unique (email);

alter table members
    add constraint uk_member_nickname unique (nickname);

-- 외래키 NULL 을 허용하여, 카테고리 없이 상품 생성이 가능하도록 하였다
ALTER TABLE products
    ADD COLUMN category_id BIGINT NULL;

alter table products
    add constraint fk_product_category_id_ref_category_id
        foreign key (category_id) references categories (id);


ALTER TABLE options
    ADD COLUMN product_id BIGINT NULL;

alter table options
    add constraint fk_option_product_id_ref_product_id
        foreign key (product_id) references products (id);
