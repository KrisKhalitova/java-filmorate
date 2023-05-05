package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();
    private long filmId = 1;
    private static final LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, 12, 28);

    @Override
    public Film addNewFilm(Film film) {
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

    @Override
    public List<Film> getAllFilms() {
        log.info("Request of getting all films is received.");
        return new ArrayList<>(films.values());
    }

    @Override
    public Film updateFilm(Film film) {
        if (films.get(film.getId()) == null) {
            log.warn("It's not possible to update non-existent film");
            throw new NotFoundException("It's not possible to update non-existent film");
        }
        validateFilm(film);
        films.put(film.getId(), film);
        log.info("The film {} is updated.", film);
        return films.get(film.getId());
    }

    @Override
    public Film getFilmById(long filmId) {
        if (!films.containsKey(filmId)) {
            log.warn("The film with filmId {} doesn't exist", filmId);
            throw new NotFoundException("The film doesn't exist");
        } else {
            return films.get(filmId);
        }
    }

    public static void validateFilm(Film film) {
        if (film.getReleaseDate().isBefore(MIN_RELEASE_DATE)) {
            log.warn("The release date of the movie should be after " + MIN_RELEASE_DATE + ".");
            throw new ValidationException("The release date of the movie should be after " + MIN_RELEASE_DATE + ".");
        }
    }
}