package ru.yandex.practicum.filmorate.service.film;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.film.CreateFilmDto;
import ru.yandex.practicum.filmorate.dto.film.UpdateFilmDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Collection;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class FilmServiceImpl implements FilmService {
    private final FilmStorage filmStorage;
    private final UserService userService;

    @Override
    public Collection<Film> getAll() {
        return filmStorage.getAll();
    }

    @Override
    public Film getById(int id) {
        return filmStorage.getById(id).orElseThrow(() -> {
            log.error("Фильм с id {} не найден", id);
            return new NotFoundException(String.format("Фильм с id %s не найден", id));
        });
    }

    @Override
    public Film create(CreateFilmDto createFilmDto) {
        return filmStorage.create(createFilmDto);
    }

    @Override
    public Film update(UpdateFilmDto updateFilmDto) {
        return filmStorage.update(updateFilmDto);
    }

    @Override
    public void likeFilm(int filmId, int userId) {
        Film film = getById(filmId);
        User user = userService.getById(userId);
        filmStorage.likeFilm(film, user);
    }

    @Override
    public void unlikeFilm(int filmId, int userId) {
        Film film = getById(filmId);
        User user = userService.getById(userId);
        filmStorage.unlikeFilm(film, user);
    }

    @Override
    public List<Film> getPopular(int count) {
        return filmStorage.getPopular(count);
    }
}
