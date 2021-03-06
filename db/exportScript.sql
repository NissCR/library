drop table if exists AUTHOR;
drop table if exists BOOK;
drop table if exists BOOK_ORDER;
drop table if exists BOOK_PURCHASING;
drop table if exists hibernate_sequence;
drop table if exists MESSAGE;
drop table if exists PUBLISHER;
drop table if exists USER;
create table AUTHOR (AUTHOR_ID  integer, NAME varchar(50) unique, primary key (AUTHOR_ID));
create table BOOK (ISBN varchar(255) not null, AVAILABILYTY integer, COPY_NUM integer, E_VERSION varchar(255), GENRE integer, NAME varchar(100), PAGE_NUM integer, PUBLISH_YEAR integer, AUTHOR_ID integer, PUBLISHER_ID integer, primary key (ISBN));
create table BOOK_ORDER (DTYPE varchar(31) not null, ORDER_ID  integer, DATE date, ISBN varchar(255), LIB_CARD_NUM varchar(10), primary key (ORDER_ID));
create table BOOK_PURCHASING (PRICE double precision, STATE varchar(255), ORDER_ID integer not null, primary key (ORDER_ID));
create table hibernate_sequence (next_val bigint);
insert into hibernate_sequence values ( 1 );
create table MESSAGE (MESSAGE_ID integer not null, ANSWER varchar(255), DATE date, QUESTION varchar(255), LIB_CARD_NUM varchar(10), primary key (MESSAGE_ID));
create table PUBLISHER (PUBLISHER_ID  integer, NAME varchar(60) unique, primary key (PUBLISHER_ID));
create table USER (LIB_CARD_NUM varchar(10) not null, HAS_DEBT boolean, PASSWORD varchar(10), USER_TYPE integer, primary key (LIB_CARD_NUM));
alter table AUTHOR add constraint UK_i6tm8xyrrx880t6wv3e28ry9m unique (NAME);
alter table PUBLISHER add constraint UK_kq3rvu5vw32pjutw46jkv2i8y unique (NAME);
