package ru.home.libraryservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Entity
@Table(name = "books_info")
@ToString
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
public class BookInfo {

    public BookInfo(Long bookId) {
        this.bookId = bookId;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "books_info_id_generator")
    @SequenceGenerator(name = "books_info_id_generator", sequenceName = "books_info_id_seq", allocationSize = 1)
    @Column(nullable = false)
    Long id;

    @Column(nullable = false)
    private Long bookId;

    private LocalDateTime bookedFrom;

    private LocalDateTime bookedTo;

}
