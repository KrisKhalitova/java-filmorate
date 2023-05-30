package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.entity.Film;
import ru.yandex.practicum.filmorate.entity.Genre;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mappers.FilmMapper;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Primary
@Slf4j
public class FilmDbStorage implements FilmStorage {

    private static final LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, 12, 28);
    private final JdbcTemplate jdbcTemplate;
    private final FilmMapper filmMapper;
    private final GenreDbStorage genreDbStorage;

    @Override
    public Film addNewFilm(Film film) {
        validateFilm(film);
        Map<String, Object> keys = new SimpleJdbcInsert(this.jdbcTemplate)
                .withTableName("FILMS")
                .usingColumns("NAME", "DESCRIPTION", "RELEASE_DATE", "DURATION", "RATING_ID")
                .usingGeneratedKeyColumns("ID")
                .executeAndReturnKeyHolder(Map.of("NAME", film.getName(),
                        "DESCRIPTION", film.getDescription(),
                        "RELEASE_DATE", java.sql.Date.valueOf(film.getReleaseDate()),
                        "DURATION", film.getDuration(),
                        "RATING_ID", film.getMpa().getId()))
                .getKeys();
        film.setId((Long) keys.get("ID"));
        setFilmGenre(film);
        return film;
    }

    @Override
    public List<Film> getAllFilms() {
        String sql = "SELECT films.*, rating_mpa.* " +
                "FROM films " +
                "JOIN rating_mpa ON rating_mpa.rating_id = films.rating_id;";
        return new ArrayList<>(genreDbStorage.loadFilmGenres(jdbcTemplate.query(sql, filmMapper)));
    }

    @Override
    public Film updateFilm(Film film) {
        validateFilm(film);
        String sql = "UPDATE films SET name = ?, description = ?, release_date = ?, duration = ?, rating_id = ? " +
                "WHERE id = ? ";
        int count = jdbcTemplate.update(sql,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());
        if (count != 1) {
            log.warn("It's not possible to update non-existent film");
            throw new NotFoundException(HttpStatus.NOT_FOUND, "It's not possible to update non-existent film");
        }
        updateFilmGenre(film);
        return film;
    }

    @Override
    public Film getFilmById(long idFilm) {
        String sql = "SELECT films.*, rating_mpa.* " +
                "FROM films " +
                "JOIN rating_mpa ON rating_mpa.rating_id = films.rating_id " +
                "WHERE films.id = ?";
        List<Film> films = jdbcTemplate.query(sql, filmMapper, idFilm);
        if (films.size() != 1) {
            log.warn("Film wasn't found");
            throw new NotFoundException(HttpStatus.NOT_FOUND, "Film wasn't found");
        }
        List<Film> genres = genreDbStorage.loadFilmGenres(films);
        setFilmGenre(genres.get(0));
        return films.get(0);
    }

    @Override
    public List<Film> getTheMostPopularFilms(long count) {
        String sql = "SELECT films.* , rating_mpa.name AS rating_name " +
                "FROM films " +
                "LEFT JOIN likes ON films.id = likes.film_id " +
                "JOIN rating_mpa ON rating_mpa.rating_id = films.rating_id " +
                "GROUP BY films.id " +
                "ORDER BY COUNT(likes.user_id) DESC " +
                "LIMIT ?";
        return new ArrayList<>(genreDbStorage.loadFilmGenres(jdbcTemplate.query(sql, filmMapper, count)));
    }

    private static void validateFilm(Film film) {
        if (film.getReleaseDate().isBefore(MIN_RELEASE_DATE)) {
            log.warn("The release date of the movie should be after " + MIN_RELEASE_DATE + ".");
            throw new ValidationException("The release date of the movie should be after " + MIN_RELEASE_DATE + ".");
        }
    }

    public void deleteFilmGenre(long id) {
        String sql = "DELETE FROM film_genre " +
                "WHERE film_id = ?";
        jdbcTemplate.update(sql, id);
    }

    public void updateFilmGenre(Film film) {
        deleteFilmGenre(film.getId());
        setFilmGenre(film);
    }

    private void setFilmGenre(Film film) {
        if (film.getGenres() == null || film.getGenres().isEmpty()) {
            return;
        }
        String sql = "INSERT INTO film_genre (film_id, genre_id) " +
                "VALUES(?,?)";
        List<Genre> genres = new ArrayList<>(film.getGenres());
        jdbcTemplate.batchUpdate(sql,
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setLong(1, film.getId());
                        ps.setLong(2, genres.get(i).getId());
                    }

                    @Override
                    public int getBatchSize() {
                        return film.getGenres().size();
                    }
                });
    }
}