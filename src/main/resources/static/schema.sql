create table `products` (
    `id` bigint auto_increment primary key,
    `name` varchar(255) not null,
    `price` int not null,
    `image_url` varchar(255) not null
);

create table `user` (
    `id`       bigint auto_increment primary key,
    `name`     varchar(255) not null
);

create table `user_account` (
    `user_id`     bigint primary key,
    `principal`   varchar(255) not null,
    `credentials` varchar(255) not null,
    constraint UK_principal unique (`principal`)
);

create table `wishes` (
    `user_id`    bigint not null,
    `product_id` bigint not null,
    primary key (`user_id`, `product_id`)
);