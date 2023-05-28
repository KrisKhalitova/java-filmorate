package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.entity.User;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mappers.UserMapper;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Primary
@Slf4j
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;
    private final UserMapper userMapper;

    @Override
    public User addNewUser(User user) {
        validateUser(user);
        Map<String, Object> keys = new SimpleJdbcInsert(this.jdbcTemplate)
                .withTableName("USERS")
                .usingColumns("NAME", "EMAIL", "LOGIN", "BIRTHDAY")
                .usingGeneratedKeyColumns("ID")
                .executeAndReturnKeyHolder(Map.of("NAME", user.getName(),
                        "EMAIL", user.getEmail(),
                        "LOGIN", user.getLogin(),
                        "BIRTHDAY", user.getBirthday()))
                .getKeys();
        user.setId((Long) keys.get("ID"));
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        String sql = "SELECT * " +
                "FROM users";
        return jdbcTemplate.query(sql, userMapper);
    }

    @Override
    public User updateUser(User user) {
        validateUser(user);
        String sql = "UPDATE users SET name = ?, email = ?, login = ?, birthday = ? " +
                "WHERE id = ? ";
        int count = jdbcTemplate.update(sql,
                user.getName(),
                user.getEmail(),
                user.getLogin(),
                user.getBirthday(),
                user.getId());
        if (count != 1) {
            log.warn("It's not possible to update non-existent user");
            throw new NotFoundException(HttpStatus.NOT_FOUND, "It's not possible to update non-existent user");
        }
        return user;
    }

    @Override
    public User getUserById(long userId) {
        String sql = "SELECT * " +
                "FROM users " +
                "WHERE id = ?";
        int count = jdbcTemplate.query(sql, userMapper, userId).size();
        if (count != 1) {
            log.warn("User wasn't found");
            throw new NotFoundException(HttpStatus.NOT_FOUND, "User wasn't found");
        } else {
            return jdbcTemplate.queryForObject(sql, userMapper, userId);
        }
    }

    private void validateUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (user.getLogin().contains(" ") || user.getLogin().isBlank() || user.getLogin() == null) {
            log.warn("The user's login shouldn't contain whitespace.");
            throw new ValidationException("The user's login shouldn't contain whitespace.");
        }
    }
}
