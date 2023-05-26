package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.entity.Film;
import ru.yandex.practicum.filmorate.service.interfaces.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.likes.LikesStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {

    private final FilmStorage filmStorage;
    private final LikesStorage likesStorage;

    @Override
    public Film getFilmById(long idFilm) {
        return filmStorage.loadFilmGenre(filmStorage.getFilmById(idFilm));
    }

    @Override
    public List<Film> getTheMostPopularFilms(long count) {
        return likesStorage.getTheMostPopularFilms(count);
    }

    @Override
    public Film addNewFilm(Film film) {
        return filmStorage.addNewFilm(film);
    }

    @Override
    public List<Film> getAllFilms() {
        return filmStorage.loadFilmGenres(filmStorage.getAllFilms());
    }

    @Override
    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }
}
