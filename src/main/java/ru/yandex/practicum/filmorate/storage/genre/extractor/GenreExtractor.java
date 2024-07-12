package ru.yandex.practicum.filmorate.storage.genre.extractor;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class GenreExtractor implements ResultSetExtractor<List<Genre>> {
    @Override
    public List<Genre> extractData(ResultSet rs) throws SQLException, DataAccessException {
        List<Genre> genres = new ArrayList<>();
        while (rs.next()) {
            genres.add(new Genre(rs.getInt("id"), rs.getString("name")));
        }
        return genres;
    }
}
