package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dto.ObjectWithIdDto;
import ru.yandex.practicum.filmorate.dto.film.CreateFilmDto;
import ru.yandex.practicum.filmorate.dto.film.UpdateFilmDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.BaseStorage;
import ru.yandex.practicum.filmorate.validator.Validator;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
@Primary
public class FilmDbStorage extends BaseStorage<Film> implements FilmStorage {
    private static final String GET_ALL_QUERY = "select f.*," +
            "l.\"user_id\" user_id_like," +
            "l.\"film_id\" liked_film_id," +
            "m.\"name\" mpa_name," +
            "fg.\"genre_id\" genre_id," +
            "g.\"name\" genre_name " +
            "from \"film\" f " +
            "left join \"likes\" l on f.\"id\" = l.\"film_id\" " +
            "left join \"mpa\" m on f.\"mpa\" = m.\"id\" " +
            "left join \"films_genres\" fg on f.\"id\" = fg.\"film_id\" " +
            "left join \"genre\" g on fg.\"genre_id\" = g.\"id\"";

    private static final String CREATE_FILM_QUERY = "insert into \"film\" (\"name\", \"description\", \"release_date\", \"duration\", \"mpa\") VALUES (?, ?, ?, ?, ?)";

    private static final String ADD_FILM_GENRE_QUERY = "insert into \"films_genres\" (\"film_id\", \"genre_id\") values (?, ?)";

    private static final String UPDATE_FILM_QUERY = "update \"film\" set \"name\" = ?, \"description\" = ?, \"release_date\" = ?, \"duration\" = ?, \"mpa\" = ? where \"id\" = ?;";

    public static String getAllQueryStringWithId() {
        return GET_ALL_QUERY + " where f.\"id\" = ?";
    }

    private final Validator<Film> filmValidator;

    public FilmDbStorage(
            JdbcTemplate jdbcTemplate,
            RowMapper<Film> mapper,
            ResultSetExtractor<List<Film>> extractor,
            Validator<Film> filmValidator
    ) {
        super(jdbcTemplate, mapper, extractor);
        this.filmValidator = filmValidator;
    }

    @Override
    public Collection<Film> getAll() {
        return findMany(GET_ALL_QUERY);
    }

    @Override
    public Optional<Film> getById(int id) {
        return findOne(getAllQueryStringWithId(), id);
    }

    public void addGenresToFilm(int filmId, List<ObjectWithIdDto> genres) {
        jdbcTemplate.batchUpdate(ADD_FILM_GENRE_QUERY, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ObjectWithIdDto genre = genres.get(i);
                ps.setInt(1, filmId);
                ps.setInt(2, genre.getId());
            }

            @Override
            public int getBatchSize() {
                return genres.size();
            }
        });
    }

    @Override
    public Film create(CreateFilmDto createFilmDto) {
        Film film = FilmMapper.INSTANCE.createFilmDtoToFilm(createFilmDto);
        if (filmValidator.isValid(film)) {
            int id = insert(
                    CREATE_FILM_QUERY,
                    createFilmDto.getName(),
                    createFilmDto.getDescription(),
                    createFilmDto.getReleaseDate().toString(),
                    createFilmDto.getDuration().toMinutes(),
                    createFilmDto.getMpa().getId()
            );
            addGenresToFilm(id, createFilmDto.getGenres());
            Optional<Film> createdFilm = findOne(getAllQueryStringWithId(), id);
            return createdFilm.orElse(null);
        }
        return null;
    }

    @Override
    public Film update(UpdateFilmDto updateFilmDto) {
        Optional<Film> founded = findOne(getAllQueryStringWithId(), updateFilmDto.getId());
        if (founded.isEmpty()) {
            throw new NotFoundException("Фильм с id = " + updateFilmDto.getId() + " не найден");
        }
        Film film = FilmMapper.INSTANCE.updateFilmDtoToFilm(updateFilmDto);
        if (filmValidator.isValid(film)) {
            update(
                    UPDATE_FILM_QUERY,
                    updateFilmDto.getName(),
                    updateFilmDto.getDescription(),
                    updateFilmDto.getReleaseDate().toString(),
                    updateFilmDto.getDuration().toMinutes(),
                    updateFilmDto.getMpa().getId(),
                    updateFilmDto.getId()
            );

            return findOne(getAllQueryStringWithId(), updateFilmDto.getId()).orElse(null);
        }
        return null;
    }

    @Override
    public void likeFilm(Film film, User user) {

    }

    @Override
    public void unlikeFilm(Film film, User user) {

    }

    @Override
    public List<Film> getPopular(int count) {
        return List.of();
    }
}
