package ru.yandex.practicum.filmorate.service.interfaces;

import ru.yandex.practicum.filmorate.entity.User;

import java.util.List;


public interface UserService {

    User getUserById(long userId);

    User addNewUser(User user);

    List<User> getAllUsers();

    User updateUser(User user);
}
