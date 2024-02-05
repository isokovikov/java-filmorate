package ru.yandex.practicum.filmorate.controller;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

class UserControllerTest {
    UserController userController;
    UserService userService;

    @BeforeEach
    void beforeEach() {
        userController = new UserController(userService);
    }

    @Test
    void validationUserTest() {
        User inavalidUser = new User(id, email, login, name, userBirthday); // test null
        assertThrows(ValidationException.class, () -> userController.addNew(inavalidUser));
    }
}