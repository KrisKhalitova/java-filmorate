package ru.yandex.practicum.filmorate.storage.mpaRating;

import ru.yandex.practicum.filmorate.entity.Mpa;

import java.util.List;

public interface MpaStorage {
    Mpa getMpaRatingById(long ratingId);

    List<Mpa> getMpaRating();
}
