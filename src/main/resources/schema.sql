DROP TABLE IF EXISTS customer_541455;

CREATE TABLE customer_541455
(
    id varchar(36) NOT NULL,
    email varchar(200) NOT NULL,
    first_name varchar(200) NOT NULL,
    last_name varchar(200) NOT NULL,
    created date NOT NULL,
    modified date NOT NULL,
    PRIMARY KEY (id)
);