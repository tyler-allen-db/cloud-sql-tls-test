DROP TABLE people IF EXISTS;

CREATE TABLE people  (
    first_name VARCHAR(20) NOT NULL PRIMARY KEY,
    sex VARCHAR(2),
    qty integer
);