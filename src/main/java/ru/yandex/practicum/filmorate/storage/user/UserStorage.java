package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.entity.User;

import java.util.List;

public interface UserStorage {

    User addNewUser(User user);

    List<User> getAllUsers();

    User updateUser(User user);

    User getUserById(long userId);
}
