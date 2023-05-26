package ru.yandex.practicum.filmorate.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class Likes {
    private long userId;
    private long filmId;
}
