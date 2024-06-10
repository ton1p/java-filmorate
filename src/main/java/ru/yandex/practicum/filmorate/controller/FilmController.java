package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Collection;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmController {
    private final FilmStorage filmStorage;
    private final FilmService filmService;

    @GetMapping
    public Collection<Film> getFilms() {
        log.info("Получение всех фильмов");
        return filmStorage.getAll();
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable("id") int id) {
        return filmStorage.getById(id);
    }

    @PostMapping
    public Film createFilm(@RequestBody Film body) {
        log.info("Создание нового фильма");
        return filmStorage.create(body);
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film body) {
        log.info("Обновление фильма");
        return filmStorage.update(body);
    }

    @PutMapping("/{id}/like/{userId}")
    public void likeFilm(@PathVariable("id") int id, @PathVariable("userId") int userId) {
        filmService.likeFilm(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void unlikeFilm(@PathVariable("id") int id, @PathVariable("userId") int userId) {
        filmService.unlikeFilm(id, userId);
    }

    @GetMapping("/popular")
    public Collection<Film> getFilmsPopular(@RequestParam(defaultValue = "10") int count) {
        return filmService.getPopular(count);
    }
}
