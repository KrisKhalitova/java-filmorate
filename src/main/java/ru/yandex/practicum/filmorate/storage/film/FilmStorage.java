package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.entity.Film;

import java.util.List;

public interface FilmStorage {

    Film addNewFilm(Film film);

    List<Film> getAllFilms();

    Film updateFilm(Film film);

    Film getFilmById(long idFilm);

    Film loadFilmGenre(Film film);

    List<Film> loadFilmGenres(List<Film> films);

    void deleteFilmGenre(long id);

    void updateFilmGenre(Film film);

    void setFilmGenre(Film film);

    void deleteFilm(long id);
}