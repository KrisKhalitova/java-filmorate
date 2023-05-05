package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public void addLike(long idFilm, long idUser) {
        if (filmStorage.getFilmById(idFilm) == null) {
            throw new NotFoundException("The film doesn't exist");
        } else if (userStorage.getUserById(idUser) == null) {
            throw new NotFoundException("The user doesn't exist.");
        }
        Film film = filmStorage.getFilmById(idFilm);
        User user = userStorage.getUserById(idUser);
        film.addLike(user.getId());
    }

    public void deleteLike(long idFilm, long idUser) {
        if (filmStorage.getFilmById(idFilm) == null) {
            throw new NotFoundException("The film doesn't exist");
        } else if (userStorage.getUserById(idUser) == null) {
            throw new NotFoundException("The user doesn't exist.");
        }
        Film film = filmStorage.getFilmById(idFilm);
        User user = userStorage.getUserById(idUser);
        film.removeLike(user.getId());
    }

    public Film getFilmById(long idFilm) {
        return filmStorage.getFilmById(idFilm);
    }

    public List<Film> getTenTopFilms(long count) {
        Comparator<Film> comparator = Comparator.comparingInt(f -> f.getLikes().size());
        return filmStorage.getAllFilms().stream().sorted(comparator.reversed()).limit(count)
                .collect(Collectors.toList());
    }

    public Film addNewFilm(Film film) {
        return filmStorage.addNewFilm(film);
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }
}
