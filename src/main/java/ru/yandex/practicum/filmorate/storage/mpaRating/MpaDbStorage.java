package ru.yandex.practicum.filmorate.storage.mpaRating;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.entity.Mpa;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mappers.MpaMapper;

import java.util.List;

@Component
@RequiredArgsConstructor
@Primary
public class MpaDbStorage implements MpaStorage {

    private final JdbcTemplate jdbcTemplate;
    private final MpaMapper mapMpaRating;

    @Override
    public Mpa getMpaRatingById(long ratingId) {
        String sql = "SELECT * " +
                "FROM rating_mpa " +
                "WHERE rating_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, mapMpaRating, ratingId);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException(HttpStatus.NOT_FOUND, "Rating wasn't found");
        }
    }

    @Override
    public List<Mpa> getMpaRating() {
        String sql = "SELECT * " +
                "FROM rating_mpa";
        return jdbcTemplate.query(sql, mapMpaRating);
    }
}
