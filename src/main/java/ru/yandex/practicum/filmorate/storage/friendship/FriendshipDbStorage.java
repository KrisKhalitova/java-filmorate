package ru.yandex.practicum.filmorate.storage.friendship;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.entity.User;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mappers.UserMapper;

import java.util.List;

@Component
@RequiredArgsConstructor
@Primary
public class FriendshipDbStorage implements FriendshipStorage {

    private final JdbcTemplate jdbcTemplate;
    private final UserMapper userMapper;

    @Override
    public void addNewFriend(long userId, long friendId) {
        String sql = "INSERT INTO friendship (user_id, friend_id) " +
                "VALUES (?, ?)";
        if (userId <= 0 || friendId <= 0) {
            throw new NotFoundException(HttpStatus.NOT_FOUND, "Id can't be negative or zero");
        }
        jdbcTemplate.update(sql, userId, friendId);
    }

    @Override
    public void deleteFromFriends(long userId, long friendId) {
        String sql = "DELETE FROM friendship " +
                "WHERE user_id = ? AND friend_id = ?";
        jdbcTemplate.update(sql, userId, friendId);
    }

    @Override
    public List<User> getAllFriends(long userId) {
        String sql = "SELECT * " +
                "FROM users " +
                "WHERE id IN " +
                "(SELECT friend_id " +
                "FROM friendship " +
                "WHERE user_id = ?)";
        return jdbcTemplate.query(sql, userMapper, userId);
    }

    @Override
    public List<User> getCommonFriends(long userId, long friendId) {
        String sql = "SELECT users.* " +
                "FROM users " +
                "JOIN FRIENDSHIP AS f1 on(users.id = f1.friend_id AND f1.user_id = ?) " +
                "JOIN FRIENDSHIP AS f2 on (users.id = f2.friend_id AND f2.user_id =?)";
        return jdbcTemplate.query(sql, userMapper, userId, friendId);
    }
}
