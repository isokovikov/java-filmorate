package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendsStorage {
    void addFriend(long userId, long friendId);

    void deleteFriend(long userId, long friendId);

    List<User> getFriends(Integer userId);

    List<User> commonFriends(Integer userId, Integer friendId);
}
