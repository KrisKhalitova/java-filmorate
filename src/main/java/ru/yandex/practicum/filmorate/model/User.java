package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString
public class User {
    private long id;
    @Email(message = "The user's email should contain @")
    @NotBlank(message = "The user's email shouldn't be empty.")
    private String email;
    @NotBlank(message = "The user's login shouldn't contain whitespace.")
    private String login;
    private String name;
    @PastOrPresent(message = "The birthday can't be set in the future.")
    private LocalDate birthday;
    private Set<Long> friends;

    public User(String email, String login, String name, LocalDate birthday) {
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
        this.friends = new HashSet<>();
    }

    public void addNewFriend(long friendId) {
        friends.add(friendId);
    }

    public void deleteFriend(long friendId) {
        friends.remove(friendId);
    }
}