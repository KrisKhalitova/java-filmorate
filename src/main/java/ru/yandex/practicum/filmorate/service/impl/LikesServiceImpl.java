package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.entity.Film;
import ru.yandex.practicum.filmorate.service.interfaces.LikesService;
import ru.yandex.practicum.filmorate.storage.likes.LikesStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LikesServiceImpl implements LikesService {

    private final LikesStorage likesStorage;

    @Override
    public void addLike(long idFilm, long idUser) {
        likesStorage.addLike(idFilm, idUser);
    }

    @Override
    public void deleteLike(long idFilm, long idUser) {
        likesStorage.deleteLike(idFilm, idUser);
    }

    @Override
    public List<Film> getTheMostPopularFilms(long count) {
        return likesStorage.getTheMostPopularFilms(count);
    }
}
