package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.entity.User;
import ru.yandex.practicum.filmorate.service.interfaces.FriendshipService;
import ru.yandex.practicum.filmorate.storage.friendship.FriendshipStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FriendshipServiceImpl implements FriendshipService {

    private final FriendshipStorage friendshipStorage;
    private final UserStorage userStorage;

    @Override
    public void addNewFriend(long userId, long friendId) {
        userStorage.getUserById(userId);
        userStorage.getUserById(friendId);
        friendshipStorage.addNewFriend(userId, friendId);
    }

    @Override
    public void deleteFromFriends(long userId, long friendId) {
        userStorage.getUserById(userId);
        userStorage.getUserById(friendId);
        friendshipStorage.deleteFromFriends(userId, friendId);
    }

    @Override
    public List<User> getAllFriends(long userId) {
        userStorage.getUserById(userId);
        return friendshipStorage.getAllFriends(userId);
    }

    @Override
    public List<User> getCommonFriends(long userId, long friendId) {
        userStorage.getUserById(userId);
        userStorage.getUserById(friendId);
        return friendshipStorage.getCommonFriends(userId, friendId);
    }
}
