package ru.yandex.practicum.filmorate.service.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.user.CreateUserDto;
import ru.yandex.practicum.filmorate.dto.user.UpdateUserDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.validator.user.UserValidator;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;
    private final UserValidator userValidator;

    @Override
    public Collection<User> getAll() {
        return userStorage.getAll();
    }

    @Override
    public User getById(int id) {
        Optional<User> user = userStorage.getById(id);
        if (user.isEmpty()) {
            log.error("Пользователь с id {} не найден", id);
            throw new NotFoundException(String.format("Пользователь с id %s не найден", id));
        }
        return user.get();
    }

    @Override
    public User create(CreateUserDto createUserDto) {
        User user = UserMapper.INSTANCE.createUserDtoToUser(createUserDto);
        if (userValidator.isValid(user)) {
            return userStorage.create(createUserDto);
        }
        return null;
    }

    @Override
    public User update(UpdateUserDto updateUserDto) {
        User user = UserMapper.INSTANCE.updateUserDtoToUser(updateUserDto);
        if (getById(user.getId()) != null && userValidator.isValid(user)) {
            return userStorage.update(updateUserDto);
        }
        return null;
    }

    @Override
    public void addFriend(int userId, int friendId) {
        User user = getById(userId);
        User friend = getById(friendId);
        userStorage.addFriend(user, friend);
    }

    @Override
    public void removeFriend(int userId, int friendId) {
        User user = getById(userId);
        User friend = getById(friendId);
        userStorage.removeFriend(user, friend);
    }

    @Override
    public List<User> getFriends(int userId) {
        User user = getById(userId);
        return userStorage.getFriends(user);
    }

    @Override
    public List<User> getCommonFriends(int userId, int otherId) {
        User user = getById(userId);
        User other = getById(otherId);
        return userStorage.getCommonFriends(user, other);
    }
}
