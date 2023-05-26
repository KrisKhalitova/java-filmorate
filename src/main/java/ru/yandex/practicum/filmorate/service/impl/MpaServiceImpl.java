package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.entity.Mpa;
import ru.yandex.practicum.filmorate.service.interfaces.MpaService;
import ru.yandex.practicum.filmorate.storage.mpaRating.MpaStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MpaServiceImpl implements MpaService {

    private final MpaStorage mpaRatingStorage;

    @Override
    public Mpa getMpaRatingById(long ratingId) {
        return mpaRatingStorage.getMpaRatingById(ratingId);
    }

    @Override
    public List<Mpa> getMpaRating() {
        return mpaRatingStorage.getMpaRating();
    }
}
