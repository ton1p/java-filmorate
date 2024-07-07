package ru.yandex.practicum.filmorate.storage.mpa;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.BaseStorage;

import java.util.List;
import java.util.Optional;

@Repository
public class MpaStorageImpl extends BaseStorage<Mpa> implements MpaStorage {
    private static final String GET_ALL_QUERY = "select * from \"mpa\"";

    public MpaStorageImpl(JdbcTemplate jdbcTemplate, RowMapper<Mpa> mapper, ResultSetExtractor<List<Mpa>> extractor) {
        super(jdbcTemplate, mapper, extractor);
    }

    @Override
    public List<Mpa> getAll() {
        return findMany(GET_ALL_QUERY);
    }

    @Override
    public Optional<Mpa> getById(int id) {
        return findOne(GET_ALL_QUERY + " where(\"id\" = ?)", id);
    }
}
