DROP TABLE IF EXISTS dragon CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP SEQUENCE IF EXISTS dragon_id_seq CASCADE;

create sequence dragon_id_seq start with 1 increment by 1;


create table users (
    uid serial primary key,
    username text not null unique,
    password text not null
);

create table dragon (
    id bigint  primary key default nextval('dragon_id_seq'),
    name text not null,
    x float not null,
    y integer not null,
    creation_date timestamp default now(),
    speaking boolean,
    color text not null,
    character text,
    head float not null,
    user_id integer not null references users(uid) on delete cascade
);
