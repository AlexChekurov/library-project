package ru.home.libraryservice.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import ru.home.library.core.dto.ErrorMessageDto;
import ru.home.library.core.rest.AbstractGlobalExceptionHandler;
import ru.home.libraryservice.exception.BookAlreadyExistsException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends AbstractGlobalExceptionHandler {

    private static final String COMMON_ERROR_TEMPLATE = "Ошибка при обработке запроса: {}";
    private static final String DEFAULT_ERROR_MSG = "Внутренняя ошибка сервиса";

    @ExceptionHandler(value = BookAlreadyExistsException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorMessageDto bookAlreadyExistsException(BookAlreadyExistsException ex, WebRequest request) {
        log.error(COMMON_ERROR_TEMPLATE, request, ex);
        return new ErrorMessageDto()
                .setStatus(HttpStatus.BAD_REQUEST)
                .setMessage(ex.getMessage());
    }
}