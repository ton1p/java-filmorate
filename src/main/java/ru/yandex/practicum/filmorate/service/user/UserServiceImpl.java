package ru.yandex.practicum.filmorate.service.user;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;

    @Override
    public void addFriend(int userId, int friendId) {
        User user = userStorage.getById(userId);
        User friend = userStorage.getById(friendId);

        Set<Integer> userFriends = user.getFriends();
        if (userFriends == null) {
            userFriends = new HashSet<>();
        }
        userFriends.add(friend.getId());
        user.setFriends(userFriends);

        Set<Integer> friendFriends = friend.getFriends();
        if (friendFriends == null) {
            friendFriends = new HashSet<>();
        }
        friendFriends.add(user.getId());
        friend.setFriends(friendFriends);
    }

    @Override
    public void removeFriend(int userId, int friendId) {
        User user = userStorage.getById(userId);
        User friend = userStorage.getById(friendId);

        Set<Integer> userFriends = user.getFriends();
        if (userFriends != null) {
            userFriends.remove(friend.getId());
            user.setFriends(userFriends);
        }

        Set<Integer> friendFriends = friend.getFriends();
        if (friendFriends != null) {
            friendFriends.remove(user.getId());
            friend.setFriends(friendFriends);
        }
    }

    @Override
    public List<User> getFriends(int userId) {
        User user = userStorage.getById(userId);
        Set<Integer> userFriends = user.getFriends();

        if (userFriends != null && !userFriends.isEmpty()) {
            return user.getFriends()
                    .stream()
                    .map(userStorage::getById)
                    .toList();
        }

        return new ArrayList<>();
    }

    @Override
    public List<User> getCommonFriends(int userId, int otherId) {
        User user = userStorage.getById(userId);
        User otherUser = userStorage.getById(otherId);

        Set<Integer> userFriends = user.getFriends();
        Set<Integer> otherUserFriends = otherUser.getFriends();

        if (userFriends != null && otherUserFriends != null) {
            return userFriends
                    .stream()
                    .filter(otherUserFriends::contains)
                    .map(userStorage::getById)
                    .toList();
        }
        return List.of();
    }
}
