package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.dto.user.CreateUserDto;
import ru.yandex.practicum.filmorate.dto.user.UpdateUserDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.service.user.UserServiceImpl;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.validator.user.UserValidator;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserControllerTest {
    UserController userController;

    @BeforeEach
    void beforeEach() {
        UserStorage userStorage = new InMemoryUserStorage();
        UserValidator userValidator = new UserValidator();
        UserService userService = new UserServiceImpl(userStorage, userValidator);
        userController = new UserController(userService);
    }

    @Test
    void getUsers() {
        assertEquals(0, userController.getUsers().size());
    }

    @Test
    void createUser() {
        CreateUserDto createUserDto = CreateUserDto
                .builder()
                .email("test@test.com")
                .login("login")
                .name("name")
                .birthday(LocalDate.now())
                .build();
        userController.createUser(createUserDto);
        assertEquals(1, userController.getUsers().size());
    }

    @Test
    void updateUser() {
        CreateUserDto userDto = CreateUserDto
                .builder()
                .email("test@test.com")
                .login("login")
                .name("name")
                .birthday(LocalDate.now())
                .build();

        User user = userController.createUser(userDto);
        user.setName("updated");
        UpdateUserDto updateUserDto = UserMapper.INSTANCE.userToUpdateUserDto(user);
        User updated = userController.updateUser(updateUserDto);
        assertEquals("updated", updated.getName());
    }


    @Test
    void shouldThrowNotFoundExceptionWhenUpdatingByWrongId() {
        UpdateUserDto updateUserDto = UpdateUserDto
                .builder()
                .id(1)
                .email("test@test.com")
                .login("login")
                .name("name")
                .build();

        assertThrows(NotFoundException.class, () -> userController.updateUser(updateUserDto));
    }

    @Test
    void shouldThrowValidationExceptionWhenEmailIsNullOrEmpty() {
        CreateUserDto createUserDto = CreateUserDto
                .builder()
                .name("name")
                .login("login")
                .birthday(LocalDate.now())
                .build();

        assertThrows(ValidationException.class, () -> userController.createUser(createUserDto));

        createUserDto.setEmail("");

        assertThrows(ValidationException.class, () -> userController.createUser(createUserDto));
    }

    @Test
    void shouldThrowValidationExceptionWhenEmailIsIncorrect() {
        CreateUserDto createUserDto = CreateUserDto
                .builder()
                .name("name")
                .login("login")
                .birthday(LocalDate.now())
                .email("test")
                .build();

        assertThrows(ValidationException.class, () -> userController.createUser(createUserDto));
    }

    @Test
    void shouldThrowValidationExceptionWhenLoginIsNullOrEmpty() {
        CreateUserDto createUserDto = CreateUserDto
                .builder()
                .name("name")
                .birthday(LocalDate.now())
                .email("test@test.com")
                .build();

        assertThrows(ValidationException.class, () -> userController.createUser(createUserDto));

        createUserDto.setLogin("");

        assertThrows(ValidationException.class, () -> userController.createUser(createUserDto));
    }

    @Test
    void shouldThrowValidationExceptionWhenLoginContainSpace() {
        CreateUserDto createUserDto = CreateUserDto.builder()
                .name("name")
                .birthday(LocalDate.now())
                .email("test@test.com")
                .login("te st")
                .build();

        assertThrows(ValidationException.class, () -> userController.createUser(createUserDto));
    }

    @Test
    void shouldThrowValidationExceptionWhenBirthdayIsNull() {
        CreateUserDto createUserDto = CreateUserDto.builder()
                .name("name")
                .email("test@test.com")
                .login("test")
                .build();

        assertThrows(ValidationException.class, () -> userController.createUser(createUserDto));
    }

    @Test
    void shouldThrowValidationExceptionWhenBirthdayIsInFuture() {
        CreateUserDto createUserDto = CreateUserDto.builder()
                .name("name")
                .email("test@test.com")
                .login("test")
                .birthday(LocalDate.now().plusDays(1))
                .build();

        assertThrows(ValidationException.class, () -> userController.createUser(createUserDto));
    }
}
