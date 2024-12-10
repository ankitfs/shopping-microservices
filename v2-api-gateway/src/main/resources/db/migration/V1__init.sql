CREATE TABLE t_users (
    id bigint NOT NULL AUTO_INCREMENT,
    name varchar(100) DEFAULT NULL,
    email varchar(100) DEFAULT NULL,
    password varchar(1000),
    created_date date,
    PRIMARY KEY (id)
);