package ru.home.library.rest;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import ru.home.library.core.dto.BookDto;
import ru.home.library.service.BookService;

import java.util.List;

import static ru.home.library.core.constant.CommonConstants.ROLE_ADMIN;
import static ru.home.library.core.constant.CommonConstants.ROLE_USER;


@RestController
@RequestMapping("api/v1/books")
@AllArgsConstructor
public class BooksController {

    private final BookService bookService;

    @GetMapping
    @Secured({ROLE_USER})
    @Operation(summary = "Найти все книги")
    public List<BookDto> findAllBooks() {
        return bookService.findAllBooks();
    }

    @GetMapping("/free")
    @Secured({ROLE_USER})
    @Operation(summary = "Найти все свободные книги")
    public List<BookDto> findAllFreeBooks() {
        return bookService.findAllFreeBooks();
    }

    @GetMapping(path = "/{id}")
    @Secured({ROLE_USER})
    @Operation(summary = "Найти книгу по ID")
    public BookDto findBookById(@PathVariable Long id) {
        return bookService.findById(id);
    }

    @GetMapping(path = "/isbn/{isbn}")
    @Secured({ROLE_USER})
    @Operation(summary = "Найти книгу по ISBN")
    public BookDto findBookByIsbn(@PathVariable Long isbn) {
        return bookService.findByIsbn(isbn);
    }

    @PostMapping
    @Secured({ROLE_ADMIN})
    @Operation(summary = "Внести данные о новой книге")
    public Long createBook(@Valid @RequestBody BookDto bookDto) {
        return bookService.save(bookDto);
    }

    @PutMapping
    @Secured({ROLE_ADMIN})
    @Operation(summary = "Обновить данные о книге")
    public void updateBook(@Valid @RequestBody BookDto bookDto) {
        bookService.updateBook(bookDto);
    }

    @PutMapping(path = "/{id}")
    @Secured({ROLE_USER})
    @Operation(summary = "Взять книгу")
    public void takeBook(@PathVariable Long id) {
        bookService.takeBook(id);
    }

    @PutMapping(path = "free/{id}")
    @Secured({ROLE_USER})
    @Operation(summary = "Вернуть книгу")
    public void returnBook(@PathVariable Long id) {
        bookService.returnBook(id);
    }

    @DeleteMapping(path = "/{id}")
    @Secured({ROLE_ADMIN})
    @Operation(summary = "Удалить книгу")
    public void deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
    }
}
