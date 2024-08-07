package ru.yandex.practicum.filmorate.storage.genre;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.ContextConfiguration;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.extractor.GenreExtractor;
import ru.yandex.practicum.filmorate.storage.genre.mapper.GenreRowMapper;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ContextConfiguration(classes = {GenreStorageImpl.class, GenreRowMapper.class, GenreExtractor.class})
class GenreStorageTest {
    private final GenreStorage genreStorage;

    @Test
    void getAll() {
        final List<Genre> genres = genreStorage.getAll();
        assertNotNull(genres);
        assertEquals(6, genres.size());
    }

    @Test
    void getById() {
        Genre genre = genreStorage.add("test");
        Optional<Genre> found = genreStorage.getById(genre.getId());
        assertTrue(found.isPresent());
        assertEquals("test", found.get().getName());
    }

    @Test
    void add() {
        Genre genre = genreStorage.add("test");
        assertNotNull(genre);
        assertEquals("test", genre.getName());
    }
}
