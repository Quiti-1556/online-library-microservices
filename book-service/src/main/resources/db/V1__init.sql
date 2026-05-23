CREATE TABLE IF NOT EXISTS books (

    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255),
    author VARCHAR(255),
    price DOUBLE,
    stock INT
);