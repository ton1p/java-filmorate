package ru.yandex.practicum.filmorate.validator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

@Slf4j
@Component
public class FilmValidator implements Validator<Film> {
    static final LocalDate DATE_OF_BIRTHDAY_FILM = LocalDate.of(1895, 12, 28);

    @Override
    public boolean isValid(Film film) {
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
            if (film.getReleaseDate().isBefore(DATE_OF_BIRTHDAY_FILM)) {
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

        return true;
    }
}
