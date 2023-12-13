package ru.home.libraryservice.exception;

public class BookAlreadyExistsException extends RuntimeException {
    public BookAlreadyExistsException(Long id) {
        super("Уже существует книга с id = " + id);
    }
}
