package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserStorage userStorage;
    private final UserService userService;

    @GetMapping
    public Collection<User> getUsers() {
        log.info("Получение всех пользователей");
        return userStorage.getAll();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable("id") int id) {
        return userStorage.getById(id);
    }

    @PostMapping
    public User createUser(@RequestBody User body) {
        log.info("Создание нового пользователя");
        return userStorage.create(body);
    }

    @PutMapping
    public User updateUser(@RequestBody User body) {
        log.info("Обновление пользователя");
        return userStorage.update(body);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable("id") int id, @PathVariable("friendId") int friendId) {
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeFriend(@PathVariable("id") int id, @PathVariable("friendId") int friendId) {
        userService.removeFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public Collection<User> getFriends(@PathVariable("id") int id) {
        return userService.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> getFriends(@PathVariable("id") int id, @PathVariable("otherId") int otherId) {
        return userService.getCommonFriends(id, otherId);
    }
}
