package ru.yandex.practicum.filmorate.service.film;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

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
        Optional<Film> film = filmStorage.getById(id);
        if (film.isEmpty()) {
            log.error("Фильм с id {} не найден", id);
            throw new NotFoundException(String.format("Фильм с id %s не найден", id));
        }
        return film.get();
    }

    @Override
    public Film create(Film film) {
        return filmStorage.create(film);
    }

    @Override
    public Film update(Film film) {
        if (getById(film.getId()) != null) {
            return filmStorage.update(film);
        }
        return null;
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
