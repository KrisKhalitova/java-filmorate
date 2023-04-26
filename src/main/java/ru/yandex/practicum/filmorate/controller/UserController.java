package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private int userId = 1;

    @PostMapping
    public User addNewUser(@Valid @RequestBody User user) {
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

    @GetMapping
    public List<User> getAllUsers() {
        log.info("Request of getting all users is received.");
        return new ArrayList<>(users.values());
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        if (users.get(user.getId()) == null) {
            log.warn("It's not possible to update non-existent user");
            throw new NotFoundException("It's not possible to update non-existent user");
        }
        validateUser(user);
        users.put(user.getId(), user);
        log.info("The user {} is updated.", user);
        return users.get(user.getId());
    }

    public void validateUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (user.getLogin().contains(" ") || user.getLogin().isBlank() || user.getLogin() == null) {
            log.warn("The user's login shouldn't contain whitespace.");
            throw new ValidationException("The user's login shouldn't contain whitespace.");
        }
    }
}
