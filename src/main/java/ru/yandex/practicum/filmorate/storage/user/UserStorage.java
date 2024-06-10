package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {
    Collection<User> getAll();

    User getById(int id);

    User create(User o);

    User update(User o);

    int getNextId();
}
