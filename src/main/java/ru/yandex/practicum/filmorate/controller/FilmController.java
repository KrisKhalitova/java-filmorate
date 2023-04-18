package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
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
    private static int filmId = 1;
    private final LocalDate minReleaseDate = LocalDate.of(1895, 12, 28);
//    private final static int MAX_SYMBOLS = 200;

    @PostMapping
    public Film addNewFilm(@Valid @RequestBody Film film) {
        if (films.containsKey(film.getId())) {
            log.warn("This film has already created.");
            throw new ValidationException("This film has already created.");
        } else {
            validateFilm(film);
            film.setId(filmId);
            filmId++;
            films.put(film.getId(), film);
            log.info("A new film {} is added.", film);
        }
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
            throw new ValidationException("It's not possible to update non-existent film");
        }
        validateFilm(film);
        films.put(film.getId(), film);
        log.info("The film {} is updated.", film);
        return films.get(film.getId());
    }

    public void validateFilm(Film film) {
        if (film.getReleaseDate().isBefore(minReleaseDate)) {
            log.warn("The release date of the movie should be after " + minReleaseDate + ".");
            throw new ValidationException("The release date of the movie should be after " + minReleaseDate + ".");
        }
//        if (film.getName().isBlank()) {
//          log.warn("The film title shouldn't be empty.");
//          throw new ValidationException("The film title shouldn't be empty.");
//        }
//        if (film.getDescription().length() > MAX_SYMBOLS) {
//          log.warn("The film description length should be less than " + MAX_SYMBOLS +
//                  "characters.");
//          throw new ValidationException("The film description length should be less than " + MAX_SYMBOLS +
//                  "characters.");
//        }
//        if (film.getDuration() < 0) {
//            log.warn("The description of the film has should more than 0 minutes.");
//            throw new ValidationException("The description of the film should be more than 0 minutes.");
//        }
    }
}
