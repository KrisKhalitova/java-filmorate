package ru.yandex.practicum.filmorate.dbStorageTests;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.entity.User;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserStorageTest {
    private final UserDbStorage userDbStorage;
    private final JdbcTemplate jdbcTemplate;
    User user1;
    User user2;

    @BeforeEach
    void clearDb() {
        String sql = "DELETE FROM likes";
        jdbcTemplate.update(sql);
        sql = "DELETE FROM friendship";
        jdbcTemplate.update(sql);
        sql = "DELETE FROM users";
        jdbcTemplate.update(sql);
        user1 = new User("mail@mail.ru", "login", "username",
                LocalDate.of(1995, 11,
                        17));
        user2 = new User("mail25@mail.ru", "login25", "username25",
                LocalDate.of(1985, 1, 7));
    }

    @Test
    void addNewUserTest() {
        user1 = userDbStorage.addNewUser(user1);
        List<User> users = userDbStorage.getAllUsers();

        assertEquals(users.size(), 1);
        assertTrue(users.contains(user1));
    }

    @Test
    void updateUserTest() {
        user1 = userDbStorage.addNewUser(user1);
        user1.setName("othername");
        user1.setEmail("othermail@mail.ru");
        user1.setLogin("otherlogin");
        user1.setBirthday(LocalDate.of(2006, 10, 4));
        user1 = userDbStorage.updateUser(user1);
        List<User> actual = userDbStorage.getAllUsers();

        assertEquals("othername", actual.get(0).getName());
        assertEquals("othermail@mail.ru", actual.get(0).getEmail());
        assertEquals("otherlogin", actual.get(0).getLogin());
        assertEquals(LocalDate.of(2006, 10, 4), actual.get(0).getBirthday());
    }

    @Test
    void getAllUsersTest() {
        user1 = userDbStorage.addNewUser(user1);
        user2 = userDbStorage.addNewUser(user2);
        List<User> users = userDbStorage.getAllUsers();

        assertEquals(users.size(), 2);
        assertTrue(users.contains(user1));
        assertTrue(users.contains(user2));
    }

    @Test
    void getUseryIdTest() {
        user1 = userDbStorage.addNewUser(user1);
        user2 = userDbStorage.addNewUser(user2);
        List<User> actual = userDbStorage.getAllUsers();

        assertEquals(user1.getId(), actual.get(0).getId());
        assertEquals(user2.getId(), actual.get(1).getId());
    }
}
