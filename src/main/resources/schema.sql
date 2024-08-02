CREATE TABLE categories (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          name VARCHAR(255) NOT NULL,
                          color VARCHAR(255),
                          image_url VARCHAR(255),
                          description TEXT
);

CREATE TABLE products (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         name VARCHAR(255) NOT NULL,
                         price INTEGER NOT NULL,
                         image_url VARCHAR(255) NOT NULL,
                         category_id BIGINT NOT NULL,
                         CONSTRAINT fk_category FOREIGN KEY (category_id) REFERENCES categories (id) ON DELETE CASCADE
);

CREATE TABLE options (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        name VARCHAR(50) NOT NULL,
                        quantity BIGINT NOT NULL CHECK (quantity >= 1 AND quantity < 99999999),
                        product_id BIGINT NOT NULL,
                        CONSTRAINT fk_product FOREIGN KEY (product_id) REFERENCES products (id) ON DELETE CASCADE
);
