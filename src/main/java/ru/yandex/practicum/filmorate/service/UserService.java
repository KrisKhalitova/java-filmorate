package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserStorage userStorage;

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User addNewFriend(long userId, long friendId) {
        if (userStorage.getUserById(userId) == null) {
            throw new NotFoundException("The user doesn't exist");
        } else if (userStorage.getUserById(friendId) == null) {
            throw new NotFoundException("The user(friend) doesn't exist.");
        }
        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);
        user.addNewFriend(friendId);
        friend.addNewFriend(userId);
        return user;
    }

    public User deleteFromFriends(long userId, long friendId) {
        if (userStorage.getUserById(userId) == null) {
            throw new NotFoundException("The user doesn't exist");
        } else if (userStorage.getUserById(friendId) == null) {
            throw new NotFoundException("The user(friend) doesn't exist.");
        }
        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);
        user.deleteFriend(friendId);
        friend.deleteFriend(userId);
        return user;
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
