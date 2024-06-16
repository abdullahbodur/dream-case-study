CREATE TABLE tournament
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    start_time   TIMESTAMP NOT NULL,
    end_time     TIMESTAMP NOT NULL,
    is_completed BOOLEAN   NOT NULL DEFAULT FALSE,
    created_at   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);


CREATE TABLE tournament_group
(
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    tournament_id BIGINT    NOT NULL,
    is_ready      BOOLEAN   NOT NULL DEFAULT FALSE,
    FOREIGN KEY (tournament_id) REFERENCES tournament (id),
    created_at    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE participation
(
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id       BIGINT    NOT NULL,
    group_id      BIGINT    NOT NULL,
    score         INT       NOT NULL,
    created_at    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user_progress (id),
    FOREIGN KEY (group_id) REFERENCES tournament_group (id)
);

CREATE TABLE reward
(
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    tournament_id BIGINT    NOT NULL,
    group_id      BIGINT    NOT NULL,
    user_id       BIGINT    NOT NULL,
    current_rank  INT       NOT NULL,
    amount        INT       NOT NULL,
    is_claimed    BOOLEAN   NOT NULL DEFAULT FALSE,
    created_at    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (tournament_id) REFERENCES tournament (id),
    FOREIGN KEY (group_id) REFERENCES tournament_group (id),
    FOREIGN KEY (user_id) REFERENCES user_progress (id)
);

