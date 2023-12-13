package ru.home.libraryservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.home.libraryservice.entity.BookInfo;

import java.util.List;
import java.util.Optional;

public interface BookInfoRepository extends JpaRepository<BookInfo, Long> {

    Optional<BookInfo> findByBookId(Long isbn);

    List<BookInfo> findByBookedFromNullAndBookedToNull();

}
