CREATE TABLE category (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          name VARCHAR(255) NOT NULL,
                          color VARCHAR(255),
                          image_url VARCHAR(255),
                          description TEXT
);
