package ru.yandex.practicum.filmorate.storage.user.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component
public class UserRowMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        Map<Integer, User> userMap = new HashMap<>();
        User user = User.builder()
                .id(rs.getInt("id"))
                .email(rs.getString("email"))
                .login(rs.getString("login"))
                .name(rs.getString("name"))
                .birthday(rs.getDate("birthday").toLocalDate())
                .friends(new HashSet<>())
                .build();

        int friendId = rs.getInt("FRIEND_ID");
        if (friendId != 0) {
            Set<Integer> friendIds = user.getFriends();
            friendIds.add(friendId);
            user.setFriends(friendIds);
        }

        userMap.putIfAbsent(user.getId(), user);

        while (rs.next()) {
            int id = rs.getInt("id");
            int nextUserFriendId = rs.getInt("FRIEND_ID");
            if (userMap.containsKey(id) && nextUserFriendId != 0) {
                Set<Integer> friendIds = userMap.get(id).getFriends();
                friendIds.add(nextUserFriendId);
                userMap.get(id).setFriends(friendIds);
            }

        }


        return userMap.get(user.getId());
    }
}
