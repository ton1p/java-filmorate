package ru.yandex.practicum.filmorate.storage.user.extractor;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class UserExtractor implements ResultSetExtractor<List<User>> {
    @Override
    public List<User> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<Integer, User> usersMap = new HashMap<>();
        String friendIdColumnName = "FRIEND_ID";

        while (rs.next()) {
            int id = rs.getInt("id");
            if (!usersMap.containsKey(id)) {
                String email = rs.getString("email");
                String login = rs.getString("login");
                String name = rs.getString("name");
                LocalDate birthday = rs.getDate("birthday").toLocalDate();

                int friendId = rs.getInt(friendIdColumnName);
                Set<Integer> friendsIds = new HashSet<>();
                if (friendId != 0) {
                    friendsIds = new HashSet<>(Set.of(rs.getInt(friendIdColumnName)));
                }

                usersMap.put(id, new User(id, email, login, name, birthday, friendsIds));
            } else {
                int nextUserId = rs.getInt("id");
                Set<Integer> friendIds = usersMap.get(nextUserId).getFriends();
                int friendId = rs.getInt(friendIdColumnName);
                friendIds.add(friendId);
            }

        }

        return new ArrayList<>(usersMap.values());
    }
}
