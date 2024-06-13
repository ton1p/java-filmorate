package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserStorage {
    Collection<User> getAll();

    Optional<User> getById(int id);

    User create(User user);

    User update(User user);

    void addFriend(User user, User friend);

    void removeFriend(User user, User friend);

    List<User> getFriends(User user);

    List<User> getCommonFriends(User user, User other);
}
