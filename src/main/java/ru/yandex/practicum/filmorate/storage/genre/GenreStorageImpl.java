package ru.yandex.practicum.filmorate.storage.genre;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.BaseStorage;

import java.util.List;
import java.util.Optional;

@Repository
public class GenreStorageImpl extends BaseStorage<Genre> implements GenreStorage {
    private static final String GET_ALL_QUERY = "select * from \"genre\"";

    public GenreStorageImpl(JdbcTemplate jdbcTemplate, RowMapper<Genre> mapper, ResultSetExtractor<List<Genre>> extractor) {
        super(jdbcTemplate, mapper, extractor);
    }

    @Override
    public List<Genre> getAll() {
        return findMany(GET_ALL_QUERY);
    }

    @Override
    public Optional<Genre> getById(int id) {
        return findOne(GET_ALL_QUERY + " where \"id\" = ?", id);
    }
}
