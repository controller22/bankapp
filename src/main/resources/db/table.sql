CREATE TABLE user_tb(
    id int auto_increment primary key,
    username varchar unique not null,
    password varchar not null,
    fullname varchar not null,
    created_at timestamp not null
);
CREATE TABLE account_tb(
    id int auto_increment primary key,
    number varchar unique not null,
    password varchar not null,
    balance bigint not null,
    user_id int,
    created_at timestamp not null
);
CREATE TABLE history_tb(
    id int auto_increment primary key,
    amount bigint not null,
    w_balance bigint,
    d_balance bigint,
    w_account_id int,
    d_account_id int,
    created_at timestamp not null
);