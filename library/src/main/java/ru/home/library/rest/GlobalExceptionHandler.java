package ru.home.library.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import ru.home.library.core.dto.ErrorMessageDto;
import ru.home.library.core.rest.AbstractGlobalExceptionHandler;
import ru.home.library.exception.BookingException;
import ru.home.library.service.exception.IntegrityViolationException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends AbstractGlobalExceptionHandler {

    private static final String COMMON_ERROR_TEMPLATE = "Ошибка при обработке запроса: {}";


    @ExceptionHandler(value = IntegrityViolationException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorMessageDto integrityViolationException(IntegrityViolationException ex, WebRequest request) {
        log.error(COMMON_ERROR_TEMPLATE, request, ex);
        return new ErrorMessageDto()
                .setStatus(HttpStatus.BAD_REQUEST)
                .setMessage(ex.getMessage());
    }

    @ExceptionHandler(value = BookingException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorMessageDto bookingException(BookingException ex, WebRequest request) {
        log.error(COMMON_ERROR_TEMPLATE, request, ex);
        return new ErrorMessageDto()
                .setStatus(HttpStatus.BAD_REQUEST)
                .setMessage(ex.getMessage());
    }
}