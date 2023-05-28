package ru.yandex.practicum.filmorate.storage.mpaRating;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
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
@Slf4j
public class MpaDbStorage implements MpaStorage {

    private final JdbcTemplate jdbcTemplate;
    private final MpaMapper mapMpaRating;

    @Override
    public Mpa getMpaRatingById(long ratingId) {
        String sql = "SELECT * " +
                "FROM rating_mpa " +
                "WHERE rating_id = ?";
        int count = jdbcTemplate.query(sql, mapMpaRating, ratingId).size();
        if (count != 1) {
            log.warn("Rating wasn't found");
            throw new NotFoundException(HttpStatus.NOT_FOUND, "Rating wasn't found");
        } else {
            return jdbcTemplate.queryForObject(sql, mapMpaRating, ratingId);
        }
    }

    @Override
    public List<Mpa> getMpaRating() {
        String sql = "SELECT * " +
                "FROM rating_mpa";
        return jdbcTemplate.query(sql, mapMpaRating);
    }
}
