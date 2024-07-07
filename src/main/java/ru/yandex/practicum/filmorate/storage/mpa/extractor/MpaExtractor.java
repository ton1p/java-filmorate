package ru.yandex.practicum.filmorate.storage.mpa.extractor;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class MpaExtractor implements ResultSetExtractor<List<Mpa>> {
    @Override
    public List<Mpa> extractData(ResultSet rs) throws SQLException, DataAccessException {
        final List<Mpa> mpaList = new ArrayList<>();
        while (rs.next()) {
            final Mpa mpa = new Mpa();
            mpa.setId(rs.getInt("id"));
            mpa.setName(rs.getString("name"));
            mpaList.add(mpa);
        }
        return mpaList;
    }
}
