package ru.yandex.practicum.filmorate.storage.genre;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.entity.Film;
import ru.yandex.practicum.filmorate.entity.Genre;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mappers.GenreMapper;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toSet;

@Component
@RequiredArgsConstructor
@Primary
@Slf4j
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;
    private final GenreMapper genreMapper;

    @Override
    public Genre getFilmGenreById(long genreId) {
        String sql = "SELECT * " +
                "FROM genres " +
                "WHERE genres_id = ?";
        List<Genre> genres = jdbcTemplate.query(sql, genreMapper, genreId);
        if (genres.size() != 1) {
            log.warn("Genre wasn't found");
            throw new NotFoundException(HttpStatus.NOT_FOUND, "Genre wasn't found");
        }
        return genres.get(0);
    }

    @Override
    public List<Genre> getGenres() {
        String sql = "SELECT * " +
                "FROM genres " +
                "ORDER BY genres_id";
        return jdbcTemplate.query(sql, genreMapper);
    }

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
}