create table if not exists member (
    id              BIGINT          not null AUTO_INCREMENT,
    email           VARCHAR(255)    not null,
    password        VARCHAR(255),
    oauth_provider  VARCHAR(255),
    points          INT             NOT NULL DEFAULT 0,
    PRIMARY KEY     (id),
    UNIQUE          (email)
);

create table if not exists access_token (
    id              BIGINT          not null AUTO_INCREMENT,
    member_id       BIGINT          not null,
    provider        VARCHAR(255)    not null,
    token           VARCHAR(255)    not null,
    expiry_date     TIMESTAMP       not null,
    PRIMARY KEY     (id),
    FOREIGN KEY     (member_id)     REFERENCES member(id)
);

create table if not exists category (
    id              BIGINT          not null AUTO_INCREMENT,
    name            VARCHAR(255)    not null,
    color           VARCHAR(7)      not null,
    image_url       VARCHAR(255)    not null,
    description     VARCHAR(255),
    PRIMARY KEY     (id),
    UNIQUE          (name)
);

create table if not exists product (
    id              BIGINT          not null AUTO_INCREMENT,
    name            VARCHAR(15)     not null,
    price           INTEGER         not null,
    image_url       VARCHAR(255)    not null,
    category_id     BIGINT          not null,
    PRIMARY KEY     (id),
    FOREIGN KEY     (category_id)   REFERENCES category(id)
);

create table if not exists option (
    id              BIGINT          not null AUTO_INCREMENT,
    name            VARCHAR(50)     not null,
    quantity        INTEGER         not null,
    product_id      BIGINT          not null,
    PRIMARY KEY     (id),
    FOREIGN KEY     (product_id)    REFERENCES product(id)
);

create table if not exists wish (
    id              BIGINT          not null AUTO_INCREMENT,
    member_id       BIGINT          not null,
    product_id      BIGINT          not null,
    PRIMARY KEY     (id),
    FOREIGN KEY     (member_id)     REFERENCES member(id),
    FOREIGN KEY     (product_id)    REFERENCES product(id)
);

CREATE TABLE IF NOT EXISTS orders (
    id              BIGINT          NOT NULL AUTO_INCREMENT,
    option_id       BIGINT          NOT NULL,
    quantity        INT             NOT NULL,
    message         VARCHAR(255),
    order_date_time TIMESTAMP       NOT NULL,
    cash_receipt    BOOLEAN         NOT NULL,
    phone_number    VARCHAR(20),
    payment_amount  INT             NOT NULL,
    points_used     INT             NOT NULL,
    PRIMARY KEY     (id),
    FOREIGN KEY     (option_id)     REFERENCES option(id)
);