package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public UserService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public void addNewFriend(long userId, long friendId) {
        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);
        user.addNewFriend(friendId);
        friend.addNewFriend(userId);
    }

    public void deleteFromFriends(long userId, long friendId) {
        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);
        user.deleteFriend(friendId);
        friend.deleteFriend(userId);
    }

    public User getUserById(long userId) {
        return userStorage.getUserById(userId);
    }

    public List<User> getAllFriends(long userId) {
        User user = userStorage.getUserById(userId);
        return user.getFriends().stream()
                .map(userStorage::getUserById)
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(long userId, long friendId) {
        return getUserById(userId).getFriends()
                .stream()
                .filter(getUserById(friendId).getFriends()::contains)
                .map(this::getUserById)
                .collect(Collectors.toList());
    }

    public User addNewUser(User user) {
        return userStorage.addNewUser(user);
    }

    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }
}
