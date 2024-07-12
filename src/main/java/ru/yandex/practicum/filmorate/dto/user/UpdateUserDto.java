package ru.yandex.practicum.filmorate.dto.user;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder(toBuilder = true)
public class UpdateUserDto extends CreateUserDto {
    private int id;

    public UpdateUserDto(String email, String login, String name, LocalDate birthday, int id) {
        super(email, login, name, birthday);
        this.id = id;
    }
}
