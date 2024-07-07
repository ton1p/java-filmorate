package ru.yandex.practicum.filmorate.validator.film;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;
import ru.yandex.practicum.filmorate.validator.Validator;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component
@Primary
@AllArgsConstructor
public class FilmValidator implements Validator<Film> {
    static final LocalDate DATE_OF_BIRTHDAY_FILM = LocalDate.of(1895, 12, 28);
    private final MpaStorage mpaStorage;
    private final GenreStorage genreStorage;

    @Override
    public boolean isValid(Film film) {
        checkNotReferenceFields(film, log, DATE_OF_BIRTHDAY_FILM);

        Mpa filmMpa = film.getMpa();
        if (filmMpa != null) {
            mpaStorage.getById(
                    filmMpa.getId()
            ).orElseThrow(() -> {
                throw new ValidationException("Рейтинг с id = " + filmMpa.getId() + " не найден");
            });
        }

        List<Genre> filmGenres = film.getGenres();
        if (filmGenres != null && !filmGenres.isEmpty()) {
            filmGenres.forEach(
                    genre -> genreStorage.getById(genre.getId()).orElseThrow(() -> {
                        throw new ValidationException("Жанр с id = " + genre.getId() + " не найден");
                    })
            );
        }

        return true;
    }

    static void checkNotReferenceFields(Film film, Logger log, LocalDate dateOfBirthdayFilm) {
        if (film.getName() == null || film.getName().isBlank()) {
            log.error("Название не может быть пустым");
            throw new ValidationException("Название не может быть пустым");
        }

        if (film.getDescription() == null || film.getDescription().isBlank()) {
            log.error("Описание обязательно к заполнению");
            throw new ValidationException("Описание обязательно к заполнению");
        } else {
            if (film.getDescription().length() > 200) {
                log.error("Максимальная длина описания — 200 символов");
                throw new ValidationException("Максимальная длина описания — 200 символов");
            }
        }

        if (film.getReleaseDate() == null) {
            log.error("Дата релиза обязательна к заполнению");
            throw new ValidationException("Дата релиза обязательна к заполнению");
        } else {
            if (film.getReleaseDate().isBefore(dateOfBirthdayFilm)) {
                log.error("Дата релиза должна быть не раньше 28 декабря 1895 года");
                throw new ValidationException("Дата релиза должна быть не раньше 28 декабря 1895 года");
            }
        }

        if (film.getDuration() == null) {
            log.error("Продолжительность обязательна к заполнению");
            throw new ValidationException("Продолжительность обязательна к заполнению");
        } else {
            if (film.getDuration().isZero() || film.getDuration().isNegative()) {
                log.error("Продолжительность фильма должна быть положительным числом");
                throw new ValidationException("Продолжительность фильма должна быть положительным числом");
            }
        }
    }
}
