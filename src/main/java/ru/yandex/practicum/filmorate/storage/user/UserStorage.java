package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.dto.user.CreateUserDto;
import ru.yandex.practicum.filmorate.dto.user.UpdateUserDto;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserStorage {
    Collection<User> getAll();

    Optional<User> getById(int id);

    User create(CreateUserDto user);

    User update(UpdateUserDto user);

    void addFriend(User user, User friend);

    void removeFriend(User user, User friend);

    List<User> getFriends(User user);

    List<User> getCommonFriends(User user, User other);
}
