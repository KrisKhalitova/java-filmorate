package ru.yandex.practicum.filmorate.controllerTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    private User user;
    private User user2;
    private UserService userService;
    private ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private Validator validator = factory.getValidator();


    @BeforeEach
    public void beforeEach() {
        userService = new UserService(new InMemoryUserStorage());
        user = new User("mail@yandex.ru", "Login", "UserName",
                LocalDate.of(1995, 10, 05));
        user2 = new User("secondmail@mail.ru", "SecondLogin", "SecondUserName",
                LocalDate.of(1990, 4, 14));
    }

    @Test
    void shouldAddNewUser() {
        userService.addNewUser(user);
        List<User> actual = userService.getAllUsers();

        assertEquals(1, actual.size());
        assertEquals(user.getId(), actual.get(0).getId());
        assertEquals(user.getEmail(), actual.get(0).getEmail());
        assertEquals(user.getName(), actual.get(0).getName());
        assertEquals(user.getLogin(), actual.get(0).getLogin());
        assertEquals(user.getBirthday(), actual.get(0).getBirthday());
    }

    @Test
    void shouldAddNewUserWithIncorrectEmail() {
        userService.addNewUser(user);
        user.setEmail("mail.mail.ru");

        assertEquals(1, validator.validate(user).size());
    }

    @Test
    void shouldAddNewUserWithIncorrectName() {
        user.setName(" ");
        userService.addNewUser(user);

        assertEquals(user.getLogin(), user.getName());
    }

    @Test
    void shouldAddNewUserWithNullName() {
        user.setName(null);
        userService.addNewUser(user);

        assertEquals(user.getLogin(), user.getName());
    }

    @Test
    void shouldAddNewUserWithIncorrectLogin() {
        userService.addNewUser(user);
        user.setLogin(" ");

        assertEquals(1, validator.validate(user).size());
    }

    @Test
    void shouldAddNewUserWithIncorrectLoginWithWhitespace() {
        userService.addNewUser(user);
        user.setLogin("Login login");

        assertThrows(ValidationException.class, () -> {
            InMemoryUserStorage.validateUser(user);
        });
    }

    @Test
    void shouldAddNewUserWithIncorrectBirthday() {
        userService.addNewUser(user);
        user.setBirthday(LocalDate.of(2024, 10, 8));

        assertEquals(1, validator.validate(user).size());
    }

    @Test
    void shouldGetAllUsers() {
        userService.addNewUser(user);
        userService.addNewUser(user2);
        List<User> actual = userService.getAllUsers();

        assertEquals(2, actual.size());
        assertTrue(actual.contains(user));
        assertTrue(actual.contains(user2));
    }

    @Test
    void shouldUpdateUser() {
        userService.addNewUser(user);
        user.setName("NewName");
        user.setEmail("newemail@google.com");
        user.setLogin("NewLogin");
        user.setBirthday(LocalDate.of(1980, 6, 14));
        userService.updateUser(user);

        List<User> actual = userService.getAllUsers();
        assertEquals(user.getId(), actual.get(0).getId());
        assertEquals(user.getEmail(), actual.get(0).getEmail());
        assertEquals(user.getLogin(), actual.get(0).getLogin());
        assertEquals(user.getName(), actual.get(0).getName());
        assertEquals(user.getBirthday(), actual.get(0).getBirthday());
    }

    @Test
    void shouldUpdateNonExistedUser() {
        assertThrows(NotFoundException.class, () -> {
            userService.updateUser(user);
        });
    }
}
