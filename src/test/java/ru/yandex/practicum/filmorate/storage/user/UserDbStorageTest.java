package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.ContextConfiguration;
import ru.yandex.practicum.filmorate.dto.user.CreateUserDto;
import ru.yandex.practicum.filmorate.dto.user.UpdateUserDto;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.extractor.UserExtractor;
import ru.yandex.practicum.filmorate.storage.user.mapper.UserRowMapper;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ContextConfiguration(classes = {UserDbStorage.class, UserRowMapper.class, UserExtractor.class})
class UserDbStorageTest {
    private final UserStorage userStorage;

    private User createUser() {
        CreateUserDto createUserDto = CreateUserDto.builder()
                .name("test")
                .email("t@t.ru")
                .login("test")
                .birthday(LocalDate.now())
                .build();

        return userStorage.create(createUserDto);
    }

    @Test
    void getAll() {
        Collection<User> users = userStorage.getAll();
        Assertions.assertEquals(0, users.size());

        createUser();
        createUser();
        users = userStorage.getAll();
        Assertions.assertEquals(2, users.size());
    }

    @Test
    void getById() {
        User user = createUser();
        Optional<User> found = userStorage.getById(user.getId());
        Assertions.assertTrue(found.isPresent());
        Assertions.assertEquals(user.getId(), found.get().getId());
        Assertions.assertEquals(user.getName(), found.get().getName());
    }

    @Test
    void create() {
        User user = createUser();
        Assertions.assertNotNull(user);
    }

    @Test
    void update() {
        User user = createUser();
        Assertions.assertNotNull(user);
        user.setName("updated");

        UpdateUserDto updateUserDto = UserMapper.INSTANCE.userToUpdateUserDto(user);
        User updated = userStorage.update(updateUserDto);
        Assertions.assertNotNull(updated);
        Assertions.assertEquals(user.getId(), updated.getId());
        Assertions.assertEquals(user.getName(), updated.getName());
    }

    @Test
    void addFriend() {
        User user = createUser();
        User user2 = createUser();
        Assertions.assertNotNull(user);
        Assertions.assertNotNull(user2);

        userStorage.addFriend(user, user2);
        Optional<User> found = userStorage.getById(user.getId());

        Assertions.assertTrue(found.isPresent());
        Assertions.assertEquals(Set.of(user2.getId()), found.get().getFriends());
    }

    @Test
    void removeFriend() {
        User user = createUser();
        User user2 = createUser();
        Assertions.assertNotNull(user);
        Assertions.assertNotNull(user2);

        userStorage.addFriend(user, user2);
        Optional<User> found = userStorage.getById(user.getId());
        Assertions.assertTrue(found.isPresent());
        Assertions.assertEquals(Set.of(user2.getId()), found.get().getFriends());

        userStorage.removeFriend(user, user2);
        found = userStorage.getById(user.getId());
        Assertions.assertTrue(found.isPresent());
        Assertions.assertEquals(Set.of(), found.get().getFriends());
    }

    @Test
    void getFriends() {
        User user = createUser();
        User user2 = createUser();
        Assertions.assertNotNull(user);
        Assertions.assertNotNull(user2);

        userStorage.addFriend(user, user2);
        user = userStorage.getById(user.getId()).orElse(null);
        List<User> userFriends = userStorage.getFriends(user);

        Assertions.assertEquals(1, userFriends.size());
        Assertions.assertEquals(userFriends.getFirst(), user2);
    }

    @Test
    void getCommonFriends() {
        User user = createUser();
        User user2 = createUser();
        User user3 = createUser();
        User user4 = createUser();
        Assertions.assertNotNull(user);
        Assertions.assertNotNull(user2);
        Assertions.assertNotNull(user3);
        Assertions.assertNotNull(user4);

        userStorage.addFriend(user, user3);
        userStorage.addFriend(user, user4);
        userStorage.addFriend(user, user2);

        userStorage.addFriend(user2, user3);
        userStorage.addFriend(user2, user4);
        userStorage.addFriend(user2, user);

        user = userStorage.getById(user.getId()).orElse(null);
        user2 = userStorage.getById(user2.getId()).orElse(null);
        Assertions.assertNotNull(user);
        Assertions.assertNotNull(user2);

        List<User> commonFriends = userStorage.getCommonFriends(user, user2);
        Assertions.assertEquals(2, commonFriends.size());
        Assertions.assertEquals(user3.getId(), commonFriends.get(0).getId());
        Assertions.assertEquals(user4.getId(), commonFriends.get(1).getId());
    }
}
