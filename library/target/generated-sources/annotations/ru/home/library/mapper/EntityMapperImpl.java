package ru.home.library.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import ru.home.library.core.dto.BookDto;
import ru.home.library.entity.Book;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-12-09T14:45:54+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 19.0.2 (Eclipse Adoptium)"
)
@Component
public class EntityMapperImpl implements EntityMapper {

    @Override
    public BookDto toDto(Book entity) {
        if ( entity == null ) {
            return null;
        }

        BookDto bookDto = new BookDto();

        bookDto.setId( entity.getId() );
        bookDto.setIsbn( entity.getIsbn() );
        bookDto.setTitle( entity.getTitle() );
        bookDto.setGenre( entity.getGenre() );
        bookDto.setDescription( entity.getDescription() );
        bookDto.setAuthor( entity.getAuthor() );

        return bookDto;
    }

    @Override
    public Book toEntity(BookDto bookDto) {
        if ( bookDto == null ) {
            return null;
        }

        Book book = new Book();

        book.setIsbn( bookDto.getIsbn() );
        book.setTitle( bookDto.getTitle() );
        book.setGenre( bookDto.getGenre() );
        book.setDescription( bookDto.getDescription() );
        book.setAuthor( bookDto.getAuthor() );

        return book;
    }
}
