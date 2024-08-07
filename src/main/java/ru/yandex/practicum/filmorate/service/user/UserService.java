package ru.yandex.practicum.filmorate.service.user;

import ru.yandex.practicum.filmorate.dto.user.CreateUserDto;
import ru.yandex.practicum.filmorate.dto.user.UpdateUserDto;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;

public interface UserService {
    Collection<User> getAll();

    User getById(int id);

    User create(CreateUserDto user);

    User update(UpdateUserDto user);

    void addFriend(int userId, int friendId);

    void removeFriend(int userId, int friendId);

    List<User> getFriends(int userId);

    List<User> getCommonFriends(int userId, int otherId);
}
