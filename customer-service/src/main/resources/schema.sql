create table if not exists customer (
    id bigserial primary key,
    email varchar(255) not null
);