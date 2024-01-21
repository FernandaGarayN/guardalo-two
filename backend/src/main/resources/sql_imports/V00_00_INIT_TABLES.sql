CREATE TABLE products
(
    id             BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    sku            VARCHAR                              NOT NULL,
    name           VARCHAR                              NOT NULL,
    description    VARCHAR                              NOT NULL,
    price          FLOAT                              NOT NULL,
    stock          INT                                  NOT NULL,
    CONSTRAINT pk_product PRIMARY KEY (id)
);

CREATE TABLE requests
(
    id              BIGINT GENERATED BY DEFAULT AS IDENTITY  NOT NULL,
    warehouse      VARCHAR                               NOT NULL,
    subsidiary      VARCHAR                               NOT NULL,
    address         VARCHAR                               NOT NULL,
    date            DATE                                  NOT NULL,
    order_number    BIGINT                               NOT NULL,
    track_code      VARCHAR                              ,
    transport       VARCHAR                              ,
    CONSTRAINT pk_requests PRIMARY KEY (id)
);

CREATE TABLE request_details
(
    id              BIGINT GENERATED BY DEFAULT AS IDENTITY  NOT NULL,
    request_id      BIGINT                                   NOT NULL,
    product_id      BIGINT                                   NOT NULL,
    quantity        BIGINT                                   NOT NULL,
    CONSTRAINT pk_request_details PRIMARY KEY (id),
    CONSTRAINT fk_product_id FOREIGN KEY (product_id) REFERENCES products (id),
    CONSTRAINT fk_request_id FOREIGN KEY (request_id) REFERENCES requests (id)
);

CREATE TABLE request_details_mp
(
    id              BIGINT GENERATED BY DEFAULT AS IDENTITY  NOT NULL,
    sku             VARCHAR                                  NOT NULL,
    name            VARCHAR                                  NOT NULL,
    description     VARCHAR                                  NOT NULL,
    price           FLOAT                                    NOT NULL,
    quantity        BIGINT                                   NOT NULL,
    request_id      BIGINT                                   NOT NULL,
    CONSTRAINT pk_request_details_mp PRIMARY KEY (id),
    CONSTRAINT fk_request_id_mp FOREIGN KEY (request_id) REFERENCES requests (id)
);

CREATE TABLE users
(
    id             BIGINT GENERATED BY DEFAULT AS IDENTITY  NOT NULL,
    user_name      VARCHAR                                  NOT NULL,
    password       VARCHAR                                  NOT NULL,
    subsidiary     VARCHAR                                  NOT NULL,
    address        VARCHAR                                  NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (id)
);
