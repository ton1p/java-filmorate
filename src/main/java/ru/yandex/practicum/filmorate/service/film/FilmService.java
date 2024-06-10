package ru.yandex.practicum.filmorate.service.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmService {
    void likeFilm(int filmId, int userId);

    void unlikeFilm(int filmId, int userId);

    List<Film> getPopular(int count);
}
