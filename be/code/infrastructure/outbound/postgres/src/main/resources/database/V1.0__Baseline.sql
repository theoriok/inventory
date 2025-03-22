create table cap
(
    id           uuid         not null,
    amount       int4         not null,
    business_id  varchar(255) not null,
    description  text,
    name         varchar(255) not null,
    country_code varchar(255) not null,
    primary key (id)
);
create table country
(
    id   uuid         not null,
    code varchar(255) not null,
    name varchar(255) not null,
    primary key (id)
);
alter table if exists cap add constraint UK_cap_business_id unique (business_id);
alter table if exists country add constraint UK_country_code unique (code);
alter table if exists cap add constraint FK_cap_country_code foreign key (country_code) references country (code);