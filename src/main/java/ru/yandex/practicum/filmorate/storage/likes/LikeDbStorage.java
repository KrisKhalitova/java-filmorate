package ru.yandex.practicum.filmorate.storage.likes;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mappers.FilmMapper;
import ru.yandex.practicum.filmorate.mappers.UserMapper;

@Component
@RequiredArgsConstructor
@Primary
@Slf4j
public class LikeDbStorage implements LikeStorage {

    private final JdbcTemplate jdbcTemplate;
    private final FilmMapper filmMapper;
    private final UserMapper userMapper;
    private final String sqlSearchUser = "SELECT users.* " +
            "FROM users " +
            "WHERE id = ?";
    private final String sqlSearchFilm = "SELECT films.*, rating_mpa.* " +
            "FROM films " +
            "JOIN rating_mpa ON rating_mpa.rating_id = films.rating_id " +
            "WHERE films.id = ?";

    @Override
    public void addLike(long idFilm, long idUser) {
        String sql = "INSERT INTO likes (film_id, user_id) " +
                "VALUES (?,?)";
        int countFilm = jdbcTemplate.query(sqlSearchFilm, filmMapper, idFilm).size();
        int countUser = jdbcTemplate.query(sqlSearchUser, userMapper, idUser).size();
        if (countUser != 0 && countFilm != 0) {
            jdbcTemplate.update(sql, idFilm, idUser);
        } else {
            log.warn("It's not possible to add like to film");
            throw new NotFoundException(HttpStatus.NOT_FOUND, "It's not possible to add like to film");
        }
    }

    @Override
    public void deleteLike(long idFilm, long idUser) {
        String sql = "DELETE FROM likes " +
                "WHERE film_id = ? AND user_id = ?";
        int countFilm = jdbcTemplate.query(sqlSearchFilm, filmMapper, idFilm).size();
        int countUser = jdbcTemplate.query(sqlSearchUser, userMapper, idUser).size();
        if (countUser != 0 && countFilm != 0) {
            jdbcTemplate.update(sql, idFilm, idUser);
        } else {
            log.warn("It's not possible to delete like from film");
            throw new NotFoundException(HttpStatus.NOT_FOUND, "It's not possible to delete like from film");
        }
    }
}