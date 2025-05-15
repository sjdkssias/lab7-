create table users (
    uid serial primary key,
    username text not null unique,
    password text not null
)

create table dragon (
    id serial primary key,
    name text not null,
    x float not null,
    y float not null,
    creation_date timestamp default now(),
    speaking boolean,
    color text not null,
    character text,
    toothcount float not null
);
