package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import ru.yandex.practicum.filmorate.service.film.FilmServiceImpl;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.service.user.UserServiceImpl;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.validator.UserValidator;

import java.time.Duration;
import java.time.LocalDate;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FilmControllerTest {
    FilmController filmController;

    @BeforeEach
    void beforeEach() {
        FilmStorage filmStorage = new InMemoryFilmStorage();
        UserStorage userStorage = new InMemoryUserStorage();
        UserValidator userValidator = new UserValidator();
        UserService userService = new UserServiceImpl(userStorage, userValidator);
        FilmService filmService = new FilmServiceImpl(filmStorage, userService);
        filmController = new FilmController(filmService);
    }

    @Test
    void getFilmsEmpty() {
        assertEquals(0, filmController.getFilms().size());
    }

    @Test
    void getFilms() {
        filmController.createFilm(Film.builder()
                .id(1)
                .name("test")
                .description("test")
                .releaseDate(LocalDate.now())
                .duration(Duration.ofMinutes(90))
                .likes(new HashSet<>())
                .build()
        );
        assertEquals(1, filmController.getFilms().size());
    }

    @Test
    void createFilm() {
        Film film = filmController.createFilm(
                Film.builder()
                        .id(1)
                        .name("test")
                        .description("test")
                        .releaseDate(LocalDate.now())
                        .duration(Duration.ofMinutes(90))
                        .likes(new HashSet<>())
                        .build()
        );
        assertEquals("test", film.getName());
    }

    @Test
    void updateFilm() {
        filmController.createFilm(
                Film.builder()
                        .id(1)
                        .name("test")
                        .description("test")
                        .releaseDate(LocalDate.now())
                        .duration(Duration.ofMinutes(90))
                        .likes(new HashSet<>())
                        .build()
        );
        Film updated = filmController.updateFilm(
                Film.builder()
                        .id(1)
                        .name("test updated")
                        .description("test")
                        .releaseDate(LocalDate.now())
                        .duration(Duration.ofMinutes(90))
                        .likes(new HashSet<>())
                        .build()
        );
        assertEquals("test updated", updated.getName());
    }

    @Test
    void shouldThrowNotFoundExceptionWhenUpdatingByWrongId() {
        Film film = Film.builder()
                .id(1)
                .name("test")
                .description("test")
                .releaseDate(LocalDate.now())
                .duration(Duration.ofMinutes(90))
                .likes(new HashSet<>())
                .build();

        assertThrows(NotFoundException.class, () -> filmController.updateFilm(film));
    }

    @Test
    void shouldThrowValidationExceptionWhenNameIsNullOrEmpty() {
        Film film = new Film();
        film.setId(1);
        film.setDescription("test");
        film.setDuration(Duration.ofMinutes(90));
        film.setReleaseDate(LocalDate.now());

        assertThrows(ValidationException.class, () -> filmController.createFilm(film));

        film.setName("");
        assertThrows(ValidationException.class, () -> filmController.createFilm(film));
    }

    @Test
    void shouldThrowValidationExceptionWhenDescriptionIsNullOrEmpty() {
        Film film = new Film();
        film.setId(1);
        film.setName("test");
        film.setDuration(Duration.ofMinutes(90));
        film.setReleaseDate(LocalDate.now());

        assertThrows(ValidationException.class, () -> filmController.createFilm(film));

        film.setDescription("");
        assertThrows(ValidationException.class, () -> filmController.createFilm(film));
    }

    @Test
    void shouldThrowValidationExceptionWhenDescriptionLengthMoreThan200() {
        Film film = new Film();
        film.setId(1);
        film.setName("test");
        film.setDescription("test".repeat(51));
        film.setDuration(Duration.ofMinutes(90));
        film.setReleaseDate(LocalDate.now());

        assertThrows(ValidationException.class, () -> filmController.createFilm(film));
    }

    @Test
    void shouldThrowValidationExceptionWhenReleaseDateIsNull() {
        Film film = new Film();
        film.setId(1);
        film.setName("test");
        film.setDescription("test");
        film.setDuration(Duration.ofMinutes(90));

        assertThrows(ValidationException.class, () -> filmController.createFilm(film));
    }

    @Test
    void shouldThrowValidationExceptionWhenReleaseDateIsBeforeFilmsBirthday() {
        Film film = new Film();
        film.setId(1);
        film.setName("test");
        film.setDescription("test");
        film.setDuration(Duration.ofMinutes(90));
        film.setReleaseDate(LocalDate.of(1895, 12, 27));

        assertThrows(ValidationException.class, () -> filmController.createFilm(film));
    }

    @Test
    void shouldThrowValidationExceptionWhenDurationIsNull() {
        Film film = new Film();
        film.setId(1);
        film.setName("test");
        film.setDescription("test");
        film.setReleaseDate(LocalDate.now());

        assertThrows(ValidationException.class, () -> filmController.createFilm(film));
    }

    @Test
    void shouldThrowValidationExceptionWhenDurationIsZeroOrNegative() {
        Film film = new Film();
        film.setId(1);
        film.setName("test");
        film.setDescription("test");
        film.setReleaseDate(LocalDate.now());
        film.setDuration(Duration.ofMinutes(0));

        assertThrows(ValidationException.class, () -> filmController.createFilm(film));

        film.setDuration(Duration.ofMinutes(-1));

        assertThrows(ValidationException.class, () -> filmController.createFilm(film));
    }
}
