DROP TABLE IF EXISTS user_progress;
DROP TABLE IF EXISTS country;

CREATE TABLE country
(
    id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

INSERT INTO country (name)

VALUES ('TURKEY'),
       ('UNITED_STATES'),
       ('UNITED_KINGDOM'),
       ('GERMANY'),
       ('FRANCE');

CREATE TABLE user_progress
(
    id           BIGINT PRIMARY KEY,
    level        INT NOT NULL DEFAULT 1,
    coin_balance INT NOT NULL DEFAULT 0,
    created_at   TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    country_id   BIGINT,
    CONSTRAINT fk_country FOREIGN KEY (country_id) REFERENCES country (id)
);
