package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.UserValidator;
import ru.yandex.practicum.filmorate.validator.Validator;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class UserService implements RestService<User> {
    final Map<Integer, User> userMap;
    final Validator<User> validator;

    public UserService() {
        this.userMap = new HashMap<>();
        this.validator = new UserValidator();
    }

    @Override
    public int getNextId() {
        int currentMaxId = userMap.keySet().stream().mapToInt(value -> value).max().orElse(0);
        return ++currentMaxId;
    }

    @Override
    public Collection<User> getAll() {
        return userMap.values();
    }

    @Override
    public User create(User user) {
        if (validator.isValid(user)) {
            user.setId(getNextId());
            userMap.put(user.getId(), user);
        }
        return user;
    }

    @Override
    public User update(User user) {
        User userFound = userMap.get(user.getId());

        if (userFound == null) {
            log.error("Пользователь с id {} не найден", user.getId());
            throw new NotFoundException(String.format("Пользователь с id %s не найден", user.getId()));
        }

        if (validator.isValid(user)) {
            userMap.put(userFound.getId(), user);
        }

        return user;
    }
}
