package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import ru.yandex.practicum.filmorate.dto.ObjectWithIdDto;
import ru.yandex.practicum.filmorate.dto.film.CreateFilmDto;
import ru.yandex.practicum.filmorate.dto.film.UpdateFilmDto;
import ru.yandex.practicum.filmorate.dto.user.CreateUserDto;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.extractor.FilmExtractor;
import ru.yandex.practicum.filmorate.storage.film.mapper.FilmRowMapper;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorageImpl;
import ru.yandex.practicum.filmorate.storage.genre.extractor.GenreExtractor;
import ru.yandex.practicum.filmorate.storage.genre.mapper.GenreRowMapper;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorageImpl;
import ru.yandex.practicum.filmorate.storage.mpa.extractor.MpaExtractor;
import ru.yandex.practicum.filmorate.storage.mpa.mapper.MpaRowMapper;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.storage.user.extractor.UserExtractor;
import ru.yandex.practicum.filmorate.storage.user.mapper.UserRowMapper;
import ru.yandex.practicum.filmorate.validator.film.FilmValidator;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ContextConfiguration(classes = {
        FilmDbStorage.class,
        FilmRowMapper.class,
        FilmExtractor.class,
        FilmValidator.class,
        MpaStorageImpl.class,
        GenreStorageImpl.class,
        MpaRowMapper.class,
        MpaExtractor.class,
        GenreRowMapper.class,
        GenreExtractor.class,
        UserDbStorage.class,
        UserRowMapper.class,
        UserExtractor.class
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class FilmDbStorageTest {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Test
    void getAll() {
        Collection<Film> films = filmStorage.getAll();
        assertNotNull(films);
        assertEquals(0, films.size());
    }

    private Film createFilm() {
        CreateFilmDto createFilmDto = CreateFilmDto.builder()
                .name("test")
                .description("test")
                .releaseDate(LocalDate.now())
                .duration(Duration.ofMinutes(120))
                .mpa(new ObjectWithIdDto(1))
                .genres(List.of(new ObjectWithIdDto(1)))
                .build();
        return filmStorage.create(createFilmDto);
    }

    private User createUser() {
        CreateUserDto createUserDto = CreateUserDto.builder()
                .email("t@t.ru")
                .login("test")
                .name("t")
                .birthday(LocalDate.now())
                .build();

        return userStorage.create(createUserDto);
    }

    @Test
    void getById() {
        Film film = createFilm();
        assertNotNull(film);
        assertEquals(1, film.getId());
        assertEquals("test", film.getName());
        assertEquals(1, film.getMpa().getId());
        assertEquals("G", film.getMpa().getName());
        assertEquals("Комедия", film.getGenres().getFirst().getName());
    }

    @Test
    void create() {
        Film film = createFilm();

        assertNotNull(film);
        assertEquals(1, film.getId());
        assertEquals("test", film.getName());
    }

    @Test
    void update() {
        Film film = createFilm();

        film.setName("updated");
        UpdateFilmDto updateFilmDto = FilmMapper.INSTANCE.filmToUpdateFilmDto(film);

        Film updated = filmStorage.update(updateFilmDto);
        assertNotNull(updated);
        assertEquals("updated", updated.getName());
    }

    @Test
    void likeFilm() {
        Film film = createFilm();
        User user = createUser();
        filmStorage.likeFilm(film, user);
        Optional<Film> found = filmStorage.getById(film.getId());
        assertTrue(found.isPresent());
        assertTrue(found.get().getLikes().contains(user.getId()));
    }

    @Test
    void unlikeFilm() {
        Film film = createFilm();
        User user = createUser();

        filmStorage.likeFilm(film, user);
        Optional<Film> found = filmStorage.getById(film.getId());
        assertTrue(found.isPresent());
        assertEquals(1, found.get().getLikes().size());

        filmStorage.unlikeFilm(film, user);
        found = filmStorage.getById(film.getId());
        assertTrue(found.isPresent());
        assertEquals(0, found.get().getLikes().size());
    }

    @Test
    void getPopular() {
        Film film = createFilm();
        Film film2 = createFilm();
        Film film3 = createFilm();

        User user = createUser();
        User user2 = createUser();

        filmStorage.likeFilm(film, user);
        filmStorage.likeFilm(film, user2);
        filmStorage.likeFilm(film2, user);

        List<Film> popularFilms = filmStorage.getPopular(10);
        assertNotNull(popularFilms);
        assertEquals(3, popularFilms.size());
        Optional<Film> foundFilm1 = filmStorage.getById(film.getId());
        Optional<Film> foundFilm2 = filmStorage.getById(film2.getId());
        Optional<Film> foundFilm3 = filmStorage.getById(film3.getId());
        assertTrue(foundFilm1.isPresent());
        assertTrue(foundFilm2.isPresent());
        assertTrue(foundFilm3.isPresent());
        assertEquals(
                List.of(
                        foundFilm1.get(),
                        foundFilm2.get(),
                        foundFilm3.get()
                ),
                popularFilms
        );
    }
}
