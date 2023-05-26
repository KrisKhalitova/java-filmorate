package ru.yandex.practicum.filmorate.service.interfaces;

import ru.yandex.practicum.filmorate.entity.Genre;

import java.util.List;

public interface GenreService {

    Genre getFilmGenreById(long genreId);

    List<Genre> getGenres();
}
