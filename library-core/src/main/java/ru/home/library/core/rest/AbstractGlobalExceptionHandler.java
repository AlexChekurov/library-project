package ru.home.library.core.rest;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import ru.home.library.core.dto.ErrorMessageDto;

import java.util.stream.Collectors;

@Slf4j
public class AbstractGlobalExceptionHandler {

    private static final String COMMON_ERROR_TEMPLATE = "Ошибка при обработке запроса: {}";
    private static final String DEFAULT_ERROR_MSG = "Внутренняя ошибка сервиса";

    @ExceptionHandler(value = EntityNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorMessageDto entityNotFoundException(EntityNotFoundException ex, WebRequest request) {
        log.error(COMMON_ERROR_TEMPLATE, request, ex);
        return new ErrorMessageDto()
                .setStatus(HttpStatus.NOT_FOUND)
                .setMessage(ex.getMessage());
    }

    @ExceptionHandler(value = NoResourceFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorMessageDto resourceNotFoundException(NoResourceFoundException ex, WebRequest request) {
        log.error(COMMON_ERROR_TEMPLATE, request, ex);
        return new ErrorMessageDto()
                .setStatus(HttpStatus.NOT_FOUND)
                .setMessage(ex.getMessage());
    }

    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorMessageDto humanReadableException(HttpMessageNotReadableException ex, WebRequest request) {
        log.error(COMMON_ERROR_TEMPLATE, request, ex);
        return new ErrorMessageDto()
                .setStatus(HttpStatus.BAD_REQUEST)
                .setMessage(ex.getMessage());
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorMessageDto invalidArgumentsException(MethodArgumentNotValidException ex, WebRequest request) {
        log.error(COMMON_ERROR_TEMPLATE, request, ex);
        var error = ex.getFieldErrors().stream()
                .map(fieldError -> "Поле " + fieldError.getField() + " " + fieldError.getDefaultMessage())
                .collect(Collectors.joining(". "));
        return new ErrorMessageDto()
                .setStatus(HttpStatus.BAD_REQUEST)
                .setMessage(error);
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public ErrorMessageDto accessDeniedExceptionHandler(AccessDeniedException ex, WebRequest request) {
        log.error(COMMON_ERROR_TEMPLATE, request, ex);
        return new ErrorMessageDto()
                .setStatus(HttpStatus.FORBIDDEN)
                .setMessage("Доступ запрещен!");
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMessageDto globalExceptionHandler(Exception ex, WebRequest request) {
        log.error(COMMON_ERROR_TEMPLATE, request, ex);
        return new ErrorMessageDto()
                .setStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .setMessage(DEFAULT_ERROR_MSG);
    }
}