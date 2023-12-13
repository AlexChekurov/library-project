package ru.home.library.service.exception;

public class IntegrityViolationException extends RuntimeException {
    public IntegrityViolationException(Long isbn, Throwable cause) {
        super("Не удалось сохранить книгу с isbn = " + isbn + ", " + cause.getMessage(), cause);
    }
}
