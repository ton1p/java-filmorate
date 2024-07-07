package ru.yandex.practicum.filmorate.dto.film;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import ru.yandex.practicum.filmorate.adapter.DurationDeserializer;
import ru.yandex.practicum.filmorate.adapter.DurationSerializer;
import ru.yandex.practicum.filmorate.adapter.LocalDateSerializer;
import ru.yandex.practicum.filmorate.dto.ObjectWithIdDto;

import java.time.Duration;
import java.time.LocalDate;

@Data
public class UpdateFilmDto {
    private int id;

    private String name;

    private String description;

    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate releaseDate;

    @JsonSerialize(using = DurationSerializer.class)
    @JsonDeserialize(using = DurationDeserializer.class)
    private Duration duration;

    private ObjectWithIdDto mpa;
}
