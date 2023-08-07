DROP TABLE IF EXISTS sequence_number;
DROP TABLE IF EXISTS rider_rating;
DROP TABLE IF EXISTS order_tracking;
DROP TABLE IF EXISTS order_segment;
DROP TABLE IF EXISTS order_header;
DROP TABLE IF EXISTS restaurant_food;
DROP TABLE IF EXISTS food;
DROP TABLE IF EXISTS restaurant_rating;
DROP TABLE IF EXISTS restaurant;
DROP TABLE IF EXISTS user_address;
DROP TABLE IF EXISTS customer_address;
DROP TABLE IF EXISTS address;
DROP TABLE IF EXISTS customer;
DROP TABLE IF EXISTS rider_location;
DROP TABLE IF EXISTS user_account;




CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS  cube;
CREATE EXTENSION IF NOT EXISTS  earthdistance;


CREATE SEQUENCE if not exists  hibernate_sequence;
CREATE SEQUENCE if not exists  order_sequence;



DROP TABLE IF EXISTS sequence_number ;
create table sequence_number
(
    web_id          bigint                                 not null
        constraint pk_sequence_number
            primary key,
    name          varchar
);

DROP TABLE IF EXISTS user_account;
create table user_account
(
    web_id          bigint                                 not null
        constraint pk_user_account
            primary key,
    key     varchar(36) not null,
    version         integer                  default 0     not null,
    created_date    timestamp with time zone default now() not null,
    updated_date    timestamp with time zone default now() not null,
    first_name      varchar,
    last_name       varchar,
    email           varchar,
    password        varchar,
    status          varchar,
    type          integer,
    phone_number    varchar,
    created_by      bigint
        constraint fk_user_account__user_account_created_by
            references user_account,
    updated_by      bigint
        constraint fk_user_account__user_account_updated_by
            references user_account,
    avatar varchar,
    CONSTRAINT unique_email_address UNIQUE (email)

);

CREATE UNIQUE INDEX uq_ix_user_account__key ON user_account(key);
CREATE INDEX ix_user_account__first_name ON user_account USING btree(first_name);
CREATE INDEX ix_user_account__phone_number ON user_account USING btree(phone_number);

COMMENT ON COLUMN user_account.web_id IS 'The primary key of user account table';
COMMENT ON COLUMN user_account.key IS 'The unique value to send back the UI to secure the database primary key';
COMMENT ON COLUMN user_account.created_date IS 'The field having a date and time of record creation';
COMMENT ON COLUMN user_account.updated_date IS 'The field when last time record got updated';
COMMENT ON COLUMN user_account.first_name IS 'The first name of the logged in user';
COMMENT ON COLUMN user_account.last_name IS 'The last name of the logged in user';
COMMENT ON COLUMN user_account.email IS 'The field contains the email address of logged in user';
COMMENT ON COLUMN user_account.password IS 'The field contains the encrypted password of  of logged in user';
COMMENT ON COLUMN user_account.status IS 'The field contains the status either user is Active or InActive ';
COMMENT ON COLUMN user_account.type IS 'The field contains the type of user Staff/Admin/Rider etc ';
COMMENT ON COLUMN user_account.phone_number IS 'The field contains contact information of logged in user ';
COMMENT ON COLUMN user_account.created_by IS 'The field contains the information who created this record ';
COMMENT ON COLUMN user_account.updated_by IS 'The field contains the information who updated this record ';
COMMENT ON COLUMN user_account.avatar IS 'The field contains the unique ID of image ';


INSERT INTO user_account (web_id, key, version, created_date, updated_date,first_name, last_name, email, password, status, phone_number, created_by, updated_by, avatar)
VALUES (1713, uuid_generate_v4(),65, '2022-05-09 14:43:17.959000', '2022-06-22 09:51:28.675000', 'waqar', 'khan','waqar@khan.com', '{lJVwInAqDEu8T1m1xrlQluEastkAbg9AjcnLBdqFAWw=}813f35c85dca8de2af9e47e6ae169492', 0, '+9212345', null, null, 'waqar');

INSERT INTO user_account (web_id, key, version, type, created_date, updated_date,first_name, last_name, email, password, status, phone_number, created_by, updated_by, avatar)
VALUES (1714, uuid_generate_v4(),65,1, '2022-05-09 14:43:17.959000', '2022-06-22 09:51:28.675000', 'rider', 'khan','rider@khan.com', '{lJVwInAqDEu8T1m1xrlQluEastkAbg9AjcnLBdqFAWw=}813f35c85dca8de2af9e47e6ae169492', 0, '+9212345', null, null, 'waqar');

INSERT INTO user_account (web_id, key, version, type, created_date, updated_date,first_name, last_name, email, password, status, phone_number, created_by, updated_by, avatar)
VALUES (1715, uuid_generate_v4(),65, 1, '2022-05-09 14:43:17.959000', '2022-06-22 09:51:28.675000', 'rider2', 'khan2','rider2@khan.com', '{lJVwInAqDEu8T1m1xrlQluEastkAbg9AjcnLBdqFAWw=}813f35c85dca8de2af9e47e6ae169492', 0, '+9212345', null, null, 'rider 2');

INSERT INTO user_account (web_id, key, version, type, created_date, updated_date,first_name, last_name, email, password, status, phone_number, created_by, updated_by, avatar)
VALUES (1716, uuid_generate_v4(),65, 1, '2022-05-09 14:43:17.959000', '2022-06-22 09:51:28.675000', 'rider3', 'khan3','rider3@khan.com', '{lJVwInAqDEu8T1m1xrlQluEastkAbg9AjcnLBdqFAWw=}813f35c85dca8de2af9e47e6ae169492', 0, '+9212345', null, null, 'rider 2');




comment on table user_account is 'common, authentication';

create index ix_user_account_email
    on user_account (email);


-------------------------Customer-------------------------


DROP TABLE IF EXISTS customer;
create table customer
(
    web_id          bigint                                 not null
        constraint pk_customer
            primary key,
    key  varchar(36) not null,
    version         integer                  default 0     not null,
    created_date    timestamp with time zone default now() not null,
    updated_date    timestamp with time zone default now() not null,
    first_name      varchar,
    last_name       varchar,
    status          integer,
    type            integer,
    phone_number    varchar,
    created_by      bigint
        constraint fk_user_account__user_account_created_by
            references user_account,
    updated_by      bigint
        constraint fk_user_account__user_account_updated_by
            references user_account,
    avatar varchar,
    CONSTRAINT unique_phone_number UNIQUE (phone_number)

);


CREATE UNIQUE INDEX uq_ix_customer__key ON customer(key);
CREATE INDEX ix_customer__first_name ON customer USING btree(first_name);
CREATE INDEX ix_customer__phone_number ON customer USING btree(phone_number);

COMMENT ON COLUMN customer.web_id IS 'The primary key of customer table';
COMMENT ON COLUMN customer.key IS 'The unique value to send back the UI to secure the database primary key';
COMMENT ON COLUMN customer.created_date IS 'The field having a date and time of record creation';
COMMENT ON COLUMN customer.updated_date IS 'The field when last time record got updated';
COMMENT ON COLUMN customer.first_name IS 'The first name of the customer';
COMMENT ON COLUMN customer.last_name IS 'The last name of the customer';
COMMENT ON COLUMN customer.status IS 'The field contains the status either customer is Active or InActive ';
COMMENT ON COLUMN customer.phone_number IS 'The field contains contact information of customer';
COMMENT ON COLUMN customer.created_by IS 'The field contains the information who created this record ';
COMMENT ON COLUMN customer.updated_by IS 'The field contains the information who updated this record ';
COMMENT ON COLUMN customer.avatar IS 'The field contains the unique ID of image ';

INSERT INTO customer (web_id, key, version, created_date, updated_date,first_name, last_name, status, type, phone_number, created_by, updated_by, avatar)
VALUES (1522, uuid_generate_v4(),65, '2022-05-09 14:43:17.959000', '2022-06-22 09:51:28.675000', 'waqar', 'khan', 0, '0','+9212345', null, null, 'waqar');
-------------------------Address-------------------------
DROP TABLE IF EXISTS address;

create table address
(
    web_id       bigint                                 not null
        constraint pk_address
            primary key,
        key  varchar(36) not null,
    version      integer                  default 0     not null,
    created_date timestamp with time zone default now() not null,
    updated_date timestamp with time zone default now() not null,
    city         varchar,
    country      varchar,
    postal_code  varchar,
    state        varchar,
    street       varchar,
    street2      varchar,
    address_type varchar,   --Need to created enum
    county       varchar,
    latitude     double precision,
    longitude   double precision,
    created_by   bigint
        constraint fk_user_account__user_account_created_by
            references user_account,
    updated_by   bigint
        constraint fk_user_account__user_account_updated_by
            references user_account
);

CREATE UNIQUE INDEX uq_ix_address__key ON address(key);
CREATE INDEX ix_address__postal_code ON address USING btree(postal_code);
CREATE INDEX ix_address__address_type ON address USING btree(address_type);
CREATE INDEX ix_address__country ON address USING btree(country);

INSERT INTO address (web_id, key, version, created_date, updated_date,  city, country,
                         postal_code, state, street, street2, address_type, county, latitude, longitude, created_by, updated_by)
                          VALUES (104102, uuid_generate_v4(), 0, '2022-03-26 21:14:26.805000', '2022-03-26 21:14:26.805000', 'Lahore','Pakisttan', '54000', 'Punjab', 'Dha phase 4', 's3', null,  null, 13.63664, 100.68676, 1713, 1713);

INSERT INTO address (web_id, key, version, created_date, updated_date,  city, country,
                         postal_code, state, street, street2, address_type, county, latitude, longitude, created_by, updated_by)
                          VALUES (104103, uuid_generate_v4(), 0, '2022-03-26 21:14:26.805000', '2022-03-26 21:14:26.805000', 'Lahore','Pakisttan', '54000', 'Punjab', 'Kalma chonk', 's3', null,  null, 21.516041, 70.468468, 1713, 1713);

INSERT INTO address (web_id, key, version, created_date, updated_date,  city, country,
                         postal_code, state, street, street2, address_type, county, latitude, longitude, created_by, updated_by)
                          VALUES (104104, uuid_generate_v4(), 0, '2022-03-26 21:14:26.805000', '2022-03-26 21:14:26.805000', 'Lahore','Pakisttan', '54000', 'Punjab', 'firdose market', 's3', null,  null, 31.618876, 74.248100, 1713, 1713);
-------------------------User Address-------------------------

DROP TABLE IF EXISTS user_address;

create table user_address
(
    web_id       bigint                                 not null
        constraint pk_user_address
            primary key,
       key  varchar(36) not null,
    version      integer                  default 0     not null,
    created_date timestamp with time zone default now() not null,
    updated_date timestamp with time zone default now() not null,
    user_id      bigint
        constraint fk_user_address__user_account
            references user_account,
    address_id      bigint
        constraint fk_user_address__address
            references address,
      created_by   bigint
        constraint fk_user_address__user_account_created_by
            references user_account,
    updated_by   bigint
        constraint fk_user_address__user_account_updated_by
            references user_account
);

CREATE UNIQUE INDEX uq_ix_user_address__key ON user_address(key);
CREATE INDEX ix_user_address__user ON user_address USING btree(user_id);
CREATE INDEX ix_user_address__address ON user_address USING btree(address_id);

insert into user_address (web_id,key, user_id, address_id, created_by, updated_by) values (121,uuid_generate_v4(), 1713,  104102,1713,1713);

-------------------------Customer Address-------------------------


DROP TABLE IF EXISTS customer_address;

create table customer_address
(
    web_id       bigint                                 not null
        constraint pk_customer_address
            primary key,
       key  varchar(36) not null,
    version      integer                  default 0     not null,
    created_date timestamp with time zone default now() not null,
    updated_date timestamp with time zone default now() not null,
    customer_id      bigint
        constraint fk_customer_address__customer
            references customer,
    address_id      bigint
        constraint fk_user_address__address
            references address,
      created_by   bigint
        constraint fk_user_address__user_account_created_by
            references user_account,
    updated_by   bigint
        constraint fk_user_address__user_account_updated_by
            references user_account
);

CREATE UNIQUE INDEX uq_ix_customer_address__key ON customer_address(key);
CREATE INDEX ix_customer_address__customer ON customer_address USING btree(customer_id);
CREATE INDEX ix_customer_address__address ON customer_address USING btree(address_id);


insert into customer_address (web_id,key, customer_id, address_id, created_by, updated_by) values (121,uuid_generate_v4(), 1522,  104102,1713,1713);


-------------------------Restaurant-------------------------
DROP TABLE IF EXISTS restaurant;
create table restaurant
(
    web_id          bigint                                 not null
        constraint pk_restaurant
            primary key,
        key  varchar(36) not null,
    version         integer                  default 0     not null,
    created_date    timestamp with time zone default now() not null,
    updated_date    timestamp with time zone default now() not null,
    name            varchar,
    address_id      bigint
        constraint fk_restaurant__address references address,
    logo            varchar,
    status          integer,
    code          varchar,
    phone_number    text,
    owner_id      bigint
        constraint fk_restaurant__user_account
            references user_account,
    created_by      bigint
        constraint fk_restaurant__user_account_created_by
            references user_account,
    updated_by      bigint
        constraint fk_restaurant__user_account_updated_by
            references user_account
);

CREATE UNIQUE INDEX uq_ix_restaurant__key ON restaurant(key);
CREATE INDEX ix_restaurant__name ON restaurant USING btree(name);
CREATE INDEX ix_restaurant__address ON restaurant USING btree(address_id);
CREATE INDEX ix_restaurant__owner ON restaurant USING btree(owner_id);

insert into restaurant (web_id, key, name, address_id, logo, status, code, phone_number, owner_id, created_by, updated_by) VALUES (450, uuid_generate_v4(), 'Bay area', 104102, 'Logo',1,'WR','CUSTOMER',1713,1713,1713);

-------------------------Foods-------------------------
DROP TABLE IF EXISTS food;
create table food
(
    web_id          bigint                                 not null
        constraint pk_food
            primary key,
        key  varchar(36) not null,
    version         integer                  default 0     not null,
    created_date    timestamp with time zone default now() not null,
    updated_date    timestamp with time zone default now() not null,
    category      varchar,
    name      varchar,
    description       varchar,
    status          integer,
    created_by      bigint
        constraint fk_food__user_account_created_by
            references user_account,
    updated_by      bigint
        constraint fk_food__user_account_updated_by
            references user_account
);

CREATE UNIQUE INDEX uq_ix_food__key ON food(key);
CREATE INDEX ix_food__name ON food USING btree(name);
CREATE INDEX ix_food__category ON food USING btree(category);

insert into food (web_id, key, name, description, status, created_by, updated_by) values (560,uuid_generate_v4(),'Chicken burger','Chicken burger', 1,1713,1713);
insert into food (web_id, key, name, description, status, created_by, updated_by) values (565,uuid_generate_v4(),'Zinger burger','Chicken burger', 1,1713,1713);
insert into food (web_id, key, name, description, status, created_by, updated_by) values (570,uuid_generate_v4(),'Pety burger','Chicken burger', 1,1713,1713);

-------------------------Restaurant Foods-------------------------
DROP TABLE IF EXISTS restaurant_food;
create table restaurant_food
(
    web_id          bigint                                 not null
        constraint pk_restaurant_food
            primary key,
        key  varchar(36) not null,
    version         integer                  default 0     not null,
    created_date    timestamp with time zone default now() not null,
    updated_date    timestamp with time zone default now() not null,
    restaurant_id      bigint
        constraint fk_restaurant_food__restaurant references restaurant,
    food_id      bigint
        constraint fk_food__restaurant_food references food,
    rating   double precision,
    estimated_cooking_time   double precision,
    status          integer,
    discount_rate   double precision,
    price   double precision,

    image           varchar,
    created_by      bigint
        constraint fk_restaurant_food__user_account_created_by
            references user_account,
    updated_by      bigint
        constraint fk_restaurant_food__user_account_updated_by
            references user_account
);

CREATE UNIQUE INDEX uq_ix_restaurant_food__key ON restaurant_food(key);
CREATE INDEX ix_restaurant_food__restaurant ON restaurant_food USING btree(restaurant_id);
CREATE INDEX ix_restaurant_food__food ON restaurant_food USING btree(food_id);
CREATE INDEX ix_restaurant_food__rating ON restaurant_food USING btree(rating);

insert into restaurant_food (web_id, key, restaurant_id, food_id, rating, status, estimated_cooking_time,discount_rate,price, created_by,  updated_by) values (575, uuid_generate_v4(), 450, 560,8,1,30,10,500,1713,1713);
insert into restaurant_food (web_id, key, restaurant_id, food_id, rating, status, estimated_cooking_time, discount_rate,price,  created_by, updated_by) values (580, uuid_generate_v4(), 450, 565,8,1,30,10, 500,1713,1713);
insert into restaurant_food (web_id, key, restaurant_id, food_id, rating, status, estimated_cooking_time, discount_rate,price, created_by, updated_by) values (585,uuid_generate_v4(), 450, 570,8,1,30,10,500,1713,1713);
-------------------------orders-------------------------
DROP TABLE IF EXISTS order_header;
create table order_header
(
    web_id          bigint                                 not null
        constraint pk_order_header
            primary key,
        key  varchar(36) not null,
    version         integer                  default 0     not null,
    created_date    timestamp with time zone default now() not null,
    updated_date    timestamp with time zone default now() not null,
    customer_id          bigint
            constraint fk_order_header__customer references customer,
    restaurant_id      bigint
            constraint fk_order_segment__restaurant references restaurant,
    order_number           varchar,
    status          integer,
    priority          integer not null default 0,
    expected_cooking_time    timestamp with time zone default now() not null,
    actual_cooking_time    timestamp with time zone,
    expected_pickup_time    timestamp with time zone default now() not null,
    actual_pickup_time    timestamp with time zone ,
    expected_delivery_time    timestamp with time zone default now(),
    actual_delivery_time    timestamp with time zone,
    ship_to_address      bigint
        constraint fk_order_header__address
            references address,
    created_by      bigint
        constraint fk_order_header__user_account_created_by
            references user_account,
    updated_by      bigint
        constraint fk_order_header__user_account_updated_by
            references user_account
);

CREATE UNIQUE INDEX uq_ix_order_header__key ON order_header(key);
CREATE INDEX ix_order_header__customer ON order_header USING btree(customer_id);
CREATE INDEX ix_order_header__restaurant ON order_header USING btree(restaurant_id);
CREATE INDEX ix_order_header__shipping_address ON order_header USING btree(ship_to_address);

insert into order_header (web_id, key, customer_id, restaurant_id,order_number, status, ship_to_address, created_by, updated_by) VALUES (590, uuid_generate_v4(),1522,450, 'WR-110', 1,104102,1713,1713);
-------------------------Orders segment -------------------------
DROP TABLE IF EXISTS order_segment;

create table order_segment
(
    web_id          bigint                                 not null
        constraint pk_order_segment
            primary key,
        key  varchar(36) not null,
    version         integer                  default 0     not null,
    created_date    timestamp with time zone default now() not null,
    updated_date    timestamp with time zone default now() not null,
    order_id      bigint
        constraint fk_order_segment__order references order_header,

    food_id      bigint
        constraint fk_food__order_segment references food,
    discount_rate   double precision,
    price   double precision,
    status          integer,
    created_by      bigint
        constraint fk_order_segment__user_account_created_by
            references user_account,
    updated_by      bigint
        constraint fk_order_segment__user_account_updated_by
            references user_account
);

CREATE UNIQUE INDEX uq_ix_order_segment__key ON order_segment(key);
CREATE INDEX ix_order_segment__order ON order_segment USING btree(order_id);
CREATE INDEX ix_order_segment__food ON order_segment USING btree(food_id);

insert into order_segment (web_id, key ,order_id, food_id, discount_rate, price, status, created_by, updated_by) VALUES (595, uuid_generate_v4(),590,560,0,500,1,1713,1713);
insert into order_segment (web_id, key,order_id, food_id, discount_rate, price, status, created_by, updated_by) VALUES (600, uuid_generate_v4(), 590,565,0,500,1,1713,1713);
insert into order_segment (web_id, key, order_id, food_id, discount_rate, price, status, created_by, updated_by) VALUES (605, uuid_generate_v4(), 590,570,0,500,1,1713,1713);

-------------------------Order Tracking -------------------------
DROP TABLE IF EXISTS order_tracking;

create table order_tracking
(
    web_id          bigint                                 not null
        constraint pk_order_tracking
            primary key,
        key  varchar(36) not null,
    version         integer                  default 0     not null,
    created_date    timestamp with time zone default now() not null,
    updated_date    timestamp with time zone default now() not null,
    order_id      bigint
        constraint fk_order_tracking__order references order_header,

   rider_id      bigint
           constraint fk_order_tracking__user_account_rider_id
               references user_account,
   current_latitude     double precision,
   current_longitude    double precision,
    distance    double precision,
    created_by      bigint
        constraint fk_order_tracking__user_account_created_by
            references user_account,
    updated_by      bigint
        constraint fk_order_tracking__user_account_updated_by
            references user_account
);

CREATE UNIQUE INDEX uq_ix_order_tracking__key ON order_tracking(key);
CREATE INDEX ix_order_tracking__order ON order_tracking USING btree(order_id);
CREATE INDEX ix_order_tracking__rider ON order_tracking USING btree(rider_id);
--insert into order_tracking (web_id, key,order_id, rider_id,current_latitude,current_longitude,created_by,updated_by) VALUES (590, uuid_generate_v4(),590,1716,1.0,1.0,1713,1713);
-------------------------Rider location -------------------------


DROP TABLE IF EXISTS rider_location;

create table rider_location
(
   web_id          bigint                                 not null
        constraint pk_rider_location
            primary key,
   key  varchar(36) not null,
   updated_date    timestamp with time zone default now() not null,
   rider_id      bigint
           constraint fk_rider_location__user_account_rider_id
               references user_account,
   latitude     double precision,
   longitude    double precision
);


INSERT INTO rider_location (web_id, key,updated_date, rider_id, latitude, longitude)
                          VALUES (1224, uuid_generate_v4(), '2022-03-26 21:14:26.805000',1714, 13.63664, 100.68676);
INSERT INTO rider_location (web_id, key,updated_date, rider_id, latitude, longitude)
                          VALUES (1225, uuid_generate_v4(), '2022-03-26 21:14:26.805000',1715, 21.516041, 70.468468);
INSERT INTO rider_location (web_id, key,updated_date, rider_id, latitude, longitude)
                          VALUES (1226, uuid_generate_v4(), '2022-03-26 21:14:26.805000',1716, 31.618876, 74.248100);
-------------------------Rider Rating -------------------------
DROP TABLE IF EXISTS rider_rating;
create table rider_rating
(
    web_id          bigint                                 not null
        constraint pk_rider_rating
            primary key,
        key  varchar(36) not null,
    version         integer                  default 0     not null,
    created_date    timestamp with time zone default now() not null,
    updated_date    timestamp with time zone default now() not null,
    rider_id      bigint
            constraint fk_rider_rating__user_account_rider_id
                references user_account,

    customer_id          bigint
        constraint fk_rider_rating__customer
            references customer,
    order_id      bigint
            constraint fk_rider_rating__order references order_header,
    restaurant_id      bigint
        constraint fk_rider_rating__restaurant
            references restaurant,
    rating   double precision,
    comment            varchar,
    created_by      bigint
        constraint fk_rider_rating_created_by
            references user_account,
    updated_by      bigint
        constraint fk_rider_rating__user_account_updated_by
            references user_account
);

CREATE UNIQUE INDEX uq_ix_rider_rating__key ON rider_rating(key);
CREATE INDEX ix_rider_rating__order ON rider_rating USING btree(order_id);
CREATE INDEX ix_rider_rating__rider ON rider_rating USING btree(rider_id);
CREATE INDEX ix_rider_rating__customer ON rider_rating USING btree(customer_id);
CREATE INDEX ix_rider_rating__restaurant ON rider_rating USING btree(restaurant_id);
CREATE INDEX ix_rider_rating__rating ON rider_rating USING btree(rating);

-----------------------------------------------------------------------
DROP VIEW if exists public.vw_ticket;
DROP TABLE IF EXISTS ticket;
DROP TABLE IF EXISTS food_delivery;
create table food_delivery
(
    web_id          bigint                                 not null
        constraint pk_food_delivery
            primary key,
        key  varchar(36) not null,
    delivery_id     varchar,
    customer        varchar,
    delivery_status varchar,
    priority integer default 0,
    expected_delivery_time    timestamp with time zone default now(),
    distance double precision,
    rider_rating double precision,
    food_mean_time  double precision,
    actual_delivery_time    timestamp with time zone
);
DROP TABLE IF EXISTS ticket;
create table ticket
(
    web_id          bigint                                 not null
        constraint pk_ticket
            primary key,
        key  varchar(36) not null,
    created_date    timestamp with time zone default now() not null,
    updated_date    timestamp with time zone default now() not null,
    food_delivery_id     bigint
            constraint fk_ticket__food_delivery
            references food_delivery,
    reason        varchar,
    status integer
);



------------------------------ticket View-----------------------------
DROP VIEW if exists public.vw_ticket;
CREATE OR REPLACE VIEW public.vw_ticket
 AS
 SELECT fd.*, t.key as ticket_key, t.reason, t.status as ticket_status, t.created_date, t.updated_date
   FROM ticket t join food_delivery fd on t.food_delivery_id = fd.web_id

