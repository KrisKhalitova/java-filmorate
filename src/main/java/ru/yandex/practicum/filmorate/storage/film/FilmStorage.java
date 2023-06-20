package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.entity.Film;

import java.util.List;

public interface FilmStorage {

    Film addNewFilm(Film film);

    List<Film> getAllFilms();

    Film updateFilm(Film film);

    Film getFilmById(long idFilm);

    List<Film> getTheMostPopularFilms(long count);
}