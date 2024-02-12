package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.FriendsStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;
    private final FriendsStorage friendsStorage;

    @Override
    public User addNew(User user) {
        nameEqualsLogin(user);
        return userStorage.addNew(user);
    }

    @Override
    public User update(User user) {
        nameEqualsLogin(user);
        getById(user.getId());
        return userStorage.update(user);
    }

    @Override
    public void remove(User user) {
        userStorage.remove(user);
    }

    @Override
    public User getById(Integer id) {
        return userStorage.getById(id)
                .orElseThrow(() ->
                        new UserNotFoundException("The user with ID " + id + " was not found"));
    }

    @Override
    public List<User> getList() {
        return userStorage.getList();
    }

    @Override
    public void addFriend(Integer userId, Integer friendId) {
        if (userId < 0 || friendId < 0) {
            throw new UserNotFoundException("ID " + userId + " or " + friendId + " not found.");
        }
        friendsStorage.addFriend(userId, friendId);
    }

    @Override
    public void removeFriend(Integer userId, Integer friendId) {
        friendsStorage.deleteFriend(userId, friendId);
    }

    @Override
    public List<User> getCommonFriends(Integer userId, Integer otherId) {
        return friendsStorage.commonFriends(userId, otherId);
    }

    @Override
    public List<User> getAllFriends(Integer userId) {
        return friendsStorage.getFriends(userId);
    }

    @Override
    public void nameEqualsLogin(User user) {
        if ((user.getName() == null) || (user.getName().isBlank())) {
            user.setName(user.getLogin());
        }
        if (user.getLogin().contains(" ")) {
            throw new ValidationException("The login '" + user.getLogin() + "' must not contain spaces.");
        }
    }
}