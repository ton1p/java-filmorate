package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.BaseStorage;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
@Primary
public class UserDbStorage extends BaseStorage<User> implements UserStorage {
    private static final String GET_ALL_QUERY = "select u.*, uf.\"friend_id\" as friend_id from \"user\" u left join \"users_friends\" uf on u.\"id\" = uf.\"user_id\"";

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
        return null;
    }

    @Override
    public User update(User user) {
        return null;
    }

    @Override
    public void addFriend(User user, User friend) {

    }

    @Override
    public void removeFriend(User user, User friend) {

    }

    @Override
    public List<User> getFriends(User user) {
        return List.of();
    }

    @Override
    public List<User> getCommonFriends(User user, User other) {
        return List.of();
    }
}
