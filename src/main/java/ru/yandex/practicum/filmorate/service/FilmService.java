package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validator.FilmValidator;
import ru.yandex.practicum.filmorate.validator.Validator;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class FilmService implements RestService<Film> {
    final Map<Integer, Film> filmMap;
    final Validator<Film> validator;

    public FilmService() {
        this.filmMap = new HashMap<>();
        this.validator = new FilmValidator();
    }

    @Override
    public int getNextId() {
        int currentMaxId = filmMap.keySet().stream().mapToInt(value -> value).max().orElse(0);
        return ++currentMaxId;
    }

    @Override
    public Collection<Film> getAll() {
        return filmMap.values();
    }

    @Override
    public Film create(Film film) {
        if (validator.isValid(film)) {
            film.setId(getNextId());
            filmMap.put(film.getId(), film);
        }
        return film;
    }

    @Override
    public Film update(Film film) {
        Film filmFound = filmMap.get(film.getId());

        if (filmFound == null) {
            log.error("Фильм с id {} не найден", film.getId());
            throw new NotFoundException(String.format("Фильм с id %s не найден", film.getId()));
        }

        if (validator.isValid(film)) {
            filmMap.put(filmFound.getId(), film);
        }

        return film;
    }
}
