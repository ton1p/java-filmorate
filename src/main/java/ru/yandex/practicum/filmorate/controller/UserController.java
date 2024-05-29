package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.RestService;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController extends BaseController {
    final RestService<User> userRestService = new UserService();

    @GetMapping
    public Collection<User> getUsers() {
        log.info("Получение всех пользователей");
        return userRestService.getAll();
    }

    @PostMapping
    public User createUser(@RequestBody User body) {
        log.info("Создание нового пользователя");
        return userRestService.create(body);
    }

    @PutMapping
    public User updateUser(@RequestBody User body) {
        log.info("Обновление пользователя");
        return userRestService.update(body);
    }
}