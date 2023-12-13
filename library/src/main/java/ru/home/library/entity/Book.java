package ru.home.library.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Entity
@Table(name = "books")
@ToString
@Getter
@Setter
@Accessors(chain = true)
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "books_id_generator")
    @SequenceGenerator(name = "books_id_generator", sequenceName = "books_id_seq", allocationSize = 1)
    @Column(nullable = false)
    Long id;

    @Column(nullable = false)
    private Long isbn;

    @Column(nullable = false)
    private String title;

    private String genre;

    private String description;

    @Column
    private String author;

    private LocalDateTime lastUpdateDate = LocalDateTime.now();

    private boolean isSendToLibrary;

    private int sendTryCount;

}
