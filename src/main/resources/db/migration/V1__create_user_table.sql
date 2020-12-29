create table if not exists user_account(
    id bigserial primary key,
    username varchar(100) not null
);