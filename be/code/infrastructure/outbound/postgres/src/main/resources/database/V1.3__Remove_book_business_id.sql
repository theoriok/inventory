alter table book drop constraint if exists UK_book_business_id;
alter table book drop column if exists business_id;
