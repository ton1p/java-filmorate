package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FilmControllerTest {
    FilmController filmController;

    @BeforeEach
    void beforeEach() {
        filmController = new FilmController();
    }

    @Test
    void getFilmsEmpty() {
        assertEquals(0, filmController.getFilms().size());
    }

    @Test
    void getFilms() {
        filmController.createFilm(new Film(1, "test", "test", LocalDate.now(), Duration.ofMinutes(90)));
        assertEquals(1, filmController.getFilms().size());
    }

    @Test
    void createFilm() {
        Film film = filmController.createFilm(new Film(1, "test", "test", LocalDate.now(), Duration.ofMinutes(90)));
        assertEquals("test", film.getName());
    }

    @Test
    void updateFilm() {
        filmController.createFilm(new Film(1, "test", "test", LocalDate.now(), Duration.ofMinutes(90)));
        Film updated = filmController.updateFilm(new Film(1, "test updated", "test", LocalDate.now(), Duration.ofMinutes(90)));
        assertEquals("test updated", updated.getName());
    }

    @Test
    void shouldThrowNotFoundExceptionWhenUpdatingByWrongId() {
        Film film = new Film(1, "test", "test", LocalDate.now(), Duration.ofMinutes(90));
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
