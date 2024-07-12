package ru.yandex.practicum.filmorate.service.film;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.film.CreateFilmDto;
import ru.yandex.practicum.filmorate.dto.film.FilmDto;
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
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class FilmServiceImpl implements FilmService {
    private final FilmStorage filmStorage;
    private final UserService userService;
    private final Validator<Film> filmValidator;

    @Override
    public Collection<FilmDto> getAll() {
        return filmStorage.getAll().stream().map(FilmMapper.INSTANCE::filmToFilmDto).toList();
    }

    @Override
    public FilmDto getById(int id) {
        Optional<Film> film = filmStorage.getById(id);
        if (film.isPresent()) {
            return FilmMapper.INSTANCE.filmToFilmDto(film.get());
        }
        throw new NotFoundException(String.format("Фильм с id %s не найден", id));
    }

    @Override
    public FilmDto create(CreateFilmDto createFilmDto) {
        Film film = FilmMapper.INSTANCE.createFilmDtoToFilm(createFilmDto);
        if (filmValidator.isValid(film)) {
            Film created = filmStorage.create(createFilmDto);
            return FilmMapper.INSTANCE.filmToFilmDto(created);
        }
        return null;
    }

    @Override
    public FilmDto update(UpdateFilmDto updateFilmDto) {
        FilmDto filmDto = getById(updateFilmDto.getId());
        Film film = FilmMapper.INSTANCE.filmDtoToFilm(filmDto);
        if (filmValidator.isValid(film)) {
            Film updated = filmStorage.update(updateFilmDto);
            return FilmMapper.INSTANCE.filmToFilmDto(updated);
        }
        return null;
    }

    @Override
    public void likeFilm(int filmId, int userId) {
        FilmDto film = getById(filmId);
        User user = userService.getById(userId);
        Film toFilm = FilmMapper.INSTANCE.filmDtoToFilm(film);
        filmStorage.likeFilm(toFilm, user);
    }

    @Override
    public void unlikeFilm(int filmId, int userId) {
        FilmDto film = getById(filmId);
        User user = userService.getById(userId);
        Film toFilm = FilmMapper.INSTANCE.filmDtoToFilm(film);
        filmStorage.unlikeFilm(toFilm, user);
    }

    @Override
    public List<FilmDto> getPopular(int count) {
        return filmStorage.getPopular(count).stream().map(FilmMapper.INSTANCE::filmToFilmDto).toList();
    }
}
