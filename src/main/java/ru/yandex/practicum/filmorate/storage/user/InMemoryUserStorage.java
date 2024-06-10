package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.UserValidator;
import ru.yandex.practicum.filmorate.validator.Validator;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> userMap;
    private final Validator<User> validator;

    public InMemoryUserStorage() {
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
    public User getById(int id) {
        User user = userMap.get(id);

        if (user == null) {
            log.error("Пользователь с id {} не найден", id);
            throw new NotFoundException(String.format("Пользователь с id %s не найден", id));
        }

        return user;
    }

    @Override
    public User create(User user) {
        if (validator.isValid(user)) {
            user.setId(getNextId());
            if (user.getName() == null) {
                user.setName(user.getLogin());
            }
            userMap.put(user.getId(), user);
        }
        return user;
    }

    @Override
    public User update(User user) {
        User userFound = getById(user.getId());

        if (validator.isValid(user)) {
            userMap.put(userFound.getId(), user);
        }

        return user;
    }
}
