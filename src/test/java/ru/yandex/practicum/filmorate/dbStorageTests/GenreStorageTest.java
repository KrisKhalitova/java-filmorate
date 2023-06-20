package ru.yandex.practicum.filmorate.dbStorageTests;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.entity.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GenreStorageTest {

    private final GenreDbStorage genreDbStorage;

    @Test
    void getFilmGenreByIdTest() {
        Genre genre = genreDbStorage.getFilmGenreById(1);
        assertEquals(genre.getName(), "Комедия");
    }

    @Test
    void getAllTest() {
        List<Genre> allGenres = genreDbStorage.getGenres();
        assertEquals(allGenres.size(), 6);
    }
}
