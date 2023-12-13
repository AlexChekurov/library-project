package ru.home.library.exception;

public class BookingException extends RuntimeException {
    public BookingException(Long bookId, String info) {
        super("Не удалось изменить информацию о броинровании книги с id = "
                + bookId + (info == null ? "." : ": " + info));
    }
}
