CREATE SEQUENCE IF NOT EXISTS books_id_seq INCREMENT BY 1 MINVALUE 0;
CREATE TABLE IF NOT EXISTS books(
    id BIGINT PRIMARY KEY DEFAULT nextval('books_id_seq'),
    isbn BIGINT NOT NULL UNIQUE,
    title VARCHAR(255) NOT NULL,
    genre VARCHAR(255),
    description VARCHAR(255),
    author VARCHAR(255),
    last_update_date TIMESTAMP NOT NULL DEFAULT NOW(),
    is_send_to_library BOOLEAN NOT NULL DEFAULT FALSE,
    send_try_count SMALLINT NOT NULL DEFAULT 0
);