package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@Repository
@Slf4j
public class FriendsDbStorage implements FriendsStorage {

    private final JdbcTemplate jdbcTemplate;
    private static final String addFriendSql = "MERGE INTO FRIENDS(USER_ID, FRIEND_ID) VALUES (?, ?)";
    private static final String delFriendSql = "delete from FRIENDS where USER_ID = ? AND FRIEND_ID = ?";
    private static final String getFriendsSql = "select * from USERS, FRIENDS where USERS.USER_ID = FRIENDS.FRIEND_ID AND FRIENDS.USER_ID = ?";
    private static final String commonFriendsSql = "SELECT u.USER_ID," +
            "u.EMAIL," +
            "u.LOGIN," +
            "u.USER_NAME," +
            "u.BIRTHDAY " +
            "FROM FRIENDS AS fr1 " +
            "INNER JOIN FRIENDS AS fr2 ON fr1.FRIEND_ID = fr2.FRIEND_ID " +
            "LEFT OUTER JOIN USERS u ON fr1.FRIEND_ID = u.USER_ID " +
            "WHERE fr1.USER_ID = ? AND fr2.USER_ID = ? ";


    public FriendsDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addFriend(long userId, long friendId) {
        jdbcTemplate.update(addFriendSql, userId, friendId);
    }

    @Override
    public void deleteFriend(long userId, long friendId) {
        jdbcTemplate.update(delFriendSql, userId, friendId);
    }

    @Override
    public List<User> getFriends(Integer userId) {
        return jdbcTemplate.query(getFriendsSql, new UserMapper(), userId);
    }

    @Override
    public List<User> commonFriends(Integer userId, Integer friendId) {
        return jdbcTemplate.query(commonFriendsSql, new UserMapper(), userId, friendId);
    }
}
