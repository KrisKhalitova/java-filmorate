package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private long userId = 1;

    @Override
    public User addNewUser(User user) {
        if (users.containsValue(user)) {
            log.warn("This user has been already created.");
            throw new ValidationException("This user has been already created.");
        }
        validateUser(user);
        user.setId(userId++);
        users.put(user.getId(), user);
        log.info("A new user {} is added.", user);
        return users.get(user.getId());
    }

    @Override
    public List<User> getAllUsers() {
        log.info("Request of getting all users is received.");
        return new ArrayList<>(users.values());
    }

    @Override
    public User updateUser(User user) {
        if (users.get(user.getId()) == null) {
            log.warn("It's not possible to update non-existent user");
            throw new NotFoundException("It's not possible to update non-existent user");
        }
        validateUser(user);
        users.put(user.getId(), user);
        log.info("The user {} is updated.", user);
        return users.get(user.getId());
    }

    @Override
    public User getUserById(long userId) {
        if (!users.containsKey(userId)) {
            log.warn("The user with userId {} doesn't exist", userId);
            throw new NotFoundException("The user doesn't exist");
        } else {
            return users.get(userId);
        }
    }

    public static void validateUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (user.getLogin().contains(" ") || user.getLogin().isBlank() || user.getLogin() == null) {
            log.warn("The user's login shouldn't contain whitespace.");
            throw new ValidationException("The user's login shouldn't contain whitespace.");
        }
    }
}
