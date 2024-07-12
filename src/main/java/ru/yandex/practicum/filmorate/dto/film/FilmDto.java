package ru.yandex.practicum.filmorate.dto.film;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.adapter.DurationDeserializer;
import ru.yandex.practicum.filmorate.adapter.DurationSerializer;
import ru.yandex.practicum.filmorate.adapter.LocalDateSerializer;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FilmDto {
    private int id;

    private String name;

    private String description;

    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate releaseDate;

    @JsonSerialize(using = DurationSerializer.class)
    @JsonDeserialize(using = DurationDeserializer.class)
    private Duration duration;

    private Set<Integer> likes;

    private Mpa mpa;

    private List<Genre> genres;
}
