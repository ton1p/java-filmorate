package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dto.film.CreateFilmDto;
import ru.yandex.practicum.filmorate.dto.film.UpdateFilmDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.Validator;
import ru.yandex.practicum.filmorate.validator.film.InMemoryFilmValidator;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> filmMap;
    private final Validator<Film> validator;

    public InMemoryFilmStorage() {
        this.filmMap = new HashMap<>();
        this.validator = new InMemoryFilmValidator();
    }

    public int getNextId() {
        int currentMaxId = filmMap.keySet().stream().mapToInt(value -> value).max().orElse(0);
        return ++currentMaxId;
    }

    @Override
    public Collection<Film> getAll() {
        return filmMap.values();
    }

    @Override
    public Optional<Film> getById(int id) {
        Film film = filmMap.get(id);

        if (film == null) {
            return Optional.empty();
        }

        return Optional.of(film);
    }

    @Override
    public Film create(CreateFilmDto createFilmDto) {
        Film film = FilmMapper.INSTANCE.createFilmDtoToFilm(createFilmDto);
        if (validator.isValid(film)) {
            film.setId(getNextId());
            filmMap.put(film.getId(), film);
        }
        return film;
    }

    @Override
    public Film update(UpdateFilmDto updateFilmDto) {
        Film film = FilmMapper.INSTANCE.updateFilmDtoToFilm(updateFilmDto);
        Film filmFromMap = filmMap.get(film.getId());
        if (filmFromMap == null) {
            throw new NotFoundException("Фильм с id =" + film.getId() + "не найден");
        }
        if (validator.isValid(film)) {
            filmMap.put(film.getId(), film);
        }
        return film;
    }

    @Override
    public void likeFilm(Film film, User user) {
        Set<Integer> filmLikes = film.getLikes();
        if (filmLikes == null) {
            filmLikes = new HashSet<>();
        }
        filmLikes.add(user.getId());
        film.setLikes(filmLikes);
    }

    @Override
    public void unlikeFilm(Film film, User user) {
        Set<Integer> filmLikes = film.getLikes();
        if (filmLikes != null) {
            filmLikes.remove(user.getId());
            film.setLikes(filmLikes);
        }
    }

    @Override
    public List<Film> getPopular(int count) {
        Comparator<Film> comparator = (film1, film2) -> film2.getLikes().size() - film1.getLikes().size();
        return getAll()
                .stream()
                .filter(film -> film.getLikes() != null)
                .sorted(comparator)
                .limit(count)
                .toList();
    }
}
