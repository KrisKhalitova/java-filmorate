package ru.yandex.practicum.filmorate.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@ToString
public class Film {
    private Long id;
    @NotBlank(message = "The film title shouldn't be empty.")
    private String name;
    @Size(min = 1, max = 200, message = "The film description length should be less than 200 characters.")
    private String description;
    @NotNull
    private LocalDate releaseDate; //yyyy-MM-dd.
    @PositiveOrZero(message = "The duration of the film should be more than 0 minutes.")
    private Integer duration;
    private Set<Long> likes = new HashSet<>();
    private Mpa mpa;
    private Set<Genre> genres = new LinkedHashSet<>();

    public Film(String name, String description, LocalDate releaseDate, Integer duration) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Film)) return false;
        Film film = (Film) o;
        return Objects.equals(id, film.id) && Objects.equals(name, film.name) && Objects.equals(description, film.description) && Objects.equals(releaseDate, film.releaseDate) && Objects.equals(duration, film.duration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, releaseDate, duration);
    }
}