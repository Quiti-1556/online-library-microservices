CREATE TABLE reviews (
                         id BIGINT NOT NULL AUTO_INCREMENT,
                         user_id BIGINT NOT NULL,
                         book_id BIGINT NOT NULL,
                         comment VARCHAR(500) NOT NULL,
                         rating INT NOT NULL,
                         PRIMARY KEY (id)
);