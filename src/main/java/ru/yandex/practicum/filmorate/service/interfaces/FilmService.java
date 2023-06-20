package ru.yandex.practicum.filmorate.service.interfaces;

import ru.yandex.practicum.filmorate.entity.Film;

import java.util.List;

public interface FilmService {

    Film getFilmById(long idFilm);

    List<Film> getTheMostPopularFilms(long count);

    Film addNewFilm(Film film);

    List<Film> getAllFilms();

    Film updateFilm(Film film);
}
