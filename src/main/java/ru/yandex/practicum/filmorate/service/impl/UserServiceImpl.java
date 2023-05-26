package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.entity.User;
import ru.yandex.practicum.filmorate.service.interfaces.UserService;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserStorage userStorage;

    @Override
    public User getUserById(long userId) {
        return userStorage.getUserById(userId);
    }

    @Override
    public User addNewUser(User user) {
        return userStorage.addNewUser(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    @Override
    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }


}
