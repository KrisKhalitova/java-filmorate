package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private int filmId = 1;
    private static final LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, 12, 28);

    @PostMapping
    public Film addNewFilm(@Valid @RequestBody Film film) {
        if (films.containsValue(film)) {
            log.warn("This film has been already created.");
            throw new ValidationException("This film has been already created.");
        }
        validateFilm(film);
        film.setId(filmId++);
        films.put(film.getId(), film);
        log.info("A new film {} is added.", film);
        return films.get(film.getId());
    }

    @GetMapping
    public List<Film> getAllFilms() {
        log.info("Request of getting all films is received.");
        return new ArrayList<>(films.values());
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        if (films.get(film.getId()) == null) {
            log.warn("It's not possible to update non-existent film");
            throw new NotFoundException("It's not possible to update non-existent film");
        }
        validateFilm(film);
        films.put(film.getId(), film);
        log.info("The film {} is updated.", film);
        return films.get(film.getId());
    }

    public void validateFilm(Film film) {
        if (film.getReleaseDate().isBefore(MIN_RELEASE_DATE)) {
            log.warn("The release date of the movie should be after " + MIN_RELEASE_DATE + ".");
            throw new ValidationException("The release date of the movie should be after " + MIN_RELEASE_DATE + ".");
        }
    }
}
