CREATE TABLE Posts
(
    id            BIGINT       NOT NULL AUTO_INCREMENT,
    title         VARCHAR(500) NOT NULL,
    content       TEXT         NOT NULL,
    author        VARCHAR(255),
    created_date  DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified_date DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);
