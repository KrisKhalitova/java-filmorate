package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.service.interfaces.LikeService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.likes.LikeStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {

    private final LikeStorage likesStorage;
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Override
    public void addLike(long idFilm, long idUser) {
        filmStorage.getFilmById(idFilm);
        userStorage.getUserById(idUser);
        likesStorage.addLike(idFilm, idUser);
    }

    @Override
    public void deleteLike(long idFilm, long idUser) {
        filmStorage.getFilmById(idFilm);
        userStorage.getUserById(idUser);
        likesStorage.deleteLike(idFilm, idUser);
    }
}
