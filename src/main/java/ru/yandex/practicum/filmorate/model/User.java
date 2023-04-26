package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
public class User {
    private int id;
    @Email (message = "The user's email should contain @")
    @NotBlank (message = "The user's email shouldn't be empty.")
    private String email;
    @NotBlank (message = "The user's login shouldn't contain whitespace.")
    private String login;
    private String name;
    @PastOrPresent (message = "The birthday can't be set in the future.")
    private LocalDate birthday;

    public User(String email, String login, String name, LocalDate birthday) {
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }
}