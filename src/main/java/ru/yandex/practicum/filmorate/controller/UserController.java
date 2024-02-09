package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<User> returnAll() {
        log.info("Request GET /users received");
        return userService.getList();
    }

    @GetMapping("/{id}")
    public User getById(@PathVariable Integer id) {
        log.info("Request GET /users/{} received", id);
        return userService.getById(id);
    }

    @GetMapping("/{id}/friends")
    public List<User> getAllFriends(@PathVariable Integer id) {
        log.info("Request GET /users/{}/friends received", id);
        return  userService.getAllFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable Integer id, @PathVariable Integer otherId) {
        log.info("Request GET /users/{}/friends/common/{}", id, otherId);
        return userService.getCommonFriends(id, otherId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User addNew(@Valid @RequestBody User user) {
        log.info("Request POST /users");
        userValidation(user);
        User addUser = userService.addNew(user);
        log.info("A new user has been added, " + user.getId());
        return addUser;
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        userValidation(user);
        log.info("Request PUT /users");
        User updateUser = userService.update(user);
        log.info("User with ID " + user.getId() + " was updated");
        return updateUser;
    }

    @PutMapping("/{id}/friends/{friendId}")
    public boolean addFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        log.info("Request PUT /users/{}/friends/{}", id, friendId);
        userService.addFriend(id, friendId);
        log.info("Friend was added");
        return true;
    }

    @DeleteMapping("/{id}")
    public User deleteById(@PathVariable Integer id) {
        log.info("Request DELETE /users/{}", id);
        User removeUser = userService.getById(id);
        userService.remove(removeUser);
        log.info("User was deleted");
        return removeUser;
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public boolean removeFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        log.info("Request DELETE /users/{}/friends/{}", id, friendId);
        userService.removeFriend(id, friendId);
        log.info("Friend was deleted");
        return true;
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
}
