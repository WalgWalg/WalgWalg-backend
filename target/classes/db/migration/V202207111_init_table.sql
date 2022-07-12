CREATE TABLE users (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    userid VARCHAR(63),
    password VARCHAR(63),
    nickname VARCHAR(63),
    address VARCHAR(63),
    salt VARCHAR(63),
    refresh_token VARCHAR(255),
    profile VARCHAR(255)
);

