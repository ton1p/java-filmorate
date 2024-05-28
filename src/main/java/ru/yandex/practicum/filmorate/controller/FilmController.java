package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.RestService;

import java.util.Collection;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController extends BaseController {
    final RestService<Film> filmRestService = new FilmService();

    @GetMapping
    public Collection<Film> getFilms() {
        log.info("Получение всех фильмов");
        return filmRestService.getAll();
    }

    @PostMapping
    public Film createFilm(@RequestBody Film body) {
        log.info("Создание нового фильма");
        return filmRestService.create(body);
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film body) {
        log.info("Обновление фильма");
        return filmRestService.update(body);
    }
}
