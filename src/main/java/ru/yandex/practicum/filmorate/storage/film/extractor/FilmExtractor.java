package ru.yandex.practicum.filmorate.storage.film.extractor;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class FilmExtractor implements ResultSetExtractor<List<Film>> {
    @Override
    public List<Film> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<Integer, Film> filmIdToFilm = new HashMap<>();
        Map<Integer, Set<Integer>> filmIdToGenreIds = new HashMap<>();

        while (rs.next()) {
            int filmId = rs.getInt("id");
            String name = rs.getString("name");
            String description = rs.getString("description");
            LocalDate releaseDate = rs.getDate("release_date").toLocalDate();
            Duration duration = Duration.ofMinutes(rs.getInt("duration"));
            int mpaId = rs.getInt("mpa");
            String mpaName = rs.getString("MPA_NAME");
            int userIdLike = rs.getInt("USER_ID_LIKE");
            int genreId = rs.getInt("GENRE_ID");
            String genreName = rs.getString("GENRE_NAME");

            Film film;
            Film filmFromMap = filmIdToFilm.get(filmId);

            if (filmFromMap != null) {
                film = filmFromMap;
                if (filmIdToGenreIds.containsKey(filmId) && !filmIdToGenreIds.get(filmId).contains(genreId)) {
                    filmIdToGenreIds.get(filmId).add(genreId);

                    List<Genre> genres = new ArrayList<>(film.getGenres());
                    genres.add(new Genre(genreId, genreName));
                    film.setGenres(genres);
                }
                if (userIdLike != 0) {
                    Set<Integer> likes = film.getLikes();
                    likes.add(userIdLike);
                    filmFromMap.setLikes(likes);
                }
            } else {
                Set<Integer> genreIds = new HashSet<>();
                genreIds.add(genreId);
                filmIdToGenreIds.putIfAbsent(filmId, genreIds);

                List<Genre> genres = new ArrayList<>();

                Set<Integer> likes = new HashSet<>();

                film = Film.builder()
                        .id(filmId)
                        .name(name)
                        .description(description)
                        .releaseDate(releaseDate)
                        .duration(duration)
                        .mpa(new Mpa(mpaId, mpaName))
                        .likes(likes)
                        .genres(genres)
                        .build();

                if (userIdLike != 0) {
                    likes.add(userIdLike);
                    film.setLikes(likes);
                }

                if (genreId != 0) {
                    genres.add(new Genre(genreId, genreName));
                    film.setGenres(genres);
                }
            }

            filmIdToFilm.putIfAbsent(filmId, film);
        }

        return new ArrayList<>(filmIdToFilm.values());
    }
}
