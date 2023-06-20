package ru.yandex.practicum.filmorate.service.interfaces;

public interface LikeService {
    void addLike(long idFilm, long idUser);

    void deleteLike(long idFilm, long idUser);
}
