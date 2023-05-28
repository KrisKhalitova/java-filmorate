package ru.yandex.practicum.filmorate.storage.friendship;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class FriendshipDbStorage implements FriendshipStorage {

    private final JdbcTemplate jdbcTemplate;
    private final UserMapper userMapper;
    private final String sqlSearch = "SELECT users.* " +
            "FROM users " +
            "WHERE id = ?";

    @Override
    public void addNewFriend(long userId, long friendId) {
        String sql = "INSERT INTO friendship (user_id, friend_id) " +
                "VALUES (?, ?)";
        int countUser = jdbcTemplate.query(sqlSearch, userMapper, userId).size();
        int countFriend = jdbcTemplate.query(sqlSearch, userMapper, friendId).size();
        if (countUser != 1 && countFriend != 1) {
            log.warn("It's not possible to add a new friend.");
            throw new NotFoundException(HttpStatus.NOT_FOUND, "It's not possible to add a new friend.");
        } else {
            jdbcTemplate.update(sql, userId, friendId);
        }
    }

    @Override
    public void deleteFromFriends(long userId, long friendId) {
        String sql = "DELETE FROM friendship " +
                "WHERE user_id = ? AND friend_id = ?";
        int countUser = jdbcTemplate.query(sqlSearch, userMapper, userId).size();
        int countFriend = jdbcTemplate.query(sqlSearch, userMapper, friendId).size();
        if (countUser > 0 && countFriend > 0) {
            jdbcTemplate.update(sql, userId, friendId);
        } else {
            log.warn("It's not possible to delete a friend.");
            throw new NotFoundException(HttpStatus.NOT_FOUND, "It's not possible to delete a friend.");
        }
    }

    @Override
    public List<User> getAllFriends(long userId) {
        String sql = "SELECT * " +
                "FROM users " +
                "WHERE id IN " +
                "(SELECT friend_id " +
                "FROM friendship " +
                "WHERE user_id = ?)";
        int countUser = jdbcTemplate.query(sqlSearch, userMapper, userId).size();
        if (countUser == 0) {
            log.warn("It's not possible to get all user's friends.");
            throw new NotFoundException(HttpStatus.NOT_FOUND, "It's not possible to get all user's friends.");
        } else {
            return jdbcTemplate.query(sql, userMapper, userId);
        }
    }

    @Override
    public List<User> getCommonFriends(long userId, long friendId) {
        String sql = "SELECT users.* " +
                "FROM users " +
                "JOIN FRIENDSHIP AS f1 on(users.id = f1.friend_id AND f1.user_id = ?) " +
                "JOIN FRIENDSHIP AS f2 on (users.id = f2.friend_id AND f2.user_id =?)";
        int countUser = jdbcTemplate.query(sqlSearch, userMapper, userId).size();
        int countFriend = jdbcTemplate.query(sqlSearch, userMapper, friendId).size();
        if (countUser == 0 && countFriend == 0) {
            log.warn("It's not possible to get all common friends.");
            throw new NotFoundException(HttpStatus.NOT_FOUND, "It's not possible to get all common friends.");
        } else {
            return jdbcTemplate.query(sql, userMapper, userId, friendId);
        }
    }
}
