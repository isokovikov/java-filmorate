package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    User addNew(User user);

    User update(User user);

    void remove(User user);

    Optional<User> getById(Integer id);

    List<User> getList();

}

