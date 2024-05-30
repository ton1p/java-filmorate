package ru.yandex.practicum.filmorate.validator;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class UserValidator implements Validator<User> {
    public static final Pattern VALID_EMAIL_PATTERN = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static final LocalDate CURRENT_LOCAL_DATE = LocalDate.now();

    @Override
    public boolean isValid(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            log.error("Электронная почта не может быть пустой");
            throw new ValidationException("Электронная почта не может быть пустой");
        }

        Matcher matcher = VALID_EMAIL_PATTERN.matcher(user.getEmail());

        if (!matcher.matches()) {
            log.error("Email адррес некорректный");
            throw new ValidationException("Email адррес некорректный");
        }

        if (user.getLogin() == null || user.getLogin().isBlank()) {
            log.error("Логин не может быть пустым");
            throw new ValidationException("Логин не может быть пустым");
        }

        if (user.getLogin().trim().contains(" ")) {
            log.error("Логин не должен содержать пробелы");
            throw new ValidationException("Логин не должен содержать пробелы");
        }

        if (user.getBirthday() == null) {
            log.error("Дата рождения не может быть пустой");
            throw new ValidationException("Дата рождения не может быть пустой");
        }

        if (user.getBirthday().isAfter(CURRENT_LOCAL_DATE)) {
            log.error("Дата рождения не может быть в будущем");
            throw new ValidationException("Дата рождения не может быть в будущем");
        }

        return true;
    }
}
