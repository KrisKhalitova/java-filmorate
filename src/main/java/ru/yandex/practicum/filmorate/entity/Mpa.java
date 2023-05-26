package ru.yandex.practicum.filmorate.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class Mpa {
    private long id;
    @NotBlank(message = "The film's rating name shouldn't be empty")
    private String name;
}
