package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.service.interfaces.LikeService;
import ru.yandex.practicum.filmorate.storage.likes.LikeStorage;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {

    private final LikeStorage likesStorage;

    @Override
    public void addLike(long idFilm, long idUser) {
        likesStorage.addLike(idFilm, idUser);
    }

    @Override
    public void deleteLike(long idFilm, long idUser) {
        likesStorage.deleteLike(idFilm, idUser);
    }
}
