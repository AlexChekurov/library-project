CREATE SEQUENCE books_info_id_seq INCREMENT BY 1 MINVALUE 0;
CREATE TABLE IF NOT EXISTS books_info(
    id BIGINT PRIMARY KEY DEFAULT nextval('books_info_id_seq'),
    book_id BIGINT NOT NULL UNIQUE,
    booked_from TIMESTAMP,
    booked_to TIMESTAMP
);