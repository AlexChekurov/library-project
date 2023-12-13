package ru.home.libraryservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.home.library.core.dto.SuccessResponseDto;
import ru.home.libraryservice.entity.BookInfo;
import ru.home.libraryservice.exception.BookAlreadyExistsException;
import ru.home.libraryservice.repository.BookInfoRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class LibraryService {

    @Value("${library-service.booking.default-period-days}")
    private int defaultPeriodInDays = 10;

    private final BookInfoRepository bookInfoRepository;

    public SuccessResponseDto addBook(Long bookId) {
        log.info("Создаем запись для книги с id = {}", bookId);
        try {
            bookInfoRepository.save(new BookInfo(bookId));
            log.info("Создана запись для книги с id = {}", bookId);
            return new SuccessResponseDto(true, null);
        } catch (DataIntegrityViolationException exception) {
            log.error("Нарушение целостности данных при сохранении книги с id = {}", bookId, exception);
            throw new BookAlreadyExistsException(bookId);
        } catch (Exception ex) {
            log.error("Не удалось сохранить данные о книге с id = {}", bookId, ex);
            throw ex;
        }
    }

    @Transactional
    public SuccessResponseDto takeBook(Long bookId) {
        log.info("Бронируем книгу с id = {}", bookId);
        try {
            var bookInfo = bookInfoRepository.findByBookId(bookId).orElse(null);
            if (bookInfo == null) {
                return new SuccessResponseDto(false, "Не найдена книга с id = " + bookId);
            }
            if (bookInfo.getBookedFrom() != null) {
                return new SuccessResponseDto(false, "Книга еще не возвращена");
            }
            var bookingDate = LocalDateTime.now();
            var bookingEndDate = bookingDate.plusDays(defaultPeriodInDays);
            bookInfo
                    .setBookedFrom(bookingDate)
                    .setBookedTo(bookingEndDate);
            log.info("Книга с id = {} забронирована с {} до {}", bookId, bookingDate, bookingEndDate);
            return new SuccessResponseDto(true, null);
        } catch (Exception exception) {
            log.error("Не удалось взять книгу с id = {}", bookId, exception);
            throw exception;
        }
    }

    @Transactional
    public SuccessResponseDto returnBook(Long bookId) {
        log.info("Возвращаем книгу с id = {}", bookId);
        try {
            var bookInfo = bookInfoRepository.findByBookId(bookId).orElse(null);
            if (bookInfo == null) {
                return new SuccessResponseDto(false, "Не найдена книга с id = " + bookId);
            }
            bookInfo
                    .setBookedFrom(null)
                    .setBookedTo(null);
            log.info("Книга с id = {} разбронирована", bookInfo);
            return new SuccessResponseDto(true, null);
        } catch (Exception exception) {
            log.error("Не удалось взять книгу с id = {}", bookId, exception);
            throw exception;
        }
    }

    public List<Long> getFreeBooksList() {
        log.info("Запрашиваем список свободных книг");
        try {
            var idList = bookInfoRepository.findByBookedFromNullAndBookedToNull().stream()
                    .map(BookInfo::getBookId)
                    .toList();
            log.error("Получен спискок свободных книг: {}", idList);
            return idList;
        } catch (Exception exception) {
            log.error("Не удалось получить список свободных книг", exception);
            return List.of();
        }
    }
}
