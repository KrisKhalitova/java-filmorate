package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
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
import ru.yandex.practicum.filmorate.mappers.GenreMapper;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toSet;

@Component
@RequiredArgsConstructor
@Primary
@Slf4j
public class FilmDbStorage implements FilmStorage {

    private static final LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, 12, 28);
    private final JdbcTemplate jdbcTemplate;
    private final FilmMapper filmMapper;
    private final GenreMapper genreMapper;

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
        return jdbcTemplate.query(sql, filmMapper);
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
        try {
            return jdbcTemplate.queryForObject(sql, filmMapper, idFilm);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException(HttpStatus.NOT_FOUND, "Film wasn't found");
        }
    }

    @Override
    public Film loadFilmGenre(Film film) {
        String sql = "SELECT film_genre.genre_id, genres.* " +
                "FROM film_genre " +
                "JOIN genres ON genres.genres_id = film_genre.genre_id " +
                "WHERE film_id IN  ( ? );";
        List<Genre> genres = jdbcTemplate.query(sql, genreMapper, film.getId());
        setFilmGenre(film);
        film.setGenres(new HashSet<>(genres));
        return film;
    }

    @Override
    public List<Film> loadFilmGenres(List<Film> films) {
        if (films.isEmpty()) {
            return Collections.emptyList();
        }
        String idsStr = films.stream()
                .map(Film::getId)
                .map(String::valueOf)
                .collect(Collectors.joining(","));
        String sql = "SELECT film_genre.*, genres.* " +
                "FROM film_genre " +
                "JOIN genres ON genres.genres_id = film_genre.genre_id " +
                "WHERE film_id IN  (" + idsStr + ");";

        List<Map<String, Object>> mapList = jdbcTemplate.queryForList(sql);
        Map<Long, Set<Genre>> genresByFilmId = mapList.stream()
                .collect(Collectors.groupingBy(k -> (Long) k.get("film_id"),
                        mapping(k -> new Genre((Long) k.get("genre_id"), (String) k.get("name")), toSet())));

        for (Film film : films) {
            Set<Genre> filmGenres = genresByFilmId.get(film.getId());
            if (filmGenres != null) {
                film.setGenres(filmGenres);
            }
        }
        return films;
    }

    @Override
    public void deleteFilmGenre(long id) {
        String sql = "DELETE FROM film_genre " +
                "WHERE film_id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public void updateFilmGenre(Film film) {
        deleteFilmGenre(film.getId());
        setFilmGenre(film);
    }

    @Override
    public void setFilmGenre(Film film) {
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

    @Override
    public void deleteFilm(long id) {
        String sql = "DELETE FROM film_genre " +
                "WHERE film_id = ?";
        jdbcTemplate.update(sql, id);
    }

    private static void validateFilm(Film film) {
        if (film.getReleaseDate().isBefore(MIN_RELEASE_DATE)) {
            log.warn("The release date of the movie should be after " + MIN_RELEASE_DATE + ".");
            throw new ValidationException("The release date of the movie should be after " + MIN_RELEASE_DATE + ".");
        }
    }
}