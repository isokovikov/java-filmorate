package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import jakarta.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(value = "/users")
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    protected static int nextUserId = 0;

    @PostMapping
    public User addNewUser(@Valid @RequestBody User user) {
        userValidation(user);
        user.setId(getNextUserId());
        users.put(user.getId(), user);
        log.info("A new user has been added");
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        userValidation(user);
        if (!(users.keySet().contains(user.getId()))) {
            throw new ValidationException("User unknown");
        }
        users.put(user.getId(), user);
        log.info("User was updated");
        return user;
    }

    @GetMapping
    public Collection<User> returnUsers() {
        Collection<User> userList = users.values();
        log.info("Received a request for a list of users");
        return userList;
    }

    private void userValidation(User user) {
        if (user == null) {
            throw new ValidationException("User cannot be null");
        }

        if (user.getLogin() == null || user.getLogin().contains(" ")) {
            log.info("The user failed validation: invalid login");
            throw new ValidationException("The login cannot be empty and contain spaces");
        }

        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

    public static int getNextUserId() {
        return ++nextUserId;
    }
}
