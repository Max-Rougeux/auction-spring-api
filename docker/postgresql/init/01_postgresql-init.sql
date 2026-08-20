-- ============================================================
-- PostgreSQL conversion of 01-init.sql
-- ============================================================

-- Create database (run as superuser outside a transaction if needed)
-- CREATE DATABASE auction;
-- \c auction

-- Drop tables in reverse FK order
DROP TABLE IF EXISTS BIDS            CASCADE;
DROP TABLE IF EXISTS SALES           CASCADE;
DROP TABLE IF EXISTS ITEMS           CASCADE;
DROP TABLE IF EXISTS REFRESH_TOKENS  CASCADE;
DROP TABLE IF EXISTS USERS           CASCADE;
DROP TABLE IF EXISTS BRANDS          CASCADE;
DROP TABLE IF EXISTS CATEGORIES      CASCADE;
DROP TABLE IF EXISTS IMAGES          CASCADE;

-- Enable pgcrypto for gen_random_uuid() if not already installed
CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE IMAGES (
    id          UUID            PRIMARY KEY DEFAULT gen_random_uuid(),
    filename    VARCHAR(255)    NOT NULL UNIQUE,
    directory   VARCHAR(20)     NOT NULL,
    width       INT             DEFAULT 0,
    height      INT             DEFAULT 0,
    type        VARCHAR(20)     NOT NULL,
    upload_at   TIMESTAMP       DEFAULT NOW()
);

CREATE TABLE USERS (
    id          UUID            PRIMARY KEY DEFAULT gen_random_uuid(),
    username    VARCHAR(100)    NOT NULL UNIQUE,
    password    VARCHAR(255)    NOT NULL,
    slug        VARCHAR(100)    NOT NULL UNIQUE,
    firstname   VARCHAR(100)    NOT NULL,
    lastname    VARCHAR(100)    NOT NULL,
    phone       VARCHAR(20),
    credit      INT             DEFAULT 0,
    roles       VARCHAR(20)     NOT NULL DEFAULT 'USER',
    enabled     BOOLEAN         DEFAULT TRUE,
    created_at  TIMESTAMP       DEFAULT NOW(),
    image_id    UUID            NOT NULL,

    CONSTRAINT CHK_USERS_CREDIT CHECK (credit >= 0),
    CONSTRAINT FK_USERS_IMAGES  FOREIGN KEY (image_id) REFERENCES IMAGES (id)
);

CREATE TABLE BRANDS (
    id      UUID            PRIMARY KEY DEFAULT gen_random_uuid(),
    name    VARCHAR(100)    NOT NULL UNIQUE
);

CREATE TABLE CATEGORIES (
    id      UUID            PRIMARY KEY DEFAULT gen_random_uuid(),
    slug    VARCHAR(100)    NOT NULL UNIQUE,
    label   VARCHAR(100)    NOT NULL
);

CREATE TABLE ITEMS (
    id          UUID            PRIMARY KEY DEFAULT gen_random_uuid(),
    model       VARCHAR(100)    NOT NULL,
    description TEXT,
    condition   VARCHAR(20)     NOT NULL DEFAULT 'NOT_SPECIFIED',
    year        INT             DEFAULT 0,
    is_gem      BOOLEAN         DEFAULT FALSE,
    user_id     UUID            NOT NULL,
    image_id    UUID            NOT NULL,
    brand_id    UUID            NOT NULL,
    category_id UUID            NOT NULL,

    CONSTRAINT CHK_ITEMS_YEAR       CHECK (year >= 1900 AND year <= EXTRACT(YEAR FROM NOW())),
    CONSTRAINT FK_ITEMS_USERS       FOREIGN KEY (user_id)       REFERENCES USERS (id),
    CONSTRAINT FK_ITEMS_IMAGES      FOREIGN KEY (image_id)      REFERENCES IMAGES (id),
    CONSTRAINT FK_ITEMS_BRANDS      FOREIGN KEY (brand_id)      REFERENCES BRANDS (id),
    CONSTRAINT FK_ITEMS_CATEGORIES  FOREIGN KEY (category_id)   REFERENCES CATEGORIES (id)
);

CREATE TABLE SALES (
    id              UUID            PRIMARY KEY DEFAULT gen_random_uuid(),
    slug            VARCHAR(100)    NOT NULL UNIQUE,
    created_at      TIMESTAMP       DEFAULT NOW(),
    started_at      TIMESTAMP       NOT NULL,
    ended_at        TIMESTAMP       NOT NULL,
    starting_price  INT             NOT NULL,
    current_price   INT             NOT NULL,
    likes           INT             DEFAULT 0,
    state           VARCHAR(20)     NOT NULL DEFAULT 'SCHEDULED',
    item_id         UUID            NOT NULL,

    CONSTRAINT CHK_SALES_DATES  CHECK (created_at <= started_at AND started_at < ended_at),
    CONSTRAINT CHK_SALES_PRICES CHECK (starting_price > 0 AND current_price >= starting_price),
    CONSTRAINT FK_SALES_ITEMS   FOREIGN KEY (item_id) REFERENCES ITEMS (id)
);

CREATE TABLE BIDS (
    id      UUID        PRIMARY KEY DEFAULT gen_random_uuid(),
    amount  INT         NOT NULL,
    time    TIMESTAMP   DEFAULT NOW(),
    user_id UUID        NOT NULL,
    sale_id UUID        NOT NULL,

    CONSTRAINT CHK_BIDS_AMOUNT  CHECK (amount > 0),
    CONSTRAINT FK_BIDS_USERS    FOREIGN KEY (user_id) REFERENCES USERS (id),
    CONSTRAINT FK_BIDS_SALES    FOREIGN KEY (sale_id) REFERENCES SALES (id)
);

CREATE TABLE REFRESH_TOKENS (
    id          UUID            PRIMARY KEY DEFAULT gen_random_uuid(),
    public_id   VARCHAR(32)     NOT NULL UNIQUE,
    username    VARCHAR(100)    NOT NULL,
    hash        VARCHAR(255)    NOT NULL,
    created_at  TIMESTAMP       DEFAULT NOW(),
    expires_at  TIMESTAMP       NOT NULL,

    CONSTRAINT CHK_REFRESH_DATES CHECK (created_at < expires_at)
);

-- Indexes
CREATE INDEX IDX_ITEMS_BRANDS       ON ITEMS(brand_id);
CREATE INDEX IDX_ITEMS_CATEGORY     ON ITEMS(category_id);
CREATE INDEX IDX_SALES_START        ON SALES(started_at DESC);
CREATE INDEX IDX_SALES_STATE        ON SALES(state);
CREATE INDEX IDX_SALES_ITEM         ON SALES(item_id);
CREATE INDEX IDX_BIDS_SALES         ON BIDS(sale_id);
CREATE INDEX IDX_BIDS_USERS         ON BIDS(user_id);
CREATE INDEX IDX_BIDS_TIME          ON BIDS(time DESC);
CREATE INDEX IDX_REFRESH_USERNAME   ON REFRESH_TOKENS(username);