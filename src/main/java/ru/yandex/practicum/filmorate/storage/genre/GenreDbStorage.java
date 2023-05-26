package ru.yandex.practicum.filmorate.storage.genre;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.entity.Genre;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mappers.GenreMapper;

import java.util.List;

@Component
@RequiredArgsConstructor
@Primary
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;
    private final GenreMapper genreMapping;

    @Override
    public Genre getFilmGenreById(long genreId) {
        String sql = "SELECT * " +
                "FROM genres " +
                "WHERE genres_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, genreMapping, genreId);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException(HttpStatus.NOT_FOUND, "Genre wasn't found");
        }
    }

    @Override
    public List<Genre> getGenres() {
        String sql = "SELECT * " +
                "FROM genres " +
                "ORDER BY genres_id";
        return jdbcTemplate.query(sql, genreMapping);
    }
}
