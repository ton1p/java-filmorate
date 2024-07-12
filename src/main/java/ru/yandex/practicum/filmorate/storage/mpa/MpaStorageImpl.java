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
    private static final String CREATE_QUERY = "insert into \"mpa\" (\"name\") values (?)";

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

    @Override
    public Mpa add(String name) {
        int id = insert(CREATE_QUERY, name);
        return getById(id).orElse(null);
    }
}
