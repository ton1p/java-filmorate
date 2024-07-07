package ru.yandex.practicum.filmorate.service.film;

import ru.yandex.practicum.filmorate.dto.film.CreateFilmDto;
import ru.yandex.practicum.filmorate.dto.film.UpdateFilmDto;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;

public interface FilmService {
    Collection<Film> getAll();

    Film getById(int id);

    Film create(CreateFilmDto createFilmDto);

    Film update(UpdateFilmDto updateFilmDto);

    void likeFilm(int filmId, int userId);

    void unlikeFilm(int filmId, int userId);

    List<Film> getPopular(int count);
}
