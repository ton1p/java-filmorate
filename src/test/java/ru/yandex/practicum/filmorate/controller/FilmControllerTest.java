package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.dto.film.CreateFilmDto;
import ru.yandex.practicum.filmorate.dto.film.UpdateFilmDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import ru.yandex.practicum.filmorate.service.film.FilmServiceImpl;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.service.user.UserServiceImpl;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.validator.user.UserValidator;

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
        Film film = Film.builder()
                .id(1)
                .name("test")
                .description("test")
                .releaseDate(LocalDate.now())
                .duration(Duration.ofMinutes(90))
                .likes(new HashSet<>())
                .build();
        CreateFilmDto createFilmDto = FilmMapper.INSTANCE.filmToCreateFilmDto(film);
        filmController.createFilm(createFilmDto
        );
        assertEquals(1, filmController.getFilms().size());
    }

    @Test
    void createFilm() {
        Film film1 = Film.builder()
                .id(1)
                .name("test")
                .description("test")
                .releaseDate(LocalDate.now())
                .duration(Duration.ofMinutes(90))
                .likes(new HashSet<>())
                .build();
        CreateFilmDto createFilmDto = FilmMapper.INSTANCE.filmToCreateFilmDto(film1);
        Film film = filmController.createFilm(
                createFilmDto
        );
        assertEquals("test", film.getName());
    }

    @Test
    void updateFilm() {
        Film film = Film.builder()
                .id(1)
                .name("test")
                .description("test")
                .releaseDate(LocalDate.now())
                .duration(Duration.ofMinutes(90))
                .likes(new HashSet<>())
                .build();
        CreateFilmDto createFilmDto = FilmMapper.INSTANCE.filmToCreateFilmDto(film);
        filmController.createFilm(
                createFilmDto
        );
        Film updatedFilm = Film.builder()
                .id(1)
                .name("test updated")
                .description("test")
                .releaseDate(LocalDate.now())
                .duration(Duration.ofMinutes(90))
                .likes(new HashSet<>())
                .build();
        UpdateFilmDto updateFilmDto = FilmMapper.INSTANCE.filmToUpdateFilmDto(updatedFilm);
        Film updated = filmController.updateFilm(
                updateFilmDto
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

        UpdateFilmDto updateFilmDto = FilmMapper.INSTANCE.filmToUpdateFilmDto(film);

        assertThrows(NotFoundException.class, () -> filmController.updateFilm(updateFilmDto));
    }

    @Test
    void shouldThrowValidationExceptionWhenNameIsNullOrEmpty() {
        Film film = new Film();
        film.setId(1);
        film.setDescription("test");
        film.setDuration(Duration.ofMinutes(90));
        film.setReleaseDate(LocalDate.now());

        CreateFilmDto createFilmDto = FilmMapper.INSTANCE.filmToCreateFilmDto(film);

        assertThrows(ValidationException.class, () -> filmController.createFilm(createFilmDto));

        film.setName("");
        assertThrows(ValidationException.class, () -> filmController.createFilm(createFilmDto));
    }

    @Test
    void shouldThrowValidationExceptionWhenDescriptionIsNullOrEmpty() {
        Film film = new Film();
        film.setId(1);
        film.setName("test");
        film.setDuration(Duration.ofMinutes(90));
        film.setReleaseDate(LocalDate.now());

        CreateFilmDto createFilmDto = FilmMapper.INSTANCE.filmToCreateFilmDto(film);

        assertThrows(ValidationException.class, () -> filmController.createFilm(createFilmDto));

        film.setDescription("");
        assertThrows(ValidationException.class, () -> filmController.createFilm(createFilmDto));
    }

    @Test
    void shouldThrowValidationExceptionWhenDescriptionLengthMoreThan200() {
        Film film = new Film();
        film.setId(1);
        film.setName("test");
        film.setDescription("test".repeat(51));
        film.setDuration(Duration.ofMinutes(90));
        film.setReleaseDate(LocalDate.now());

        CreateFilmDto createFilmDto = FilmMapper.INSTANCE.filmToCreateFilmDto(film);

        assertThrows(ValidationException.class, () -> filmController.createFilm(createFilmDto));
    }

    @Test
    void shouldThrowValidationExceptionWhenReleaseDateIsNull() {
        Film film = new Film();
        film.setId(1);
        film.setName("test");
        film.setDescription("test");
        film.setDuration(Duration.ofMinutes(90));
        CreateFilmDto createFilmDto = FilmMapper.INSTANCE.filmToCreateFilmDto(film);

        assertThrows(ValidationException.class, () -> filmController.createFilm(createFilmDto));
    }

    @Test
    void shouldThrowValidationExceptionWhenReleaseDateIsBeforeFilmsBirthday() {
        Film film = new Film();
        film.setId(1);
        film.setName("test");
        film.setDescription("test");
        film.setDuration(Duration.ofMinutes(90));
        film.setReleaseDate(LocalDate.of(1895, 12, 27));
        CreateFilmDto createFilmDto = FilmMapper.INSTANCE.filmToCreateFilmDto(film);

        assertThrows(ValidationException.class, () -> filmController.createFilm(createFilmDto));
    }

    @Test
    void shouldThrowValidationExceptionWhenDurationIsNull() {
        Film film = new Film();
        film.setId(1);
        film.setName("test");
        film.setDescription("test");
        film.setReleaseDate(LocalDate.now());
        CreateFilmDto createFilmDto = FilmMapper.INSTANCE.filmToCreateFilmDto(film);

        assertThrows(ValidationException.class, () -> filmController.createFilm(createFilmDto));
    }

    @Test
    void shouldThrowValidationExceptionWhenDurationIsZeroOrNegative() {
        Film film = new Film();
        film.setId(1);
        film.setName("test");
        film.setDescription("test");
        film.setReleaseDate(LocalDate.now());
        film.setDuration(Duration.ofMinutes(0));

        CreateFilmDto createFilmDto = FilmMapper.INSTANCE.filmToCreateFilmDto(film);
        assertThrows(ValidationException.class, () -> filmController.createFilm(createFilmDto));

        film.setDuration(Duration.ofMinutes(-1));

        assertThrows(ValidationException.class, () -> filmController.createFilm(createFilmDto));
    }
}
