package ru.home.libraryservice.rest;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import ru.home.library.core.dto.BookRequestDto;
import ru.home.library.core.dto.SuccessResponseDto;
import ru.home.libraryservice.service.LibraryService;

import java.util.List;

import static ru.home.library.core.constant.CommonConstants.ROLE_ADMIN;
import static ru.home.library.core.constant.CommonConstants.ROLE_USER;

@RestController
@RequestMapping("api/v1/library/books")
@AllArgsConstructor
public class LibraryController {

    private final LibraryService libraryService;

    @PostMapping
    @Secured({ROLE_ADMIN})
    @Operation(summary = "Добавить информацию о книге")
    public SuccessResponseDto addBook(@RequestBody @Valid BookRequestDto request) {
        return libraryService.addBook(request.getId());
    }

    @PutMapping
    @Secured({ROLE_ADMIN})
    @Operation(summary = "Забронировать книгу")
    public SuccessResponseDto takeBook(@RequestBody @Valid BookRequestDto request) {
        return libraryService.takeBook(request.getId());
    }

    @PutMapping("/free")
    @Secured({ROLE_ADMIN})
    @Operation(summary = "Вернуть книгу")
    public SuccessResponseDto returnBook(@RequestBody @Valid BookRequestDto request) {
        return libraryService.returnBook(request.getId());
    }

    @GetMapping("/free")
    @Secured({ROLE_USER})
    @Operation(summary = "Получить список идентификаторов свободных книг")
    public List<Long> getFreeBooksList() {
        return libraryService.getFreeBooksList();
    }

}
