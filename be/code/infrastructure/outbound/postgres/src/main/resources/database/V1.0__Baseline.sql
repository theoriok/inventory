create table country
(
    id   uuid         not null,
    code varchar(10)  not null,
    name varchar(255) not null,
    primary key (id)
);
alter table if exists country add constraint UK_country_code unique (code);

create table cap
(
    id           uuid         not null,
    amount       int4         not null,
    description  text         not null,
    name         varchar(255) not null,
    country_code varchar(10)  not null,
    primary key (id)
);
alter table if exists cap add constraint FK_cap_country_code foreign key (country_code) references country (code);

create table book
(
    id          uuid         not null,
    description text         not null,
    title       varchar(255) not null,
    author      varchar(255) not null,
    primary key (id)
);
