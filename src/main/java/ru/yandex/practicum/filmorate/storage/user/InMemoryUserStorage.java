package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> userMap;

    public InMemoryUserStorage() {
        this.userMap = new HashMap<>();
    }

    public int getNextId() {
        int currentMaxId = userMap.keySet()
                .stream()
                .mapToInt(value -> value)
                .max()
                .orElse(0);

        return ++currentMaxId;
    }

    @Override
    public Collection<User> getAll() {
        return userMap.values();
    }

    @Override
    public Optional<User> getById(int id) {
        User user = userMap.get(id);

        if (user == null) {
            return Optional.empty();
        }

        return Optional.of(user);
    }

    @Override
    public User create(User user) {
        user.setId(getNextId());
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        userMap.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User user) {
        userMap.put(user.getId(), user);
        return user;
    }

    @Override
    public void addFriend(User user, User friend) {
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
    public void removeFriend(User user, User friend) {
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
    public List<User> getFriends(User user) {
        Set<Integer> userFriends = user.getFriends();

        if (userFriends != null && !userFriends.isEmpty()) {
            return user.getFriends()
                    .stream()
                    .map(this::getById)
                    .map(u -> u.orElse(null))
                    .toList();
        }

        return new ArrayList<>();
    }

    @Override
    public List<User> getCommonFriends(User user, User other) {
        Set<Integer> userFriends = user.getFriends();
        Set<Integer> otherUserFriends = other.getFriends();

        if (userFriends != null && otherUserFriends != null) {
            return userFriends
                    .stream()
                    .filter(otherUserFriends::contains)
                    .map(this::getById)
                    .map(u -> u.orElse(null))
                    .toList();
        }

        return List.of();
    }
}
