package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserService {
    User addNew(User user);

    User update(User user);

    void remove(User yser);

    User getById(Integer id);

    List<User> getList();

    void addFriend(Integer id, Integer friendId);

    void removeFriend(Integer userId, Integer friendId);

    List<User> getCommonFriends(Integer userId, Integer otherId);

    List<User> getAllFriends(Integer userId);

    void nameEqualsLogin(User user);
}