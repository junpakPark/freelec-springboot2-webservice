CREATE TABLE IF NOT EXISTS Posts
(
    id            BIGINT       NOT NULL AUTO_INCREMENT,
    title         VARCHAR(500) NOT NULL,
    content       TEXT         NOT NULL,
    author        VARCHAR(255),
    created_date  DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified_date DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);
CREATE TABLE IF NOT EXISTS User
(
    id         BIGINT                                         NOT NULL AUTO_INCREMENT,
    name       VARCHAR(255)                                   NOT NULL,
    email      VARCHAR(255)                                   NOT NULL,
    picture    VARCHAR(255),
    role       ENUM ('ROLE_USER', 'ROLE_ADMIN', 'ROLE_GUEST') NOT NULL,
    created_at DATETIME(6)                                    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME(6)                                    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);
