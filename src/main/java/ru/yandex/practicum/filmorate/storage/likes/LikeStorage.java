package ru.yandex.practicum.filmorate.storage.likes;

public interface LikeStorage {

    void addLike(long idFilm, long idUser);

    void deleteLike(long idFilm, long idUser);
}
