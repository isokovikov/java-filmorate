package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Repository
@Slf4j
public class FriendsDbStorage implements FriendsStorage {

    private final JdbcTemplate jdbcTemplate;

    public FriendsDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addFriend(long userId, long friendId) {
        String sqlQuery = "merge into FRIENDS(USER_ID, FRIEND_ID) values (?, ?)";
        jdbcTemplate.update(sqlQuery, userId, friendId);
    }

    @Override
    public void deleteFriend(long userId, long friendId) {
        String sqlQuery = "delete from FRIENDS where USER_ID = ? AND FRIEND_ID = ?";
        jdbcTemplate.update(sqlQuery, userId, friendId);
    }

    @Override
    public List<User> getFriends(Integer userId) {
        String sqlQuery = "select * from USERS, FRIENDS where USERS.USER_ID = FRIENDS.FRIEND_ID AND FRIENDS.USER_ID = ?";
        return jdbcTemplate.query(sqlQuery, FriendsDbStorage::makeUser, userId);
    }

    @Override
    public List<User> commonFriends(Integer userId, Integer friendId) {
        String sqlQuery = "SELECT u.USER_ID," +
                "u.EMAIL," +
                "u.LOGIN," +
                "u.USER_NAME," +
                "u.BIRTHDAY " +
                "FROM FRIENDS AS fr1 " +
                "INNER JOIN FRIENDS AS fr2 ON fr1.FRIEND_ID = fr2.FRIEND_ID " +
                "LEFT OUTER JOIN USERS u ON fr1.FRIEND_ID = u.USER_ID " +
                "WHERE fr1.USER_ID = ? AND fr2.USER_ID = ? ";
        return jdbcTemplate.query(sqlQuery, FriendsDbStorage::makeUser, userId, friendId);
    }

    private static User makeUser(ResultSet resultSet, int rowNum) throws SQLException {
        Integer id = resultSet.getInt("USER_ID");
        String email = resultSet.getString("EMAIL");
        String login = resultSet.getString("LOGIN");
        String name = resultSet.getString("USER_NAME");
        Date birthday = resultSet.getDate("BIRTHDAY");
        LocalDate userBirthday = null;
        if (birthday != null) {
            userBirthday = birthday.toLocalDate();
        }
        return new User(id, email, login, name, userBirthday);
    }
}
