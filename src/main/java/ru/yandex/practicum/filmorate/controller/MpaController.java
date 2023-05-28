package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.entity.Mpa;
import ru.yandex.practicum.filmorate.service.interfaces.MpaService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/mpa")
@RequiredArgsConstructor
public class MpaController {
    private final MpaService mpaRatingService;

    @GetMapping("/{ratingId}")
    public Mpa getMpaRatingById(@Valid @PathVariable long ratingId) {
        return mpaRatingService.getMpaRatingById(ratingId);
    }

    @GetMapping
    public List<Mpa> getMpaRating() {
        return mpaRatingService.getMpaRating();
    }
}
