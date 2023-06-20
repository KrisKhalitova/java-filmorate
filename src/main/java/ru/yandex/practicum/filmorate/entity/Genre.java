package ru.yandex.practicum.filmorate.entity;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Genre {
    private long id;
    @NotBlank(message = "The film's genre shouldn't be empty.")
    private String name;
}
