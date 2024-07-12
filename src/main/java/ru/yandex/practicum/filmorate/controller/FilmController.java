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
import ru.yandex.practicum.filmorate.dto.film.CreateFilmDto;
import ru.yandex.practicum.filmorate.dto.film.FilmDto;
import ru.yandex.practicum.filmorate.dto.film.UpdateFilmDto;
import ru.yandex.practicum.filmorate.service.film.FilmService;

import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    @GetMapping
    public Collection<FilmDto> getFilms() {
        log.info("Получение всех фильмов");
        return filmService.getAll();
    }

    @GetMapping("/{id}")
    public FilmDto getFilmById(@PathVariable("id") int id) {
        log.info("Получение фильма по id = {}", id);
        return filmService.getById(id);
    }

    @PostMapping
    public FilmDto createFilm(@RequestBody CreateFilmDto body) {
        log.info("Создание нового фильма");
        return filmService.create(body);
    }

    @PutMapping
    public FilmDto updateFilm(@RequestBody UpdateFilmDto body) {
        log.info("Обновление фильма");
        return filmService.update(body);
    }

    @PutMapping("/{id}/like/{userId}")
    public void likeFilm(@PathVariable("id") int id, @PathVariable("userId") int userId) {
        log.info("Юзер с id = {} ставит лайк фильму с id = {}", userId, id);
        filmService.likeFilm(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void unlikeFilm(@PathVariable("id") int id, @PathVariable("userId") int userId) {
        log.info("Юзеру с id = {} больше не нравится фильм с id = {}", userId, id);
        filmService.unlikeFilm(id, userId);
    }

    @GetMapping("/popular")
    public List<FilmDto> getFilmsPopular(@RequestParam(defaultValue = "10") int count) {
        log.info("Получение самых популярных фильмов с параметром count = {}", count);
        return filmService.getPopular(count);
    }
}
