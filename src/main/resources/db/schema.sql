CREATE TABLE Product
(
    id          BIGINT            AUTO_INCREMENT   PRIMARY KEY,
    name        VARCHAR(255)   NOT NULL,
    price       BIGINT         NOT NULL         CHECK (0 <= price),
    imageUrl    VARCHAR(255)   NOT NULL
);

create table Member
(
    id          BIGINT            AUTO_INCREMENT   PRIMARY KEY,
    email       VARCHAR(255)                    UNIQUE,
    password    VARCHAR(255),
    kakao_id    BIGINT,
    kakao_token VARCHAR(255)
);

create table WishList
(
    id          BIGINT            AUTO_INCREMENT   PRIMARY KEY,
    member_id   BIGINT          NOT NULL,
    product_id  BIGINT          NOT NULL
);

create table Option
(
    id          BIGINT            AUTO_INCREMENT   PRIMARY KEY,
    name        VARCHAR(255)   NOT NULL,
    quantity    BIGINT         NOT NULL         CHECK (0 <= quantity),
    product_id  BIGINT         NOT NULL
);
