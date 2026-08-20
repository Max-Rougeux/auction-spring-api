IF NOT EXISTS (SELECT name FROM sys.databases WHERE name = 'AUCTION')
BEGIN
    CREATE DATABASE AUCTION;
END
GO

USE AUCTION;
GO

IF OBJECT_ID('BIDS',            'U') IS NOT NULL DROP TABLE BIDS;
IF OBJECT_ID('SALES',           'U') IS NOT NULL DROP TABLE SALES;
IF OBJECT_ID('ITEMS',           'U') IS NOT NULL DROP TABLE ITEMS;
IF OBJECT_ID('REFRESH_TOKENS',  'U') IS NOT NULL DROP TABLE REFRESH_TOKENS;
IF OBJECT_ID('USERS',           'U') IS NOT NULL DROP TABLE USERS;
IF OBJECT_ID('BRANDS',          'U') IS NOT NULL DROP TABLE BRANDS;
IF OBJECT_ID('CATEGORIES',      'U') IS NOT NULL DROP TABLE CATEGORIES;
IF OBJECT_ID('IMAGES', 'U') IS NOT NULL DROP TABLE IMAGES;
GO

CREATE TABLE IMAGES (
    id              UNIQUEIDENTIFIER    PRIMARY KEY DEFAULT NEWID(),
    filename        VARCHAR(255)        NOT NULL UNIQUE,
    directory       VARCHAR(20)         NOT NULL,
    width           INT                 DEFAULT 0,
    height          INT                 DEFAULT 0,
    type            VARCHAR(20)         NOT NULL,
    upload_at       DATETIME2           DEFAULT SYSDATETIME(),
);

CREATE TABLE USERS (
    id              UNIQUEIDENTIFIER    PRIMARY KEY DEFAULT NEWID(),
    username        VARCHAR(100)        NOT NULL UNIQUE,
    password        VARCHAR(255)        NOT NULL,
    slug            VARCHAR(100)        NOT NULL UNIQUE,
    firstname       VARCHAR(100)        NOT NULL,
    lastname        VARCHAR(100)        NOT NULL,
    phone           VARCHAR(20),
    credit          INT                 DEFAULT 0,
    roles           VARCHAR(20)         NOT NULL DEFAULT 'USER',
    enabled         BIT                 DEFAULT 1,
    created_at      DATETIME2           DEFAULT SYSDATETIME(),
    image_id        UNIQUEIDENTIFIER    NOT NULL,

    CONSTRAINT CHK_USERS_CREDIT CHECK (credit >= 0),
    CONSTRAINT FK_USERS_IMAGES FOREIGN KEY (image_id) REFERENCES IMAGES (id),
);

CREATE TABLE BRANDS (
    id              UNIQUEIDENTIFIER    PRIMARY KEY DEFAULT NEWID(),
    name            VARCHAR(100)        NOT NULL UNIQUE,
);

CREATE TABLE CATEGORIES (
    id              UNIQUEIDENTIFIER    PRIMARY KEY DEFAULT NEWID(),
    slug            VARCHAR(100)        NOT NULL UNIQUE,
    label           VARCHAR(100)        NOT NULL,
);

CREATE TABLE ITEMS (
    id              UNIQUEIDENTIFIER    PRIMARY KEY DEFAULT NEWID(),
    model           VARCHAR(100)        NOT NULL,
    description     VARCHAR(MAX),
    condition       VARCHAR(20)         NOT NULL DEFAULT 'NOT_SPECIFIED',
    year            INT                 DEFAULT 0,
    is_gem          BIT                 DEFAULT 0,
    user_id         UNIQUEIDENTIFIER    NOT NULL,
    image_id        UNIQUEIDENTIFIER    NOT NULL,
    brand_id        UNIQUEIDENTIFIER    NOT NULL,
    category_id     UNIQUEIDENTIFIER    NOT NULL,

    CONSTRAINT CHK_ITEMS_YEAR       CHECK (year >= 1900 AND year <= YEAR(SYSDATETIME())),
    CONSTRAINT FK_ITEMS_USERS       FOREIGN KEY (user_id)           REFERENCES USERS (id),
    CONSTRAINT FK_ITEMS_IMAGES      FOREIGN KEY (image_id)          REFERENCES IMAGES (id),
    CONSTRAINT FK_ITEMS_BRANDS      FOREIGN KEY (brand_id)          REFERENCES BRANDS (id),
    CONSTRAINT FK_ITEMS_CATEGORIES  FOREIGN KEY (category_id)       REFERENCES CATEGORIES (id),
);

CREATE TABLE SALES (
    id              UNIQUEIDENTIFIER    PRIMARY KEY DEFAULT NEWID(),
    slug            VARCHAR(100)        NOT NULL UNIQUE,
    created_at      DATETIME2           DEFAULT SYSDATETIME(),
    started_at      DATETIME2           NOT NULL,
    ended_at        DATETIME2           NOT NULL,
    starting_price  INT                 NOT NULL,
    current_price   INT                 NOT NULL,
    likes           INT                 DEFAULT 0,
    state           VARCHAR(20)         NOT NULL DEFAULT 'SCHEDULED',
    item_id         UNIQUEIDENTIFIER    NOT NULL,

    CONSTRAINT CHK_SALES_DATES  CHECK (created_at <= started_at AND started_at < ended_at),
    CONSTRAINT CHK_SALES_PRICES CHECK (starting_price > 0       AND current_price >= starting_price),
    CONSTRAINT FK_SALES_ITEMS   FOREIGN KEY (item_id)           REFERENCES ITEMS (id),
);

CREATE TABLE BIDS (
    id              UNIQUEIDENTIFIER    PRIMARY KEY DEFAULT NEWID(),
    amount          INT                 NOT NULL,
    time            DATETIME2           DEFAULT SYSDATETIME(),
    user_id         UNIQUEIDENTIFIER    NOT NULL,
    sale_id         UNIQUEIDENTIFIER    NOT NULL,

    CONSTRAINT CHK_BIDS_AMOUNT  CHECK (amount > 0),
    CONSTRAINT FK_BIDS_USERS    FOREIGN KEY (user_id) REFERENCES USERS (id),
    CONSTRAINT FK_BIDS_SALES    FOREIGN KEY (sale_id) REFERENCES SALES (id),
);

CREATE TABLE REFRESH_TOKENS (
    id              UNIQUEIDENTIFIER    PRIMARY KEY DEFAULT NEWID(),
    public_id       VARCHAR(32)         NOT NULL UNIQUE,
    username        VARCHAR(100)        NOT NULL,
    hash            VARCHAR(255)        NOT NULL,
    created_at      DATETIME2           DEFAULT SYSDATETIME(),
    expires_at      DATETIME2           NOT NULL,

    CONSTRAINT CHK_REFRESH_DATES  CHECK (created_at < expires_at),
);
GO

CREATE INDEX IDX_ITEMS_BRANDS       ON ITEMS(brand_id);
CREATE INDEX IDX_ITEMS_CATEGORY     ON ITEMS(category_id);
CREATE INDEX IDX_SALES_START        ON SALES(started_at DESC);
CREATE INDEX IDX_SALES_STATE        ON SALES(state);
CREATE INDEX IDX_SALES_ITEM         ON SALES(item_id);
CREATE INDEX IDX_BIDS_SALES         ON BIDS(sale_id);
CREATE INDEX IDX_BIDS_USERS         ON BIDS(user_id);
CREATE INDEX IDX_BIDS_TIME          ON BIDS(time DESC);
CREATE INDEX IDX_REFRESH_USERNAME   ON REFRESH_TOKENS(username);
GO