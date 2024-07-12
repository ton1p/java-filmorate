package ru.yandex.practicum.filmorate.validator.film;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validator.Validator;

import java.time.LocalDate;

@Slf4j
public class InMemoryFilmValidator implements Validator<Film> {
    static final LocalDate DATE_OF_BIRTHDAY_FILM = LocalDate.of(1895, 12, 28);

    @Override
    public boolean isValid(Film film) {
        FilmValidator.checkNotReferenceFields(film, log, DATE_OF_BIRTHDAY_FILM);
        return true;
    }
}
