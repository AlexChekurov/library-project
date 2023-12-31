package ru.home.library.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.home.library.core.dto.BookDto;
import ru.home.library.entity.Book;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface EntityMapper {

    BookDto toDto(Book entity);

    @Mapping(target = "id", ignore = true)
    Book toEntity(BookDto bookDto);
}
