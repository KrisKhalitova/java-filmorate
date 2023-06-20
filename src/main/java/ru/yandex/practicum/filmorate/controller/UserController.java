package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.entity.User;
import ru.yandex.practicum.filmorate.service.interfaces.FriendshipService;
import ru.yandex.practicum.filmorate.service.interfaces.UserService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final FriendshipService friendshipService;

    @PostMapping
    public User addNewUser(@Valid @RequestBody User user) {
        return userService.addNewUser(user);
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        return userService.updateUser(user);
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable long id) {
        return userService.getUserById(id);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFromFriends(@PathVariable long id, @PathVariable long friendId) {
        friendshipService.deleteFromFriends(id, friendId);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addNewFriend(@PathVariable long id, @PathVariable long friendId) {
        friendshipService.addNewFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getAllFriends(@PathVariable long id) {
        return friendshipService.getAllFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable long id, @PathVariable long otherId) {
        return friendshipService.getCommonFriends(id, otherId);
    }
}