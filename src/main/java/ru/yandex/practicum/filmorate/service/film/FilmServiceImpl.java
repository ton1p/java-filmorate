package ru.yandex.practicum.filmorate.service.film;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.film.CreateFilmDto;
import ru.yandex.practicum.filmorate.dto.film.UpdateFilmDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.validator.Validator;

import java.util.Collection;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class FilmServiceImpl implements FilmService {
    private final FilmStorage filmStorage;
    private final UserService userService;
    private final Validator<Film> filmValidator;

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
        Film film = FilmMapper.INSTANCE.createFilmDtoToFilm(createFilmDto);
        if (filmValidator.isValid(film)) {
            return filmStorage.create(createFilmDto);
        }
        return null;
    }

    @Override
    public Film update(UpdateFilmDto updateFilmDto) {
        Film film = getById(updateFilmDto.getId());
        if (filmValidator.isValid(film)) {
            return filmStorage.update(updateFilmDto);
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
