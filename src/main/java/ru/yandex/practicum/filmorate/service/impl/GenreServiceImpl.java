package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.entity.Genre;
import ru.yandex.practicum.filmorate.service.interfaces.GenreService;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GenreServiceImpl implements GenreService {

    private final GenreStorage genreStorage;

    @Override
    public Genre getFilmGenreById(long genreId) {
        return genreStorage.getFilmGenreById(genreId);
    }

    @Override
    public List<Genre> getGenres() {
        return genreStorage.getGenres();
    }
}
