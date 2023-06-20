package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.entity.Genre;

import java.util.List;

public interface GenreStorage {
    Genre getFilmGenreById(long genreId);

    List<Genre> getGenres();
}
