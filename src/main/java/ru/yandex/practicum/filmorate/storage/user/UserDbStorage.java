package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.InternalServerException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.BaseStorage;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
@Primary
@Slf4j
public class UserDbStorage extends BaseStorage<User> implements UserStorage {
    private static final String GET_ALL_QUERY = "select u.*, uf.\"friend_id\" as friend_id from \"user\" u left join \"users_friends\" uf on u.\"id\" = uf.\"user_id\"";
    private static final String CREATE_QUERY = "insert into \"user\" (\"email\", \"login\", \"name\", \"birthday\") values (?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "update \"user\" set \"email\" = ?, \"login\" = ?, \"name\" = ?, \"birthday\" = ? where \"id\" = ?";

    public UserDbStorage(JdbcTemplate jdbcTemplate, RowMapper<User> mapper, ResultSetExtractor<List<User>> extractor) {
        super(jdbcTemplate, mapper, extractor);
    }

    @Override
    public Collection<User> getAll() {
        return findMany(GET_ALL_QUERY);
    }

    @Override
    public Optional<User> getById(int id) {
        return findOne(GET_ALL_QUERY + " where u.\"id\" = ?", id);
    }

    @Override
    public User create(User user) {
        int id = insert(
                CREATE_QUERY,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday().toString()
        );
        return getById(id).orElse(null);
    }

    @Override
    public User update(User user) {
        update(
                UPDATE_QUERY,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday().toString(),
                user.getId()
        );
        return getById(user.getId()).orElse(null);
    }

    @Override
    public void addFriend(User user, User friend) {
        final String query = "insert into \"users_friends\" (\"user_id\", \"friend_id\") values (?, ?)";
        update(query, user.getId(), friend.getId());
    }

    @Override
    public void removeFriend(User user, User friend) {
        final String query = "delete from \"users_friends\" where \"user_id\" = ? and \"friend_id\" = ?";
        try {
            update(query, user.getId(), friend.getId());
        } catch (InternalServerException e) {
            log.info("У Пользователя с id = {}", user.getId() + " нет друга с id = " + friend.getId());
        }
    }

    @Override
    public List<User> getFriends(User user) {
        if (user.getFriends().isEmpty()) {
            return List.of();
        }
        return findMany(GET_ALL_QUERY + " where u.\"id\" in (?)", user.getFriends().toArray());
    }

    @Override
    public List<User> getCommonFriends(User user, User other) {
        Set<Integer> userFriends = user.getFriends();
        Set<Integer> otherFriends = other.getFriends();
        return findMany(
                GET_ALL_QUERY + " where u.\"id\" in (?)",
                userFriends.stream().filter(otherFriends::contains).distinct().toArray()
        );
    }
}
