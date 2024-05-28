package ru.yandex.practicum.filmorate.service;

import java.util.Collection;

public interface RestService<T> {
    Collection<T> getAll();

    T create(T o);

    T update(T o);

    int getNextId();
}
