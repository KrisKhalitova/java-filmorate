package ru.yandex.practicum.filmorate.service.interfaces;

import ru.yandex.practicum.filmorate.entity.Mpa;

import java.util.List;

public interface MpaService {
    Mpa getMpaRatingById(long ratingId);

    List<Mpa> getMpaRating();
}
