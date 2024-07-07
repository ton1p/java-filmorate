package ru.yandex.practicum.filmorate.storage.film.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class FilmRowMapper implements RowMapper<Film> {
    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        Set<Integer> genreIds = new HashSet<>();
        Set<Integer> likes = new HashSet<>();
        List<Genre> genres = new ArrayList<>();

        int userIdLikes = rs.getInt("USER_ID_LIKE");
        if (userIdLikes != 0) {
            likes.add(userIdLikes);
        }

        int genreId = rs.getInt("GENRE_ID");
        String genreName = rs.getString("GENRE_NAME");
        if (genreId != 0) {
            genreIds.add(genreId);
            genres.add(new Genre(genreId, genreName));
        }

        Film film = Film.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .releaseDate(rs.getDate("release_date").toLocalDate())
                .duration(Duration.ofMinutes(rs.getInt("duration")))
                .mpa(new Mpa(rs.getInt("mpa"), rs.getString("MPA_NAME")))
                .likes(likes)
                .genres(genres)
                .build();

        while (rs.next()) {
            int genreId1 = rs.getInt("genre_id");
            String genreName1 = rs.getString("genre_name");
            if (!genreIds.contains(genreId1)) {
                genreIds.add(genreId1);
                film.getGenres().add(new Genre(genreId1, genreName1));
            }
            int userIdLike = rs.getInt("USER_ID_LIKE");
            film.getLikes().add(userIdLike);
        }

        return film;
    }
}
