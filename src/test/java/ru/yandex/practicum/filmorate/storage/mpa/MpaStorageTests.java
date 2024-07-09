package ru.yandex.practicum.filmorate.storage.mpa;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.ContextConfiguration;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.extractor.MpaExtractor;
import ru.yandex.practicum.filmorate.storage.mpa.mapper.MpaRowMapper;

import java.util.List;
import java.util.Optional;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ContextConfiguration(classes = {MpaStorageImpl.class, MpaRowMapper.class, MpaExtractor.class})
class MpaStorageTests {
    private final MpaStorage mpaStorage;

    @Test
    void getAll() {
        List<Mpa> mpaList = mpaStorage.getAll();
        Assertions.assertEquals(5, mpaList.size());
    }

    @Test
    void add() {
        Mpa created = mpaStorage.add("test");
        Assertions.assertNotNull(created);
    }

    @Test
    void getById() {
        Mpa created = mpaStorage.add("test");
        Optional<Mpa> optionalMpa = mpaStorage.getById(created.getId());
        Assertions.assertTrue(optionalMpa.isPresent());
        Assertions.assertEquals("test", optionalMpa.get().getName());
    }
}
