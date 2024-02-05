package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public UserServiceImpl(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    @Override
    public User addNew(User user) {
        nameEqualsLogin(user);
        return userStorage.addNew(user);
    }

    @Override
    public User update(User user) {
        return userStorage.getById(user.getId())
                .map(u -> {
                    nameEqualsLogin(user);
                    return userStorage.update(user);
                })
                .orElseThrow(() ->
                        new UserNotFoundException("The user with ID " + user.getId() + " was not found"));
    }

    @Override
    public User remove(Integer id) {
        return userStorage.remove(id);
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
    public User addFriend(Integer userId, Integer friendId) {
        User user = userStorage.getById(userId)
                .orElseThrow(() ->
                        new UserNotFoundException("User with ID " + userId + " was not found"));
        User friendUser = userStorage.getById(friendId)
                .orElseThrow(() ->
                        new UserNotFoundException("Friend with ID " + friendId + " was not found"));

        user.getFriendsId().add(friendId);
        friendUser.getFriendsId().add(userId);
        userStorage.update(friendUser);

        return userStorage.update(user);
    }

    @Override
    public User removeFriend(Integer userId, Integer friendId) {
        // Проверяем наличие обоих пользователей и выбрасываем исключение, если кто-то не найден
        User user = userStorage.getById(userId)
                .orElseThrow(() ->
                        new UserNotFoundException("User with ID " + userId + " was not found"));
        User friendUser = userStorage.getById(friendId)
                .orElseThrow(() ->
                        new UserNotFoundException("Friend with ID " + friendId + " was not found"));

        // Удаляем идентификатор друга из списка друзей пользователя и наоборот
        boolean removedFromUser = user.getFriendsId().remove(friendId);
        boolean removedFromFriend = friendUser.getFriendsId().remove(userId);

        // Проверяем, были ли произведены изменения, и обновляем информацию в хранилище
        if (removedFromUser) {
            userStorage.update(user);
        }
        if (removedFromFriend) {
            userStorage.update(friendUser);
        }

        // Возвращаем обновленного пользователя
        return user;
    }

    @Override
    public List<User> getCommonFriends(Integer userId, Integer otherId) {
        User user = userStorage.getById(userId)
                .orElseThrow(() ->
                        new UserNotFoundException("User with ID " + userId + " was not found"));
        User otherUser = userStorage.getById(otherId)
                .orElseThrow(() ->
                        new UserNotFoundException("User with ID " + otherId + " was not found"));

        return user.getFriendsId().stream()
                .filter(otherUser.getFriendsId()::contains)
                .map(id -> userStorage.getById(id).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> getAllFriends(Integer userId) {
        // Используем один вызов getById для получения пользователя.
        User user = userStorage.getById(userId)
                .orElseThrow(() ->
                        new UserNotFoundException("The user with ID " + userId + " was not found"));

        // Возвращаем список друзей, преобразуя их идентификаторы в объекты User.
        return user.getFriendsId().stream()
                .map(this::getById)
                .collect(Collectors.toList());
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