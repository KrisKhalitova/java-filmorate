package ru.yandex.practicum.filmorate.dbStorageTests;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.entity.Mpa;
import ru.yandex.practicum.filmorate.storage.mpaRating.MpaDbStorage;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MpaStorageTest {

    private final MpaDbStorage mpaDbStorage;

    @Test
    void getMpaTest() {
        Mpa mpa = mpaDbStorage.getMpaRatingById(1);

        assertEquals(mpa.getName(), "G");
    }

    @Test
    void getAllMpaTest() {
        List<Mpa> allGenres = mpaDbStorage.getMpaRating();

        assertEquals(allGenres.size(), 5);
    }
}
