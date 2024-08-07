package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.dto.film.CreateFilmDto;
import ru.yandex.practicum.filmorate.dto.film.UpdateFilmDto;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    Collection<Film> getAll();

    Optional<Film> getById(int id);

    Film create(CreateFilmDto createFilmDto);

    Film update(UpdateFilmDto updateFilmDto);

    void likeFilm(Film film, User user);

    void unlikeFilm(Film film, User user);

    List<Film> getPopular(int count);
}
