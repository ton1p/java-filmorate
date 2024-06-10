package ru.yandex.practicum.filmorate.service.film;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class FilmServiceImpl implements FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Override
    public void likeFilm(int filmId, int userId) {
        Film film = filmStorage.getById(filmId);
        User user = userStorage.getById(userId);
        Set<Integer> filmLikes = film.getLikes();
        if (filmLikes == null) {
            filmLikes = new HashSet<>();
        }
        filmLikes.add(user.getId());
        film.setLikes(filmLikes);
    }

    @Override
    public void unlikeFilm(int filmId, int userId) {
        Film film = filmStorage.getById(filmId);
        User user = userStorage.getById(userId);
        Set<Integer> filmLikes = film.getLikes();
        if (filmLikes != null) {
            filmLikes.remove(user.getId());
            film.setLikes(filmLikes);
        }
    }

    @Override
    public List<Film> getPopular(int count) {
        Comparator<Film> comparator = (film1, film2) -> film2.getLikes().size() - film1.getLikes().size();
        return filmStorage.getAll()
                .stream()
                .filter(film -> film.getLikes() != null)
                .sorted(comparator)
                .limit(count)
                .toList();
    }
}
