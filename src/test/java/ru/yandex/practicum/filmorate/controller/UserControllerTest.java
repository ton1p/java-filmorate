package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.service.user.UserServiceImpl;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserControllerTest {
    UserController userController;

    @BeforeEach
    void beforeEach() {
        UserStorage userStorage = new InMemoryUserStorage();
        UserService userService = new UserServiceImpl(userStorage);
        userController = new UserController(userStorage, userService);
    }

    @Test
    void getUsers() {
        assertEquals(0, userController.getUsers().size());
    }

    @Test
    void createUser() {
        userController.createUser(User
                .builder()
                .id(1)
                .email("test@test.com")
                .login("login")
                .name("name")
                .birthday(LocalDate.now())
                .friends(new HashSet<>())
                .build()
        );
        assertEquals(1, userController.getUsers().size());
    }

    @Test
    void updateUser() {
        User user = User
                .builder()
                .id(1)
                .email("test@test.com")
                .login("login")
                .name("name")
                .birthday(LocalDate.now())
                .friends(new HashSet<>())
                .build();

        userController.createUser(user);
        user.setName("updated");
        User updated = userController.updateUser(user);
        assertEquals("updated", updated.getName());
    }


    @Test
    void shouldThrowNotFoundExceptionWhenUpdatingByWrongId() {
        User user = User
                .builder()
                .id(1)
                .email("test@test.com")
                .login("login")
                .name("name")
                .friends(new HashSet<>())
                .build();

        assertThrows(NotFoundException.class, () -> userController.updateUser(user));
    }

    @Test
    void shouldThrowValidationExceptionWhenEmailIsNullOrEmpty() {
        User user = User
                .builder()
                .id(1)
                .name("name")
                .login("login")
                .birthday(LocalDate.now())
                .build();

        assertThrows(ValidationException.class, () -> userController.createUser(user));

        user.setEmail("");

        assertThrows(ValidationException.class, () -> userController.createUser(user));
    }

    @Test
    void shouldThrowValidationExceptionWhenEmailIsIncorrect() {
        User user = User
                .builder()
                .id(1)
                .name("name")
                .login("login")
                .birthday(LocalDate.now())
                .email("test")
                .build();

        assertThrows(ValidationException.class, () -> userController.createUser(user));
    }

    @Test
    void shouldThrowValidationExceptionWhenLoginIsNullOrEmpty() {
        User user = User
                .builder()
                .id(1)
                .name("name")
                .birthday(LocalDate.now())
                .email("test@test.com")
                .build();

        assertThrows(ValidationException.class, () -> userController.createUser(user));

        user.setLogin("");

        assertThrows(ValidationException.class, () -> userController.createUser(user));
    }

    @Test
    void shouldThrowValidationExceptionWhenLoginContainSpace() {
        User user = new User();
        user.setId(1);
        user.setName("test");
        user.setBirthday(LocalDate.now());
        user.setEmail("test@test.com");
        user.setLogin("te st");

        assertThrows(ValidationException.class, () -> userController.createUser(user));
    }

    @Test
    void shouldThrowValidationExceptionWhenBirthdayIsNull() {
        User user = new User();
        user.setId(1);
        user.setName("test");
        user.setEmail("test@test.com");
        user.setLogin("test");

        assertThrows(ValidationException.class, () -> userController.createUser(user));
    }

    @Test
    void shouldThrowValidationExceptionWhenBirthdayIsInFuture() {
        User user = new User();
        user.setId(1);
        user.setName("test");
        user.setEmail("test@test.com");
        user.setLogin("test");
        user.setBirthday(LocalDate.now().plusDays(1));

        assertThrows(ValidationException.class, () -> userController.createUser(user));
    }
}
