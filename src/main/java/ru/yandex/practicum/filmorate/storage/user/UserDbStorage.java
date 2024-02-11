package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@Slf4j
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<User> getList() {
        String sqlQuery = "select * from USERS";
        return jdbcTemplate.query(sqlQuery, new UserMapper());
    }

    @Override
    public User addNew(User user) {
        String sqlQuery = "insert into USERS(USER_NAME, EMAIL, LOGIN, BIRTHDAY) values (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"USER_ID"});
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getLogin());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);
        user.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        return user;
    }

    @Override
    public User update(User user) {
        String sqlQuery = "update USERS set " +
                "USER_NAME = ?, EMAIL = ?, LOGIN = ? , BIRTHDAY = ?" +
                "where USER_ID = ?";
        jdbcTemplate.update(sqlQuery,
                user.getName(),
                user.getEmail(),
                user.getLogin(),
                user.getBirthday(),
                user.getId());
        return user;
    }

    @Override
    public void remove(User user) {
        //Добавление проверки существования пользователя перед удалением
        String checkSql = "select count(*) form USERS where USER_ID = ?";
        Integer count = jdbcTemplate.queryForObject(checkSql, Integer.class, user.getId());
        if (count == null || count == 0) {
            throw new UserNotFoundException("User with id " + user.getId() + " does not exist.");
        }

        String sqlQuery = "delete from USERS where USER_ID = ?";
        if (jdbcTemplate.update(sqlQuery, user.getId()) > 0) {
            log.info("User with ID {} was removed", user.getId());
        } else {
            log.info("User with ID {} was not removed", user.getId());
        }
    }

    @Override
    public Optional<User> getById(Integer id) {
        String sqlQuery = "select * from USERS where USER_ID = ?";
        List<User> userRows = jdbcTemplate.query(sqlQuery, new UserMapper(), id);
        if (!userRows.isEmpty()) {
            User user = userRows.get(0);
            log.info("User found: {} {}", user.getId(), user.getLogin());
            return Optional.of(user);
        } else {
            return Optional.empty();
        }
    }

}
