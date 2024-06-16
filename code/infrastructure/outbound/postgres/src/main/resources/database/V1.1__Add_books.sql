create table book
(
    id          uuid         not null,
    business_id varchar(255) not null,
    description text,
    title       varchar(255) not null,
    author      varchar(255) not null,
    primary key (id)
);
alter table if exists book add constraint UK_book_business_id unique (business_id);