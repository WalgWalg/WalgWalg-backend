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

CREATE TABLE walk (
    walk_id VARCHAR(63) NOT NULL,
    walk_date DATE,
    step_count BIGINT,
    distance BIGINT,
    calorie BIGINT,
    walk_time VARCHAR(63),
    course VARCHAR(255),
    location VARCHAR(63),
    address VARCHAR(63),
    user_id BIGINT,
    FOREIGN KEY(user_id) REFERENCES users(id);
);

CREATE TABLE gps (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    latitude VARCHAR(63),
    longitude VARCHAR(63),
    walk_id VARCHAR(63),
    FOREIGN KEY(walk_id) REFERENCES walk(walk_id);
);

CREATE TABLE board (
    board_id VARCHAR(63) NOT NULL,
    title VARCHAR(63),
    board_date TIMESTAMP,
    contents VARCHAR(255),
    walk_id VARCHAR(63),
    FOREIGN KEY(walk_id) REFERENCES walk(walk_id),
    user_id VARCHAR(63),
    FOREIGN KEY(user_id) REFERENCES users(user_id);
);

CREATE TABLE hashtag (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    tag VARCHAR(63),
    board_id VARCHAR(63),
    FOREIGN KEY(board_id) REFERENCES board(board_id);
);

CREATE TABLE likes (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    board_id VARCHAR(63),
    FOREIGN KEY(board_id) REFERENCES board(board_id),
    user_id VARCHAR(63),
    FOREIGN KEY(user_id) REFERENCES users(user_id);
);

CREATE TABLE scrap (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    board_id VARCHAR(63),
    FOREIGN KEY(board_id) REFERENCES board(board_id),
    user_id VARCHAR(63),
    FOREIGN KEY(user_id) REFERENCES users(user_id);
);

CREATE TABLE park (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    park_name VARCHAR(63),
    road_address VARCHAR(63),
    number_address VARCHAR(63),
    latitude VARCHAR(63),
    longitude VARCHAR(63)
);

CREATE INDEX idx_park_number_address ON park (number_address);