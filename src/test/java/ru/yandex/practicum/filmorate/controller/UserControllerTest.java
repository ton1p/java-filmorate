package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserControllerTest {
    UserController userController;

    @BeforeEach
    void beforeEach() {
        userController = new UserController();
    }

    @Test
    void getUsers() {
        assertEquals(0, userController.getUsers().size());
    }

    @Test
    void createUser() {
        userController.createUser(new User(1, "test@test.com", "login", "name", LocalDate.now()));
        assertEquals(1, userController.getUsers().size());
    }

    @Test
    void updateUser() {
        User user = new User(1, "test@test.com", "login", "name", LocalDate.now());
        userController.createUser(user);
        user.setName("updated");
        User updated = userController.updateUser(user);
        assertEquals("updated", updated.getName());
    }


    @Test
    void shouldThrowNotFoundExceptionWhenUpdatingByWrongId() {
        User user = new User(1, "test@test.com", "login", "name", LocalDate.now());
        assertThrows(NotFoundException.class, () -> userController.updateUser(user));
    }

    @Test
    void shouldThrowValidationExceptionWhenEmailIsNullOrEmpty() {
        User user = new User();
        user.setId(1);
        user.setName("test");
        user.setLogin("test");
        user.setBirthday(LocalDate.now());

        assertThrows(ValidationException.class, () -> userController.createUser(user));

        user.setEmail("");

        assertThrows(ValidationException.class, () -> userController.createUser(user));
    }

    @Test
    void shouldThrowValidationExceptionWhenEmailIsIncorrect() {
        User user = new User();
        user.setId(1);
        user.setName("test");
        user.setLogin("test");
        user.setBirthday(LocalDate.now());
        user.setEmail("test");

        assertThrows(ValidationException.class, () -> userController.createUser(user));
    }

    @Test
    void shouldThrowValidationExceptionWhenLoginIsNullOrEmpty() {
        User user = new User();
        user.setId(1);
        user.setName("test");
        user.setBirthday(LocalDate.now());
        user.setEmail("test@test.com");

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
