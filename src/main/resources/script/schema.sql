CREATE TABLE app_user (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          email VARCHAR(255) NOT NULL UNIQUE,
                          password VARCHAR(100) NOT NULL,
                          is_active BOOLEAN NOT NULL DEFAULT true,
                          role VARCHAR(50) NOT NULL DEFAULT 'USER',
                          salt VARCHAR(255),
                          reg_date TIMESTAMP NOT NULL DEFAULT NOW(),
                          mod_date TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE product (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          name VARCHAR(255) NOT NULL,
                          price INT NOT NULL CHECK (price >= 0),
                          image_url TEXT,
                          user_id BIGINT NOT NULL,
                          is_active BOOLEAN NOT NULL DEFAULT true,
                          reg_date TIMESTAMP NOT NULL DEFAULT NOW(),
                          mod_date TIMESTAMP NOT NULL DEFAULT NOW(),
                          FOREIGN KEY (user_id) REFERENCES app_user(id)
);

CREATE TABLE wish (
                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                    user_id BIGINT NOT NULL,
                    product_id BIGINT NOT NULL,
                    quantity INT NOT NULL DEFAULT 1,
                    is_active BOOLEAN DEFAULT TRUE,
                    reg_date TIMESTAMP NOT NULL DEFAULT NOW(),
                    mod_date TIMESTAMP NOT NULL DEFAULT NOW(),
                    FOREIGN KEY (user_id) REFERENCES app_user(id),
                    FOREIGN KEY (product_id) REFERENCES product(id)
);
