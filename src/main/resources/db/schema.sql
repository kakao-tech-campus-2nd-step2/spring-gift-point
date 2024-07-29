CREATE TABLE menus (
                       id INT AUTO_INCREMENT PRIMARY KEY,

                       name VARCHAR(255) NOT NULL,
                       price INT NOT NULL,
                       imageUrl VARCHAR(2048) NOT NULL
);
CREATE TABLE members (
                         id VARCHAR(255) NOT NULL,
                         password VARCHAR(255) NOT NULL
);

CREATE TABLE wishList (
                          id INT AUTO_INCREMENT PRIMARY KEY,
                          menuId INT NOT NULL,
                          memberId VARCHAR(255) NOT NULL
);
