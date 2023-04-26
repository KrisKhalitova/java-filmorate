package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
public class Film {
    private int id;
    @NotBlank (message = "The film title shouldn't be empty.")
    private String name;
    @Size(min = 1, max = 200, message = "The film description length should be less than 200 characters.")
    private String description;
    @NotNull
    private LocalDate releaseDate; //yyyy-MM-dd.
    @PositiveOrZero (message = "The description of the film should be more than 0 minutes.")
    private Integer duration;

    public Film(String name, String description, LocalDate releaseDate, Integer duration) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }
}