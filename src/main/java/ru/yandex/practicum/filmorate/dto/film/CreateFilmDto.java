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
import ru.yandex.practicum.filmorate.dto.ObjectWithIdDto;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateFilmDto {
    private String name;

    private String description;

    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate releaseDate;

    @JsonSerialize(using = DurationSerializer.class)
    @JsonDeserialize(using = DurationDeserializer.class)
    private Duration duration;

    private ObjectWithIdDto mpa;

    private List<ObjectWithIdDto> genres;
}
