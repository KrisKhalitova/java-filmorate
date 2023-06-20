package ru.yandex.practicum.filmorate.dbStorageTests;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.entity.Film;
import ru.yandex.practicum.filmorate.entity.Genre;
import ru.yandex.practicum.filmorate.entity.Mpa;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmStorageTest {

    private final FilmDbStorage filmDbStorage;
    private final JdbcTemplate jdbcTemplate;
    private final GenreDbStorage genreDbStorage;
    Film film;
    Film film2;
    Film film3;

    @BeforeEach
    void clearDb() {
        String sql = "DELETE FROM film_genre";
        jdbcTemplate.update(sql);
        sql = "DELETE FROM films";
        jdbcTemplate.update(sql);
        film = new Film("Film 1", "Description 1", LocalDate.of(2005, 10, 4),
                80);
        film.setMpa(new Mpa(1, "G"));
        film2 = new Film("Film 2", "Description 2", LocalDate.of(2000, 3, 15),
                210);
        film2.setMpa(new Mpa(2, "PG"));
        film3 = new Film("Film 3", "Description 3", LocalDate.of(2015, 6, 28),
                90);
        film3.setMpa(new Mpa(4, "R"));
    }

    @Test
    void addFilmTest() {
        film = filmDbStorage.addNewFilm(film);
        List<Film> films = filmDbStorage.getAllFilms();

        assertEquals(films.size(), 1);
        assertTrue(films.contains(film));
    }

    @Test
    void updateFilmTest() {
        film = filmDbStorage.addNewFilm(film);
        film.setName("othername");
        film.setDuration(120);
        film.setDescription("other description");
        film.setReleaseDate(LocalDate.of(2006, 10, 4));
        film = filmDbStorage.updateFilm(film);
        List<Film> actual = filmDbStorage.getAllFilms();

        assertEquals("othername", actual.get(0).getName());
        assertEquals(120, actual.get(0).getDuration());
        assertEquals("other description", actual.get(0).getDescription());
        assertEquals(LocalDate.of(2006, 10, 4), actual.get(0).getReleaseDate());
    }

    @Test
    void getFilmByIdTest() {
        film = filmDbStorage.addNewFilm(film);
        film2 = filmDbStorage.addNewFilm(film2);
        film3 = filmDbStorage.addNewFilm(film3);
        List<Film> actual = filmDbStorage.getAllFilms();

        assertEquals(film.getId(), actual.get(0).getId());
        assertEquals(film2.getId(), actual.get(1).getId());
        assertEquals(film3.getId(), actual.get(2).getId());
    }

    @Test
    void getALlFilmsTest() {
        film = filmDbStorage.addNewFilm(film);
        film2 = filmDbStorage.addNewFilm(film2);
        film3 = filmDbStorage.addNewFilm(film3);
        List<Film> actual = filmDbStorage.getAllFilms();

        assertEquals(3, actual.size());
        assertTrue(actual.contains(film));
        assertTrue(actual.contains(film2));
        assertTrue(actual.contains(film3));
    }

    @Test
    void setFilmGenreTest() {
        Set<Genre> filmGenres = Set.of(new Genre(1, "Комедия"), new Genre(2, "Драма"));
        film.setGenres(filmGenres);
        film = filmDbStorage.addNewFilm(film);

        List<Film> films = new ArrayList<>();
        films.add(film);
        Film gettingFilm = genreDbStorage.loadFilmGenres(films).get(0);
        Set<Genre> gettingGenres = gettingFilm.getGenres();

        assertEquals(gettingGenres.size(), filmGenres.size());
        assertTrue(gettingGenres.containsAll(filmGenres));
        assertTrue(filmGenres.containsAll(gettingGenres));
    }

    @Test
    void loadFilmGenresTest() {
        Set<Genre> filmGenres1 = Set.of(new Genre(1, "Комедия"), new Genre(2, "Драма"));
        film.setGenres(filmGenres1);
        film = filmDbStorage.addNewFilm(film);

        Set<Genre> filmGenres2 = Set.of(new Genre(4, "Триллер"), new Genre(6, "Боевик"));
        film2 = filmDbStorage.addNewFilm(film2);
        film2.setGenres(filmGenres2);


        List<Film> films = List.of(film, film2);
        films = genreDbStorage.loadFilmGenres(films);

        Set<Genre> gottenGenres1 = films.get(0).getGenres();
        assertEquals(gottenGenres1.size(), filmGenres1.size());
        assertTrue(gottenGenres1.containsAll(filmGenres1));
        assertTrue(filmGenres1.containsAll(gottenGenres1));

        Set<Genre> gottenGenres2 = films.get(1).getGenres();
        assertEquals(gottenGenres2.size(), filmGenres2.size());
        assertTrue(gottenGenres2.containsAll(filmGenres2));
        assertTrue(filmGenres2.containsAll(gottenGenres2));
    }

    @Test
    void updateGenresTest() {
        Set<Genre> filmGenres1 = Set.of(new Genre(1, "Комедия"), new Genre(2, "Драма"));
        film.setGenres(filmGenres1);
        film = filmDbStorage.addNewFilm(film);

        Set<Genre> filmGenres2 = Set.of(new Genre(4, "Триллер"), new Genre(6, "Боевик"));
        film.setGenres(filmGenres2);
        filmDbStorage.updateFilmGenre(film);

        List<Film> films = new ArrayList<>();
        films.add(film);
        Film gettingFilm = genreDbStorage.loadFilmGenres(films).get(0);
        Set<Genre> gottenGenres = gettingFilm.getGenres();

        assertEquals(gottenGenres.size(), filmGenres2.size());
        assertTrue(gottenGenres.containsAll(filmGenres2));
        assertTrue(filmGenres2.containsAll(gottenGenres));
    }
}