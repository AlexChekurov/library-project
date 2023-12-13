package ru.home.library.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.home.library.core.dto.BookDto;
import ru.home.library.entity.Book;
import ru.home.library.mapper.EntityMapper;
import ru.home.library.repository.BookRepository;
import ru.home.library.service.exception.IntegrityViolationException;

import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@Slf4j
@Service
public class BookService {

    private final BookRepository bookRepository;
    private final EntityMapper entityMapper;
    private final LibraryService libraryService;

    public List<BookDto> findAllBooks() {
        var result = bookRepository.findAll().stream()
                .map(entityMapper::toDto)
                .toList();
        log.info("По запросу на все книги найдено {} записей", result.size());
        return result;
    }

    public BookDto findById(Long id) {
        Objects.requireNonNull(id, "Id не должен быть равен null");

        return bookRepository.findById(id)
                .map(entityMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Не удалось найти книгу с id = " + id));
    }

    public BookDto findByIsbn(Long isbn) {
        Objects.requireNonNull(isbn, "ISBN не должен быть равен null");

        return bookRepository.findByIsbn(isbn)
                .map(entityMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Не удалось найти книгу с isbn = " + isbn));
    }

    public List<BookDto> findAllFreeBooks() {
        var booksIdList = libraryService.findFreeBooks();
        if (booksIdList.isEmpty()) {
            return List.of();
        }
        return bookRepository.findAllById(booksIdList).stream()
                .map(entityMapper::toDto).toList();
    }

    public Long save(BookDto bookDto) {
        var entity = entityMapper.toEntity(bookDto);
        log.info("Сохраняем книгу: {}", bookDto);
        try {
            return bookRepository.save(entity).getId();
        } catch (DataIntegrityViolationException dataIntegrityViolationException) {
            throw new IntegrityViolationException(bookDto.getIsbn(), dataIntegrityViolationException.getRootCause());
        }
    }

    @Transactional
    public void updateBook(BookDto bookDto) {
        bookRepository.findById(bookDto.getId())
                .map(entity -> updateEntityByRequest(entity, bookDto))
                .orElseThrow(() -> new EntityNotFoundException("Не удалось обновить книгу с id = " + bookDto.getId()));
    }

    public void takeBook(Long id) {
        log.info("Ищем книгу для бронирования с id= {}", id);
        var book = findById(id);
        libraryService.takeBook(book.getId());
    }

    public void returnBook(Long id) {
        log.info("Ищем книгу для разбронирования с id= {}", id);
        var book = findById(id);
        libraryService.returnBook(book.getId());
    }

    public void deleteBook(Long bookId) {
        log.info("Удаление книги с id = {}", bookId);
        bookRepository.deleteById(bookId);
        log.info("Книга удалена");
    }

    private Book updateEntityByRequest(Book entity, BookDto dto) {
        log.debug("Обновляем сущность({}) по дто({})", entity, dto);

        entity.setAuthor(dto.getAuthor());
        entity.setIsbn(dto.getIsbn());
        entity.setGenre(dto.getGenre());
        entity.setDescription(dto.getDescription());
        entity.setTitle(dto.getTitle());

        log.debug("Результат обновления: {}", entity);

        return entity;
    }
}
