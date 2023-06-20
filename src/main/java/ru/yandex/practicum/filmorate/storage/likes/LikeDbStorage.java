package ru.yandex.practicum.filmorate.storage.likes;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Primary
@Slf4j
public class LikeDbStorage implements LikeStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addLike(long idFilm, long idUser) {
        String sql = "INSERT INTO likes (film_id, user_id) " +
                "VALUES (?,?)";
        jdbcTemplate.update(sql, idFilm, idUser);
    }

    @Override
    public void deleteLike(long idFilm, long idUser) {
        String sql = "DELETE FROM likes " +
                "WHERE film_id = ? AND user_id = ?";
        jdbcTemplate.update(sql, idFilm, idUser);
    }
}