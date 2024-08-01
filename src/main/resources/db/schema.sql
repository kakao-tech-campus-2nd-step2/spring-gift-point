DROP TABLE IF EXISTS WISHES;
DROP TABLE IF EXISTS OPTIONS;
DROP TABLE IF EXISTS PRODUCTS;
DROP TABLE IF EXISTS CATEGORIES;
DROP TABLE IF EXISTS MEMBERS;
DROP TABLE IF EXISTS KAKAO_MEMBERS;
DROP TABLE IF EXISTS ORDERS;

CREATE TABLE CATEGORIES (
                            ID BIGINT AUTO_INCREMENT PRIMARY KEY,
                            NAME VARCHAR(255) NOT NULL UNIQUE,
                            COLOR VARCHAR(255),
                            IMAGE_URL VARCHAR(255),
                            DESCRIPTION TEXT NOT NULL
);

CREATE TABLE members (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         email VARCHAR(255) NOT NULL UNIQUE,
                         password VARCHAR(255),
                         points INT DEFAULT 0
);

CREATE TABLE PRODUCTS (
                          ID BIGINT AUTO_INCREMENT PRIMARY KEY,
                          NAME VARCHAR(255) NOT NULL,
                          PRICE INT NOT NULL,
                          IMAGE_URL VARCHAR(255) NOT NULL,
                          CATEGORY_ID BIGINT,
                          FOREIGN KEY (CATEGORY_ID) REFERENCES CATEGORIES(ID)
);

CREATE TABLE OPTIONS (
                         ID BIGINT AUTO_INCREMENT PRIMARY KEY,
                         NAME VARCHAR(255) NOT NULL,
                         QUANTITY INT,
                         PRODUCT_ID BIGINT,
                         FOREIGN KEY (PRODUCT_ID) REFERENCES PRODUCTS(ID)
);

CREATE TABLE WISHES (
                        ID BIGINT AUTO_INCREMENT PRIMARY KEY,
                        MEMBER_ID BIGINT,
                        PRODUCT_ID BIGINT,
                        CREATED_DATE DATETIME NOT NULL,
                        FOREIGN KEY (MEMBER_ID) REFERENCES MEMBERS(ID),
                        FOREIGN KEY (PRODUCT_ID) REFERENCES PRODUCTS(ID)
);

CREATE TABLE ORDERS (
                        ID BIGINT AUTO_INCREMENT PRIMARY KEY,
                        QUANTITY INT NOT NULL,
                        ORDER_DATE_TIME DATETIME NOT NULL,
                        MESSAGE TEXT,
                        MEMBER_ID BIGINT,
                        OPTION_ID BIGINT,
                        FOREIGN KEY (MEMBER_ID) REFERENCES MEMBERS(ID),
                        FOREIGN KEY (OPTION_ID) REFERENCES OPTIONS(ID)
);