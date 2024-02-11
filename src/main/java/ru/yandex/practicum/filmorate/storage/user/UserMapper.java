package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

// Вынесение метода makeUser в отдельный класс
public class UserMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        Integer id = rs.getInt("USER_ID");
        String email = rs.getString("EMAIL");
        String login = rs.getString("LOGIN");
        String name = rs.getString("USER_NAME");
        Date birthday = rs.getDate("BIRTHDAY");
        LocalDate userBirthday = null;
        if (birthday != null) {
            userBirthday = birthday.toLocalDate();
        }
        return new User(id, email, login, name, userBirthday);
    }
}
