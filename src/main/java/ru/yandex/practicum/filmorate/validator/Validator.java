package ru.yandex.practicum.filmorate.validator;

public interface Validator<T> {
    boolean isValid(T o);
}
