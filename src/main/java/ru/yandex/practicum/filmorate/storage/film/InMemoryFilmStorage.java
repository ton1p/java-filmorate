package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validator.FilmValidator;
import ru.yandex.practicum.filmorate.validator.Validator;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> filmMap;
    private final Validator<Film> validator;

    public InMemoryFilmStorage() {
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
    public Film getById(int id) {
        Film film = filmMap.get(id);

        if (film == null) {
            log.error("Фильм с id {} не найден", id);
            throw new NotFoundException(String.format("Фильм с id %s не найден", id));
        }

        return film;
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
        Film filmToUpdate = getById(film.getId());

        if (validator.isValid(film)) {
            filmMap.put(filmToUpdate.getId(), film);
        }

        return film;
    }
}
