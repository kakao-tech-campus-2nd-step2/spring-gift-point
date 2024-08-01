/*CREATE TABLE member_table
(
      id       bigint       not null auto_increment,
      email    varchar(255) not null,
      password varchar(255) not null,
      primary key (id)
) engine=InnoDB;

CREATE TABLE product
(
    price     integer      not null,
    id        bigint       not null auto_increment,
    name      varchar(15)  not null,
    image_url varchar(255) not null,
    primary key (id)
) engine=InnoDB;

CREATE TABLE wish
(
    id         bigint not null auto_increment,
    member_id  bigint not null,
    product_id bigint not null,
    primary key (id)
) engine=InnoDB;

ALTER TABLE member
    add constraint uk_member unique (email);*/