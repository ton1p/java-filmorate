package ru.yandex.practicum.filmorate.service.genre;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.util.Collection;

@Service
@AllArgsConstructor
@Slf4j
public class GenreServiceImpl implements GenreService {
    private GenreStorage genreStorage;

    @Override
    public Collection<Genre> getAll() {
        return genreStorage.getAll();
    }

    @Override
    public Genre getById(int id) {
        return genreStorage.getById(id).orElseThrow(() -> new NotFoundException("Genre with id " + id + " not found"));
    }
}
