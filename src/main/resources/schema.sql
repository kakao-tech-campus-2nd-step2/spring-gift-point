-- Drop tables if they exist (with constraints)
ALTER TABLE option DROP CONSTRAINT IF EXISTS FK5t6etuqa4wl7lyn0ysxnts7q4;
ALTER TABLE "order" DROP CONSTRAINT IF EXISTS FKbtfnkke0l8kyq7lyhpwjtg5ev;
ALTER TABLE "order" DROP CONSTRAINT IF EXISTS FK7vnoncjmyv8yy11a8auvoetto;
ALTER TABLE product DROP CONSTRAINT IF EXISTS FK1mtsbur82frn64de7balymq9s;
ALTER TABLE wish_list DROP CONSTRAINT IF EXISTS FK8rt1tquybk69qkn942joirym1;
ALTER TABLE wish_list DROP CONSTRAINT IF EXISTS FKqn4e0ta2823kynefeg4jektp0;

-- Now drop tables
DROP TABLE IF EXISTS category;
DROP TABLE IF EXISTS member;
DROP TABLE IF EXISTS option;
DROP TABLE IF EXISTS "order";
DROP TABLE IF EXISTS product;
DROP TABLE IF EXISTS wish_list;

-- Create tables
CREATE TABLE category (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          name VARCHAR(255) NOT NULL,
                          description VARCHAR(255),
                          color VARCHAR(7),
                          image_url VARCHAR(255)
);

CREATE TABLE member (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        email VARCHAR(255) NOT NULL,
                        password VARCHAR(255) NOT NULL
);

CREATE TABLE product (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         name VARCHAR(255) NOT NULL,
                         image_url VARCHAR(255),
                         price INT NOT NULL,
                         category_id BIGINT,
                         FOREIGN KEY (category_id) REFERENCES category(id)
);

CREATE TABLE option (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        name VARCHAR(255) NOT NULL,
                        quantity INT NOT NULL,
                        product_id BIGINT,
                        FOREIGN KEY (product_id) REFERENCES product(id)
);

CREATE TABLE "`order`" (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         quantity INT NOT NULL,
                         member_id BIGINT,
                         option_id BIGINT,
                         order_date_time TIMESTAMP,
                         message VARCHAR(255),
                         FOREIGN KEY (member_id) REFERENCES member(id),
                         FOREIGN KEY (option_id) REFERENCES option(id)
);

CREATE TABLE wish_list (
                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           product_id BIGINT,
                           member_id BIGINT,
                           FOREIGN KEY (product_id) REFERENCES product(id),
                           FOREIGN KEY (member_id) REFERENCES member(id)
);
