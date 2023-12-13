package ru.home.library.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.home.library.client.LibraryServiceFeignClient;
import ru.home.library.core.dto.BookRequestDto;
import ru.home.library.exception.BookingException;
import ru.home.library.repository.BookRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.data.domain.Sort.Direction.ASC;

@Service
@Slf4j
@RequiredArgsConstructor
public class LibraryService {
    private static final Pageable DEFAULT_PAGE_REQUEST = PageRequest.of(0, 1, Sort.by(ASC, "lastUpdateDate"));

    private final LibraryServiceFeignClient libraryServiceFeignClient;
    private final BookRepository bookRepository;

    @Value("${library-service.scheduler.max-attempts}")
    private int maxAttemptsToSend = 5;

    @Scheduled(fixedDelayString = "${library-service.scheduler.fixed-delay-duration}")
    @Transactional
    public void sendBookToLibraryService() {
        log.info("Старт шедулера отправки книг");
        var booksList = bookRepository.findBookToSend(maxAttemptsToSend, DEFAULT_PAGE_REQUEST);
        if (booksList == null || booksList.size() == 0) {
            log.info("Нечего отправлять в library-service");
            return;
        }
        var book = booksList.get(0);
        book
                .setLastUpdateDate(LocalDateTime.now())
                .setSendTryCount(book.getSendTryCount() + 1)
                .setSendToLibrary(sendNewBookToLibraryService(book.getId()));
        log.debug("Результат отправки книги с id = {} в library-service: {}, попыток: {}",
                book.getId(), book.isSendToLibrary(), book.getSendTryCount());
        log.info("Стоп шедулера отправки книг");
    }

    public boolean takeBook(Long bookId) {
        return takeBookInLibraryService(bookId);
    }

    public boolean returnBook(Long bookId) {
        return returnBookInLibraryService(bookId);
    }

    public List<Long> findFreeBooks() {
        return getFreeBooksList();
    }


    private boolean sendNewBookToLibraryService(Long bookId) {
        log.info("Отправляем книгу с id = {} в library-service", bookId);
        try {
            libraryServiceFeignClient.sendNewBookInfo(new BookRequestDto(bookId));
            log.info("Книга с id = {} успешно отправлена в library-service", bookId);
            return true;
        } catch (Exception exception) {
            log.error("Не удалось отпправить информацию о книге с id = {} в library-service", bookId);
            return false;
        }
    }

    private boolean takeBookInLibraryService(Long bookId) {
        log.info("Отправляем запрос на бронирование книги с id = {}", bookId);
        try {
            var bookingResult = libraryServiceFeignClient.takeBook(new BookRequestDto(bookId));
            if (!bookingResult.success()) {
                throw new BookingException(bookId, bookingResult.info());
            }
            log.info("Книга с id = {} успешно забронирована в library-service", bookId);
            return true;
        } catch (Exception exception) {
            log.error("Не удалось забронировать книгу с id = {} в library-service", bookId, exception);
            throw exception;
        }
    }

    private List<Long> getFreeBooksList() {
        log.info("Отправляем запрос на поиск свободных книг");
        try {
            var idList = libraryServiceFeignClient.getFreeBooksList();
            log.info("Список свободных книг: {}", idList);
            return idList;
        } catch (Exception exception) {
            log.error("Не удалось запросить список свободных книг в library-service", exception);
            throw exception;
        }
    }

    private boolean returnBookInLibraryService(Long bookId) {
        log.info("Отправляем запрос на разбронирование книги с id = {}", bookId);
        try {
            var bookingResult = libraryServiceFeignClient.returnBook(new BookRequestDto(bookId));
            if (!bookingResult.success()) {
                throw new BookingException(bookId, bookingResult.info());
            }
            log.info("Книга с id = {} успешно разбронирована в library-service", bookId);
            return true;
        } catch (Exception exception) {
            log.error("Не удалось разбронировать книгу с id = {} в library-service", bookId, exception);
            throw exception;
        }
    }
}
