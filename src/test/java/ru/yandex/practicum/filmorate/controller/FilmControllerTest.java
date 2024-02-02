package ru.yandex.practicum.filmorate.controller;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

class FilmControllerTest {
    FilmController filmController;
    FilmService filmService;

    @BeforeEach
    void beforeEach() {
        filmController = new FilmController(filmService);
    }

    @Test
    void validationFilmTest() {
        Film inavalidFilm = new Film(); // test null
        assertThrows(ValidationException.class, () -> filmController.addNew(inavalidFilm));
    }
}