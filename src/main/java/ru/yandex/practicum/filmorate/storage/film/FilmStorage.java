package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {
    Collection<Film> getAll();

    Film getById(int id);

    Film create(Film o);

    Film update(Film o);

    int getNextId();
}
