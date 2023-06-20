package ru.yandex.practicum.filmorate.storage.friendship;

import ru.yandex.practicum.filmorate.entity.User;

import java.util.List;

public interface FriendshipStorage {

    void addNewFriend(long userId, long friendId);

    void deleteFromFriends(long userId, long friendId);

    List<User> getAllFriends(long userId);

    List<User> getCommonFriends(long userId, long friendId);
}
