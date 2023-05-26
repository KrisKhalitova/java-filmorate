package ru.yandex.practicum.filmorate.storage.likes;

import ru.yandex.practicum.filmorate.entity.Film;

import java.util.List;

public interface LikesStorage {

    void addLike(long idFilm, long idUser);

    void deleteLike(long idFilm, long idUser);

    List<Film> getTheMostPopularFilms(long count);
}
