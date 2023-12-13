package ru.home.library.core.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Data
@Accessors(chain = true)
public class BookDto {

    private Long id;

    @NotNull
    private Long isbn;

    @NotNull
    private String title;

    private String genre;

    private String description;

    private String author;
}
