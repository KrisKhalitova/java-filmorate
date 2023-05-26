package ru.yandex.practicum.filmorate.storage.likes;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.entity.Film;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mappers.FilmMapper;

import java.util.List;

@Component
@RequiredArgsConstructor
@Primary
public class LikesDbStorage implements LikesStorage {

    private final JdbcTemplate jdbcTemplate;
    private final FilmMapper filmMapper;

    @Override
    public void addLike(long idFilm, long idUser) {
        String sql = "INSERT INTO likes (film_id, user_id) " +
                "VALUES (?,?)";
        if (idFilm <= 0 || idUser <= 0) {
            throw new NotFoundException(HttpStatus.NOT_FOUND, "Id can't be negative or zero");
        }
        jdbcTemplate.update(sql, idFilm, idUser);
    }

    @Override
    public void deleteLike(long idFilm, long idUser) {
        String sql = "DELETE FROM likes " +
                "WHERE film_id = ? AND user_id = ?";
        if (idFilm <= 0 || idUser <= 0) {
            throw new NotFoundException(HttpStatus.NOT_FOUND, "Id can't be negative or zero");
        }
        jdbcTemplate.update(sql, idFilm, idUser);
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
        return jdbcTemplate.query(sql, filmMapper, count);
    }
}
