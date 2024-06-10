package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.adapter.LocalDateDeserializer;
import ru.yandex.practicum.filmorate.adapter.LocalDateSerializer;

import java.time.LocalDate;
import java.util.Set;

/**
 * User.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private int id;
    private String email;
    private String login;
    private String name;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    LocalDate birthday;

    private Set<Integer> friends;
}
