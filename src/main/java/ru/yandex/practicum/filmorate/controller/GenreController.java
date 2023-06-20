package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.entity.Genre;
import ru.yandex.practicum.filmorate.service.interfaces.GenreService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor
public class GenreController {
    private final GenreService genreService;

    @GetMapping("/{genreId}")
    public Genre getFilmGenreById(@PathVariable long genreId) {
        return genreService.getFilmGenreById(genreId);
    }

    @GetMapping
    public List<Genre> getGenres() {
        return genreService.getGenres();
    }
}
