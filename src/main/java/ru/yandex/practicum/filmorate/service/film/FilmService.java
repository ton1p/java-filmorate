package ru.yandex.practicum.filmorate.service.film;

import ru.yandex.practicum.filmorate.dto.film.CreateFilmDto;
import ru.yandex.practicum.filmorate.dto.film.FilmDto;
import ru.yandex.practicum.filmorate.dto.film.UpdateFilmDto;

import java.util.Collection;
import java.util.List;

public interface FilmService {
    Collection<FilmDto> getAll();

    FilmDto getById(int id);

    FilmDto create(CreateFilmDto createFilmDto);

    FilmDto update(UpdateFilmDto updateFilmDto);

    void likeFilm(int filmId, int userId);

    void unlikeFilm(int filmId, int userId);

    List<FilmDto> getPopular(int count);
}
