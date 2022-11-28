CREATE DATABASE online_store;

CREATE SCHEMA store_catalog;

CREATE TABLE IF NOT EXISTS customer
(
    id         SERIAL PRIMARY KEY,
    first_name VARCHAR(128) NOT NULL,
    last_name  VARCHAR(128) NOT NULL,
    email      VARCHAR(128) NOT NULL UNIQUE,
    birthdate  DATE         NOT NULL,
    sex        CHAR(6)      NOT NULL,
    city       VARCHAR(32)  NOT NULL
);

CREATE TABLE IF NOT EXISTS product
(
    id           BIGSERIAL PRIMARY KEY,
    product_name VARCHAR(128)  NOT NULL,
    price        NUMERIC(8, 2) NOT NULL,
    description  VARCHAR(1024),
    quantity     INTEGER       NOT NULL
);

CREATE TABLE IF NOT EXISTS orders
(
    id            BIGSERIAL PRIMARY KEY,
    customer_id   INT REFERENCES customer (id),
    sum           NUMERIC(8, 2) NOT NULL,
    or_created_at TIMESTAMP,
    status        VARCHAR(32)   NOT NULL
);


CREATE TABLE IF NOT EXISTS cart
(
    order_id   BIGINT REFERENCES orders (id),
    product_id BIGINT REFERENCES product (id),
    number     INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS review
(
    id            BIGSERIAL PRIMARY KEY,
    product_id    BIGINT REFERENCES product (id),
    costumer_id   INT REFERENCES customer (id),
    content_rv    varchar(1024) NOT NULL,
    rv_created_at TIMESTAMP
);
