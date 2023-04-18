package ru.yandex.practicum.filmorate.controllerTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {

    private Film film;
    private Film film2;
    private FilmController filmController;
    private ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private Validator validator = factory.getValidator();

    @BeforeEach
    public void beforeEach() {
        filmController = new FilmController();
        film = new Film("Film", "Description of the first film",
                LocalDate.of(1975, 3, 1), 100);
        film2 = new Film("Second Film", "Description of the second film",
                LocalDate.of(2000, 12, 5), 124);
    }

    @Test
    void shouldAddNewFilm() {
        filmController.addNewFilm(film);
        List<Film> actual = filmController.getAllFilms();

        assertEquals(1, actual.size());
        assertEquals(film.getId(), actual.get(0).getId());
        assertEquals(film.getName(), actual.get(0).getName());
        assertEquals(film.getDescription(), actual.get(0).getDescription());
        assertEquals(film.getReleaseDate(), actual.get(0).getReleaseDate());
        assertEquals(film.getDuration(), actual.get(0).getDuration());
    }

    @Test
    void shouldAddNewFilmWithIncorrectName() {
        filmController.addNewFilm(film);
        film.setName("");

        assertEquals(1, validator.validate(film).size());
    }

    @Test
    void shouldAddNewFilmWithIncorrectDescription() {
        filmController.addNewFilm(film);
        String newDescr = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa".repeat(3);
        film.setDescription(newDescr);

        assertEquals(1, validator.validate(film).size());
    }

    @Test
    void shouldAddNewFilmWithIncorrectReleaseDate() {
        filmController.addNewFilm(film);
        film.setReleaseDate(LocalDate.of(1700, 10, 8));

        assertThrows(ValidationException.class, () -> {
            filmController.validateFilm(film);
        });
    }

    @Test
    void shouldAddNewFilmWithIncorrectDuration() {
        filmController.addNewFilm(film);
        film.setDuration(-100);

        assertEquals(1, validator.validate(film).size());
    }

    @Test
    void shouldGetAllFilms() {
        filmController.addNewFilm(film);
        filmController.addNewFilm(film2);
        List<Film> actual = filmController.getAllFilms();

        assertEquals(2, actual.size());
        assertTrue(actual.contains(film));
        assertTrue(actual.contains(film2));
    }

    @Test
    void shouldUpdateFilm() {
        filmController.addNewFilm(film);
        film.setName("New Name of The film");
        film.setDescription("New Description");
        film.setReleaseDate(LocalDate.of(2014, 6, 7));
        film.setDuration(140);

        filmController.updateFilm(film);
        List<Film> actual = filmController.getAllFilms();

        assertEquals(1, actual.size());
        assertEquals(film.getId(), actual.get(0).getId());
        assertEquals(film.getName(), actual.get(0).getName());
        assertEquals(film.getDescription(), actual.get(0).getDescription());
        assertEquals(film.getReleaseDate(), actual.get(0).getReleaseDate());
        assertEquals(film.getDuration(), actual.get(0).getDuration());
    }

    @Test
    void shouldUpdateNonExistedFilm() {
        assertThrows(ValidationException.class, () -> {
            filmController.updateFilm(film);
        });
    }
}
