DROP TABLE people IF EXISTS;

CREATE TABLE people  (
    first_name BIGINT IDENTITY NOT NULL PRIMARY KEY,
    sex VARCHAR(2),
    qty integer,
    year integer
);